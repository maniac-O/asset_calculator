package com.SH.asset_calculator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.management.BadAttributeValueExpException;
import java.io.IOException;
import java.sql.SQLException;

@RestControllerAdvice
public class ExceptionController {


    @ExceptionHandler(BadArgumentException.class)
    public ResponseEntity badArgument() {
        return new ResponseEntity("BAD", HttpStatus.PAYMENT_REQUIRED);
    }
}
