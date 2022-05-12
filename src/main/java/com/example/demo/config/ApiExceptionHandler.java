package com.example.demo.config;

import java.util.NoSuchElementException;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(value = {ResponseStatusException.class})
	public final ResponseEntity<Object> handleResponseStatusException(final ResponseStatusException e, final WebRequest request) {
		return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), e.getStatus(), request);
	}
	
	@ExceptionHandler(value = {ConstraintViolationException.class})
	public final ResponseEntity<Object> handleConstraintViolationException(final ConstraintViolationException e, final WebRequest request){
		return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler(value = {EntityNotFoundException.class, NoSuchElementException.class})
	public final ResponseEntity<Object> handleNotFound(final Exception e, final WebRequest request){
		return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
}
