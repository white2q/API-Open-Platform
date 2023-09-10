package com.ppf.apiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.ppf.apiclientsdk.model.User;
import com.ppf.apiclientsdk.utils.SignUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author panpengfei
 * @date 2023/8/15
 */
public class ApiClient {

    private String accessKey;
    private String secreteKey;

    private static final String GATEWAY_HOST = "http://localhost:8003";

    public ApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secreteKey = secretKey;
    }

    public String getNameByGET(String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result= HttpUtil.get(GATEWAY_HOST + "/api/user/get", paramMap);
        System.out.println(result);
        return result;
    }

    public String getNameByPOST(String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result= HttpUtil.post(GATEWAY_HOST + "/api/user/post", paramMap);
        return result;
    }

    public String getUserNameByPOST(User user) {
        String json = JSONUtil.toJsonStr(user);
        String result = HttpRequest.post( GATEWAY_HOST + "/api/user/user")
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute().body();
        System.out.println(result);
        return result;
    }

    private Map<String, String> getHeaderMap(String body) {
        Map<String, String> map = new HashMap<>();
        map.put("accessKey", accessKey);
        map.put("sign", SignUtil.genSign(body, secreteKey));
        map.put("nonce", RandomUtil.randomNumbers(4));
        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        map.put("body", body);
        return map;
    }
}
