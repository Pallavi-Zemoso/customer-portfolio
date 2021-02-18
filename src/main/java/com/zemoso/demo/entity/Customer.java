package com.zemoso.demo.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "customer")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @NotBlank(message = "First name should not be null")
    @Column(name="first_name")
    private String firstName;

    @NotBlank(message = "Last name should not be null")
    @Column(name="last_name")
    private String lastName;

    @Email(message = "Invalid email")
    @Column(name="email")
    private String email;

    public Customer(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
