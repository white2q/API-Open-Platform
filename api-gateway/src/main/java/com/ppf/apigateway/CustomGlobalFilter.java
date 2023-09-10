package com.ppf.apigateway;

import com.ppf.apiclientsdk.utils.SignUtil;
import com.ppf.apicommon.model.entity.InterfaceInfo;
import com.ppf.apicommon.model.entity.User;
import com.ppf.apicommon.service.InnerInterfaceInfoService;
import com.ppf.apicommon.service.InnerUserInterfaceInfoService;
import com.ppf.apicommon.service.InnerUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    private static final List<String> IP_BLACK_LIST = new ArrayList<>();

    private static final long FIVE_MINUTES = 5 * 60L;

    public static final String HOST = "http://localhost:8002";

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String url = request.getPath().value();
        String path = HOST + url;
        String requestId = request.getId();
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        String method = request.getMethodValue();
        String sourceAddress = Objects.requireNonNull(request.getLocalAddress()).getHostString();
        // 1. 请求日志
        log.info("请求唯一标识id号: " + requestId);
        log.info("请求路径: " + path);
        log.info("请求方式: " + method);
        log.info("请求参数: " + queryParams);
        log.info("请求来源地址: " + sourceAddress);
        // 2. 访问控制-黑白名单
        // todo 读取文件判断是否在黑名单中
        if(IP_BLACK_LIST.contains(sourceAddress)) {
            handleNoAuth(response);
        }
        // 3. 用户鉴权 ak-sk
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timeStamp = headers.getFirst("timeStamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");
        // 随机数 < 10000
        assert nonce != null;
        if(Long.parseLong(nonce) > 10000) {
            return handleNoAuth(response);
        }
        // timeStamp 不超过5min
        long currentTime = System.currentTimeMillis() / 1000;
        assert timeStamp != null;
        if(currentTime - Long.parseLong(timeStamp) >= FIVE_MINUTES) {
            return handleNoAuth(response);
        }
        User user = null;
        try {
            user = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.error("getInvokeUser error", e);
        }
        if(user == null) {
            return handleNoAuth(response);
        }
        String secretKey = user.getSecretKey();
        assert sign != null;
        if(!sign.equals(SignUtil.genSign(body, secretKey))) {
            return handleNoAuth(response);
        }
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(HOST, url, method);
        } catch (Exception e) {
            log.error("getInterfaceInfo error", e);
        }
        if(interfaceInfo == null) {
            return handleNoAuth(response);
        }
        return handleResponse(exchange, chain, interfaceInfo.getId(), user.getId());
    }

    /**
     * 响应装饰器
     *
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long userId) {
        try {
            // 获取原始的response对象
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 获取数据缓冲工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 获取响应状态码
            HttpStatus statusCode = originalResponse.getStatusCode();

            if(statusCode == HttpStatus.OK){
                // 获取装饰器response对象
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 重写writeWith,调用接口后执行处理返回值内容及增强能力
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                try {
                                    innerUserInterfaceInfoService.invokeCount(interfaceInfoId, userId);
                                } catch (Exception e) {
                                    log.error("invokeCount error", e);
                                }
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);//释放掉内存
                                // 构建日志
                                String data = new String(content, StandardCharsets.UTF_8);//data
                                // 打印日志
                                log.info("响应信息：" + data);
                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            // 8.调用失败， 返回错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);//降级处理返回数据
        }catch (Exception e){
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        // 403
        response.setStatusCode(HttpStatus.FORBIDDEN);
        // 返回处理后的响应
        return response.setComplete();
    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response) {
        // 500
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        // 返回处理后的响应
        return response.setComplete();
    }
}