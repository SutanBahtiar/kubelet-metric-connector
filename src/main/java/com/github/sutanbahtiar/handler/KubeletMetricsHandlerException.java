package com.github.sutanbahtiar.handler;

/*
 * @author sutan.bahtiar@gmail.com
 */

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class KubeletMetricsHandlerException extends ResponseStatusException {
    private static final long serialVersionUID = 2140767556529698743L;

    public KubeletMetricsHandlerException(HttpStatus status) {
        super(status);
    }

    public KubeletMetricsHandlerException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public KubeletMetricsHandlerException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public KubeletMetricsHandlerException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }
}
