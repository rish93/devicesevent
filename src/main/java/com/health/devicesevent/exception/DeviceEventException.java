package com.health.devicesevent.exception;

import com.health.devicesevent.exception.model.ErrorMessage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DeviceEventException{

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<ErrorMessage> resourceNotFoundException(ResourceNotFoundException ex ) {
       ErrorMessage errorMessage= ErrorMessage.builder()
               .message("Not found")
               .error(ex.getMessage())
               .status(404)
               .exception(ex.toString())
               .timestamp(System.currentTimeMillis() / 1000)
               .build();

        return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.NOT_FOUND);
    }
}
