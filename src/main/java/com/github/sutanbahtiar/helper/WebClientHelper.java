package com.github.sutanbahtiar.helper;

/*
 * @created 08/06/2025 - 2:37 PM
 * @author sutan.bahtiar@brids.co.id
 */

import com.github.sutanbahtiar.constants.RequestHeaders;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Log4j2
public class WebClientHelper {

    private final WebClient webClient;
    private final String token;

    public WebClientHelper(WebClient webClient,
                           String token) {
        this.webClient = webClient;
        this.token = token;
    }

    private MultiValueMap<String, String> getHeader() {
        final MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add(RequestHeaders.AUTHORIZATION, RequestHeaders.BEARER + token);
        return header;
    }

    protected Mono<Object> get(String logId,
                               String path) {
        final MultiValueMap<String, String> header = getHeader();
        return webClient.get()
                .uri(path)
                .headers(httpHeaders -> httpHeaders.addAll(header))
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful())
                        return clientResponse.bodyToMono(String.class);

                    return createException(logId, clientResponse);
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
                                WebClientHelperException(clientResponse.statusCode(), e.getResponseBodyAsString()));
                    });

        return clientResponse.createException()
                .flatMap(e -> {
                    logErrorResponse(logId, clientResponse, e);
                    e.getResponseBodyAsString();
                    if (!e.getResponseBodyAsString().isBlank())
                        logErrorBody(logId, clientResponse, e);
                    return Mono.error(new
                            WebClientHelperException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
                });
    }

    private static void logErrorBody(String logId,
                                     ClientResponse clientResponse,
                                     WebClientResponseException e) {
        log.error("{}, Status code: {}, Error response body: {}",
                logId, clientResponse.statusCode(), e.getResponseBodyAsString());
    }
}
