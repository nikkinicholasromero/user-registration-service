package com.demo.exception;

public class EmailAddressIsAlreadyTakenException extends RuntimeException {
    public final String code = "email-address.already-taken";
}
