package com.example.demo.exception.responsestatus;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ForbiddenStatusException extends ResponseStatusException {

	public ForbiddenStatusException(String reason) {
		super(HttpStatus.FORBIDDEN, reason);
	}

	private static final long serialVersionUID = 1L;

}
