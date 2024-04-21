package com.health.devicesevent.exception;

public class TenantNotFoundException extends Exception{
    public TenantNotFoundException() {}


    public TenantNotFoundException(String message)
    {
        super(message);
    }

}
