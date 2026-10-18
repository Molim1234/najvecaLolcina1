package com.example.najvecaLolcina.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class ShippingAddressDTO {

    @NotBlank(message = "First name is mandatory")
    @Size(max = 30)
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(max = 30)
    private String lastName;

    @NotBlank(message = "Phone number is mandatory")
    @Size(max = 20)
    private String phoneNumber;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Size(max = 40)
    private String email;

    @Size(max = 20)
    @NotBlank(message = "City is mandatory")
    private String city;
    @Size(max = 50)
    private String note;

    public ShippingAddressDTO(String firstName, String lastName, String phoneNumber, String email, String city, String note) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.city = city;
        this.note = note;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}