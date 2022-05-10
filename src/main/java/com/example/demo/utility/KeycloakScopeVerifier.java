package com.example.demo.utility;

import java.util.Arrays;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;

import com.example.demo.exception.ForbiddenStatusException;

public class KeycloakScopeVerifier {

	public void hasScope(KeycloakAuthenticationToken principal, String scope) throws ForbiddenStatusException {
		boolean hasScope = Arrays.stream(principal.getAccount()
													.getKeycloakSecurityContext()
													.getToken()
													.getScope()
													.split("\\s+"))
								.anyMatch(scope::equals);
		if (!hasScope) throw new ForbiddenStatusException("Scope " + scope + " required");
	}
}
