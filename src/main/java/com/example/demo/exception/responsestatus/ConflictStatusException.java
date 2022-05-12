package com.example.demo.exception.responsestatus;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ConflictStatusException extends ResponseStatusException {

	private static final long serialVersionUID = 1L;

	public ConflictStatusException(String reason) {
		super(HttpStatus.CONFLICT, reason);
	}
}
