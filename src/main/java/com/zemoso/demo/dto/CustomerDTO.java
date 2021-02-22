package com.zemoso.demo.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CustomerDTO {
    private int id;

    @NotBlank(message = "First name should not be null")
    private String firstName;

    @NotBlank(message = "Last name should not be null")
    private String lastName;

    @Email(message = "Invalid email")
    private String email;

    public CustomerDTO(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
