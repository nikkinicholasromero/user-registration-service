package com.demo.controller;

import com.demo.model.UserAccount;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/registration")
public class RegistrationController {
    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public void registerUserAccount(@RequestBody @Valid UserAccount userAccount) {

    }
}


