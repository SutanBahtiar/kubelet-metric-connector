package com.github.sutanbahtiar.service;

/*
 * @author sutan.bahtiar@gmail.com
 */

import com.github.sutanbahtiar.config.AppConfig;
import com.github.sutanbahtiar.helper.WebClientHelper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class KubeletSandboxSandboxClientServiceImpl extends WebClientHelper implements KubeletSandboxClientService {
    private final AppConfig appConfig;

    public KubeletSandboxSandboxClientServiceImpl(WebClient webClientSandbox,
                                                  AppConfig appConfig) {
        super(webClientSandbox, appConfig.getKubelet().getSandboxToken());
        this.appConfig = appConfig;
    }


    @Override
    public Mono<Object> callGetMetrics(String logId) {
        final String path = appConfig.getKubelet().getMetricsUrl();
        return get(logId, path);
    }

    @Override
    public Mono<Object> callGetMetricsCadvisor(String logId) {
        final String path = appConfig.getKubelet().getMetricsCadvisorUrl();
        return get(logId, path);
    }

    @Override
    public Mono<Object> callGetMetricsResource(String logId) {
        final String path = appConfig.getKubelet().getMetricsResourceUrl();
        return get(logId, path);
    }
}
