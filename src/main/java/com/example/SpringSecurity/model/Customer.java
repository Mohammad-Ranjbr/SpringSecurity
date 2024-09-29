package com.example.SpringSecurity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Entity
@Setter
@Getter
@Table(name = "customer")
public class Customer {

    @Id
    @Column(name = "customer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String email;

    @Column(name = "mobile_number")
    private String mobileNumber;

    // This annotation is used when we want a field to be writable only when receiving data (deserialization),
    // but not visible in the JSON output when sending data (serialization). In other words,
    // this field can be received in requests, but it should not be displayed in responses.
    // When JSON data is deserialized to a User object (such as an HTTP POST request for registration),
    // the password value is read from the JSON and assigned to the corresponding field.
    // When a User object is serialized to JSON (such as an HTTP GET response to display user information),
    // the password field is not included in the JSON output and sent to the client.

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String pwd;

    private String role;

    @JsonIgnore
    @Column(name = "create_dt")
    private Date createDt;

}
