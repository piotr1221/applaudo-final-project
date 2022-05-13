package com.example.demo.exception.responsestatus;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundStatusException extends ResponseStatusException {

	private static final long serialVersionUID = 1L;
	
	public NotFoundStatusException(String reason) {
		super(HttpStatus.NOT_FOUND, reason);
	}
}
