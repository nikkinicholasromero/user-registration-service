package com.demo.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "USER_ACCOUNTS")
public class UserAccount {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "EMAIL_ADDRESS")
    private String emailAddress;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private EmailAddressStatus status;
}
