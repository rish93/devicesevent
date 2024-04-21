package com.health.devicesevent.exception.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorMessage {

    private long timestamp;
    private String error;
    private int status;
    private String exception;
    private String message;
}
