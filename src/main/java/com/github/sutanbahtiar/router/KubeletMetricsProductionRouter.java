package com.github.sutanbahtiar.router;

/*
 * @author sutan.bahtiar@gmail.com
 */

import com.github.sutanbahtiar.constants.RouteUrl;
import com.github.sutanbahtiar.handler.KubeletMetricProductionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class KubeletMetricsProductionRouter {
    private final KubeletMetricProductionHandler handler;

    @Bean
    public RouterFunction<ServerResponse> routerProductionFunction() {
        return RouterFunctions.route()
                .path(RouteUrl.BASE_URL_PRODUCTION, getRoute())
                .build();
    }

    private Consumer<RouterFunctions.Builder> getRoute() {
        return builder -> builder
                .GET(RouteUrl.METRICS_URL, handler::getMetrics)
                .GET(RouteUrl.METRICS_CADVISOR_URL, handler::getMetricsCadvisor)
                .GET(RouteUrl.METRICS_RESOURCE_URL, handler::getMetricsResource);
    }
}
