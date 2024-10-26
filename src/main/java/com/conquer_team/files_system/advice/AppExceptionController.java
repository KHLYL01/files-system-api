package com.conquer_team.files_system.advice;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@ControllerAdvice
public class AppExceptionController {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ExceptionDto> methodArgumentNotValidHandle(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toList();

        ExceptionDto exceptionResponse = ExceptionDto.builder()
                .timestamp(new Date())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(errors.toString())
                .build();

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<ExceptionDto> runtimeException(RuntimeException ex) {
        ExceptionDto exceptionResponse = ExceptionDto.builder()
                .timestamp(new Date())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getLocalizedMessage())
                .build();

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<ExceptionDto> sqlIntegrityConstraintViolationHandle(SQLIntegrityConstraintViolationException ex) {

        ExceptionDto exceptionResponse = ExceptionDto.builder()
                .timestamp(new Date())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getLocalizedMessage())
                .build();

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NoSuchElementException.class})
    public ResponseEntity<ExceptionDto> noSuchElementHandle(NoSuchElementException ex) {

        ExceptionDto exceptionResponse = ExceptionDto.builder()
                .timestamp(new Date())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getLocalizedMessage())
                .build();

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<ExceptionDto> illegalArgumentHandle(IllegalArgumentException ex) {

        ExceptionDto exceptionResponse = ExceptionDto.builder()
                .timestamp(new Date())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getLocalizedMessage())
                .build();

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<ExceptionDto> accessDeniedException(AccessDeniedException ex) {

        ExceptionDto exceptionResponse = ExceptionDto.builder()
                .timestamp(new Date())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getLocalizedMessage())
                .build();

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

}
