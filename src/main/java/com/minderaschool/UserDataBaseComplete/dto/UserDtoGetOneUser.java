package com.minderaschool.UserDataBaseComplete.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoGetOneUser {
    private int id;
    private String username;
    private String email;
    private String password;
}
