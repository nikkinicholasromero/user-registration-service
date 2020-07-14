package com.demo.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "USER_ACCOUNTS")
public class UserAccount {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Email(message = "validation.email-address.format")
    @NotBlank(message = "validation.email-address.required")
    @Column(name = "EMAIL_ADDRESS")
    private String emailAddress;

    @NotBlank(message = "validation.password.required")
    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private EmailAddressStatus status;

    @NotBlank(message = "validation.first-name.required")
    @Column(name = "FIRST_NAME")
    private String firstName;

    @NotBlank(message = "validation.last-name.required")
    @Column(name = "LAST_NAME")
    private String lastName;
}
