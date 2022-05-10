package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ForbiddenStatusException extends ResponseStatusException {

	public ForbiddenStatusException(HttpStatus status, String reason) {
		super(status, reason);
	}

	private static final long serialVersionUID = 1L;

}
