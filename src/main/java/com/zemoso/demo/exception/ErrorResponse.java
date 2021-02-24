package com.zemoso.demo.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Setter
@Getter
@ToString
public class ErrorResponse {
    private int status;
    private String message;
    private ZonedDateTime timestamp;

    public ErrorResponse(){
        timestamp = ZonedDateTime.now();
    }

    public ErrorResponse(int status, String message){
        this.status = status;
        this.message = message;
        timestamp = ZonedDateTime.now();
    }

    public ErrorResponse(int status){
        this.status = status;
        timestamp = ZonedDateTime.now();
        message = "";
    }
}
