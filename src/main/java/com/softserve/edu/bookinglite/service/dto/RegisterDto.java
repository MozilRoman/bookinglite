package com.softserve.edu.bookinglite.service.dto;

import com.softserve.edu.bookinglite.entity.Address;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

// TODO: REFACTOR add validation
public class RegisterDto {
    @NotBlank
    @Email
    private String email;

    private String first_name;

    private String last_name;

    private String password;

    private String phone_number;

    private boolean owner;

    private Address address;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
