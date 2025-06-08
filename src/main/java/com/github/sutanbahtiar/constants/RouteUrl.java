package com.github.sutanbahtiar.constants;

/*
 * @author sutan.bahtiar@gmail.com
 */

public class RouteUrl {
    private RouteUrl() {
        throw new IllegalStateException();
    }

    public static final String BASE_URL = "/kubelet-metrics";
    public static final String METRICS_URL = "/metrics";
    public static final String METRICS_CADVISOR_URL = "/metrics/cadvisor";
    public static final String METRICS_RESOURCE_URL = "/metrics/resource";

}
