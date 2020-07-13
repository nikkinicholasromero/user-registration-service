package com.demo.controller;

import com.demo.model.StatusResponse;
import com.demo.service.EmailAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Email;

@RestController
@RequestMapping("/emailAddress")
@Validated
public class EmailAddressController {
    @Autowired
    private EmailAddressService emailAddressService;

    @GetMapping("/{emailAddress}")
    public ResponseEntity<String> getEmailAddressStatus(@PathVariable("emailAddress") @Email String emailAddress) {
        return new ResponseEntity<>(emailAddressService.getEmailAddressStatus(emailAddress).getMessage(), HttpStatus.OK);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<StatusResponse> handleConstraintViolationException(ConstraintViolationException e) {
        StatusResponse statusResponse = new StatusResponse();
        statusResponse.setCode("E1");
        statusResponse.setMessage("Invalid email address");
        return new ResponseEntity<>(statusResponse, HttpStatus.BAD_REQUEST);
    }
}
