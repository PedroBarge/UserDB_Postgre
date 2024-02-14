package com.minderaschool.UserDataBaseComplete.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserMissArgsException extends RuntimeException {
    public UserMissArgsException() {
        super("User not complete");
    }
}
