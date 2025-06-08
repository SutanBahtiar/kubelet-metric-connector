package com.github.sutanbahtiar.service;

/*
 * @author sutan.bahtiar@gmail.com
 */

import com.github.sutanbahtiar.config.AppConfig;
import com.github.sutanbahtiar.constants.RequestHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@RequiredArgsConstructor
public class KubeletClientServiceImpl implements KubeletClientService {
    private final WebClient webClient;
    private final AppConfig appConfig;

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

    private Mono<Object> get(String logId,
                             String path) {
        final MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add(RequestHeaders.AUTHORIZATION, RequestHeaders.BEARER + appConfig.getKubelet().getToken());

        return webClient.get()
                .uri(path)
                .headers(httpHeaders -> httpHeaders.addAll(header))
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful())
                        return clientResponse.bodyToMono(String.class);

                    return createException(logId, clientResponse);
                });
    }

    private Mono<Object> responseToString(String logId,
                                          String path,
                                          ClientResponse clientResponse) {
        return clientResponse.bodyToMono(String.class)
                .flatMap(response -> {
                    log.info("{}, Path: {}, Response Body: {}",
                            logId, path, response);
                    return Mono.just(response);
                });
    }

    private static void logErrorResponse(String logId,
                                         ClientResponse clientResponse,
                                         WebClientResponseException e) {
        log.error("{}, {}, Error Message: {}", logId, clientResponse.logPrefix(), e.getMessage());
    }

    private static Mono<Object> createException(String logId,
                                                ClientResponse clientResponse) {
        if (clientResponse.statusCode().is4xxClientError())
            return clientResponse.createException()
                    .flatMap(e -> {
                        logErrorResponse(logId, clientResponse, e);
                        logErrorBody(logId, clientResponse, e);
                        return Mono.error(new
                                KubeletClientServiceException(clientResponse.statusCode(), e.getResponseBodyAsString()));
                    });

        return clientResponse.createException()
                .flatMap(e -> {
                    logErrorResponse(logId, clientResponse, e);
                    e.getResponseBodyAsString();
                    if (!e.getResponseBodyAsString().isBlank())
                        logErrorBody(logId, clientResponse, e);
                    return Mono.error(new
                            KubeletClientServiceException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
                });
    }

    private static void logErrorBody(String logId,
                                     ClientResponse clientResponse,
                                     WebClientResponseException e) {
        log.error("{}, Status code: {}, Error response body: {}",
                logId, clientResponse.statusCode(), e.getResponseBodyAsString());
    }
}
