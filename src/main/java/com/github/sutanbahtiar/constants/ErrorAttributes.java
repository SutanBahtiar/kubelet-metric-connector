package com.github.sutanbahtiar.constants;

/*
 * @author sutan.bahtiar@gmail.com
 */

import lombok.Getter;

@Getter
public enum ErrorAttributes {
    CODE("code"),
    MESSAGE("message"),
    TIME("timestamp");

    private final String key;

    ErrorAttributes(String key) {
        this.key = key;
    }
}
