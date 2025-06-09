package com.github.sutanbahtiar.helper;

/*
 * @author sutan.bahtiar@gmail.com
 */

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class WebClientHelperException extends ResponseStatusException {

    private static final long serialVersionUID = -3985580919098962862L;

    public WebClientHelperException(HttpStatus status) {
        super(status);
    }

    public WebClientHelperException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public WebClientHelperException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public WebClientHelperException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }
}
