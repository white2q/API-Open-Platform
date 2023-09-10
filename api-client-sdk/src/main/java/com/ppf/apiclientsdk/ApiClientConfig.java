package com.ppf.apiclientsdk;

import com.ppf.apiclientsdk.client.ApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 读取配置初始化客户端
 *
 * @author panpengfei
 * @date 2023/8/22
 */

@Data
@Configuration
@ComponentScan
@ConfigurationProperties("api.client")
public class ApiClientConfig {
    private String accessKey;
    private String secreteKey;

    @Bean
    public ApiClient apiClient() {
        return new ApiClient(accessKey, secreteKey);
    }

}
