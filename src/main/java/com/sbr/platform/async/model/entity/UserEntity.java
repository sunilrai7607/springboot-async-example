package com.sbr.platform.async.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document(collection = "USER")
public class UserEntity implements Serializable {


    private static final long serialVersionUID = -6335840745314196186L;

    @Id
    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private String gender;

    private String ssn;

    private String phone;

    private String username;

    private String password;

    private AddressEntity address;
}
