package com.example.demo.utility;

import java.util.Arrays;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;

public class KeycloakScopeVerifier {

	public boolean hasScope(KeycloakAuthenticationToken principal, String scope) {
		return Arrays.stream(principal.getAccount()
									.getKeycloakSecurityContext()
									.getToken()
									.getScope()
									.split("\\s+"))
					.anyMatch(scope::equals);
	}
}
