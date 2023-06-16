package com.ccsw.tutorial.common.ownException;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class PrestamoControllerExceptionHandler {

    @ExceptionHandler(value = { ErrorNuevoPrestamoException.class })
    public ResponseEntity<ErrorMessage> resourceNotFoundException(ErrorNuevoPrestamoException ex, WebRequest request) {

        ErrorMessage message = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date(), ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<ErrorMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
