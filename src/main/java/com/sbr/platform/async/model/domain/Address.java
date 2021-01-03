package com.sbr.platform.async.model.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(of = {"id"})
public class Address implements Serializable {


    private static final long serialVersionUID = -7907072363155980020L;

    private String id;

    private String streetAddress;

    private String city;

    private String state;

    private String zip;

    private String timeZone;

    public Address find(String id){
        return this;
    }
}
