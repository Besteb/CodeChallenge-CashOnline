package com.cash_online.Code_Challenge.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserRequest {

    private final String email;
    private final String firstName;
    private final String lastName;
}
