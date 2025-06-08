package com.github.sutanbahtiar.service;

/*
 * @author sutan.bahtiar@gmail.com
 */

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class KubeletClientServiceException extends ResponseStatusException {

    private static final long serialVersionUID = -3985580919098962862L;

    public KubeletClientServiceException(HttpStatus status) {
        super(status);
    }

    public KubeletClientServiceException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public KubeletClientServiceException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public KubeletClientServiceException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }
}
