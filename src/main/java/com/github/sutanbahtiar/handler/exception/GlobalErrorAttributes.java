package com.github.sutanbahtiar.handler.exception;

import com.github.sutanbahtiar.constants.ErrorAttributes;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/*
 * @author sutan.bahtiar@gmail.com
 */

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request,
                                                  ErrorAttributeOptions options) {
        Throwable error = getError(request);

        final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);

        Map<String, Object> map = new HashMap<>();
        map.put(ErrorAttributes.CODE.getKey(), determineHttpStatus(error).value());
        map.put(ErrorAttributes.MESSAGE.getKey(), determineMessage(error));
        map.put(ErrorAttributes.TIME.getKey(), timestamp);
        return map;
    }

    private String determineMessage(Throwable error) {
        if (error instanceof ResponseStatusException) {
            ResponseStatusException err = (ResponseStatusException) error;
            return null == err.getReason() ? err.getMessage() : err.getReason();
        }

        return error.getMessage();
    }

    private HttpStatus determineHttpStatus(Throwable error) {
        if (error instanceof ResponseStatusException) {
            ResponseStatusException err = (ResponseStatusException) error;
            return err.getStatus();
        }

        return MergedAnnotations
                .from(error.getClass(), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY)
                .get(ResponseStatus.class)
                .getValue(ErrorAttributes.CODE.getKey(), HttpStatus.class)
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
