package com.minderaschool.UserDataBaseComplete.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDtoGetOneUser {
    private int id;
    private String username;
    private String email;
    private String password;
}
