package com.cash_online.Code_Challenge.models;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "userId")
    private List<Loan> loans;

    public User(String email, String firstName, String lastName) {
        setEmail(email);
        setFirstName(firstName);
        setLastName(lastName);
    }
}
