package com.github.sutanbahtiar.service;

/*
 * @author sutan.bahtiar@gmail.com
 */

import reactor.core.publisher.Mono;

public interface KubeletClientService {

    Mono<Object> callGetMetrics(String logId);

    Mono<Object> callGetMetricsCadvisor(String logId);

    Mono<Object> callGetMetricsResource(String logId);
}
