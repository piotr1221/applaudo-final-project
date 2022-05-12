package com.example.demo.exception.responsestatus;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BadRequestStatusException extends ResponseStatusException {

	private static final long serialVersionUID = 1L;

	public BadRequestStatusException(String reason) {
		super(HttpStatus.BAD_REQUEST, reason);
	}
}
