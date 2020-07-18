package com.demo.exception;

public class EmailAddressIsDueForActivationException extends RuntimeException {
    public final String code = "email-address.activation-due";
}
