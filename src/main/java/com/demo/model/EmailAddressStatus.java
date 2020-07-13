package com.demo.model;

import lombok.Getter;

@Getter
public enum EmailAddressStatus {
    NOT_REGISTERED("Not Registered"),
    REGISTERED("Registered"),
    ACTIVATED("Activated"),
    DEACTIVATED("Deactivated");

    private final String message;

    EmailAddressStatus(String message) {
        this.message = message;
    }
}
