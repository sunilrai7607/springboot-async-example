package com.sbr.platform.async.model.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document(collection = "USER-ADDRESS")
public class AddressEntity implements Serializable {


    private static final long serialVersionUID = -7907072363155980020L;

    private String streetAddress;

    private String city;

    private String state;

    private String zip;

    private String timeZone;
}
