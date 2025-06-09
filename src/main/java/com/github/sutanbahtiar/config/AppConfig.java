package com.github.sutanbahtiar.config;

/*
 * @author sutan.bahtiar@gmail.com
 */

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("app.config")
public class AppConfig {
    private Kubelet kubelet;
    private HttpClient httpClient;

    @Data
    @Configuration
    @ConfigurationProperties("app.config.kubelet")
    public static class Kubelet {
        private String baseSandboxUrl;
        private String baseProductionUrl;
        private String metricsUrl;
        private String metricsCadvisorUrl;
        private String metricsResourceUrl;
        private String sandboxToken;
        private String productionToken;
    }

    @Data
    @Configuration
    @ConfigurationProperties("app.config.http-client")
    public static class HttpClient {
        private Integer connectTimeout;
        private Integer readTimeout;
        private Integer writeTimeout;
    }
}
