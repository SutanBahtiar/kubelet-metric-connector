package com.github.sutanbahtiar.handler;

/*
 * @author sutan.bahtiar@gmail.com
 */

import com.github.sutanbahtiar.service.KubeletSandboxClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Consumer;

@Log4j2
@Service
@RequiredArgsConstructor
public class KubeletMetricSandboxHandler {
    private final KubeletSandboxClientService clientService;

    public Mono<ServerResponse> getMetrics(ServerRequest serverRequest) {
        final String logId = String.valueOf(UUID.randomUUID());
        log.info("{}, Request getMetrics..", logId);
        return clientService
                .callGetMetrics(logId)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .doOnSuccess(getOnSuccess(logId));
    }

    public Mono<ServerResponse> getMetricsCadvisor(ServerRequest serverRequest) {
        final String logId = String.valueOf(UUID.randomUUID());
        log.info("{}, Request getMetricsCadvisor..", logId);
        return clientService
                .callGetMetricsCadvisor(logId)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .doOnSuccess(getOnSuccess(logId));
    }

    public Mono<ServerResponse> getMetricsResource(ServerRequest serverRequest) {
        final String logId = String.valueOf(UUID.randomUUID());
        log.info("{}, Request getMetricsResource..", logId);
        return clientService
                .callGetMetricsResource(logId)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .doOnSuccess(getOnSuccess(logId));
    }

    private static Consumer<ServerResponse> getOnSuccess(String logId) {
        return serverResponse -> log.info("{}, status: {}",
                logId, serverResponse.statusCode());
    }
}
