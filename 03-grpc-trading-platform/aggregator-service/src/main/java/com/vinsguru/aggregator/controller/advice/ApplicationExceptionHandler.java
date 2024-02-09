package com.vinsguru.aggregator.controller.advice;

import io.grpc.StatusRuntimeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(StatusRuntimeException.class)
    public ResponseEntity<String> handleStatusRuntimeException(StatusRuntimeException e){
        return switch (e.getStatus().getCode()){
            case INVALID_ARGUMENT, FAILED_PRECONDITION -> ResponseEntity.badRequest().body(e.getStatus().getDescription());
            case NOT_FOUND -> ResponseEntity.notFound().build();
            case null, default -> ResponseEntity.internalServerError().body(e.getMessage());
        };
    }

}
