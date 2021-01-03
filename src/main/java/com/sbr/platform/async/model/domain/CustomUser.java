package com.sbr.platform.async.model.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomUser implements Serializable {


    private static final long serialVersionUID = -6335840745314196186L;

    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private String gender;

    private String ssn;

    private String phone;

    private String username;

    private String password;
}
