package com.sayem.trackfit.gatewayserver.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sayem.trackfit.gatewayserver.dto.RegisterRequest;
import com.sayem.trackfit.gatewayserver.service.client.UserFeignClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
//@RequiredArgsConstructor
public class KeycloakUserSyncFilter implements WebFilter{
	
	@Autowired
	private UserFeignClient userFeignClient;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		
		log.info("Started executing KeycloakUserSyncFilter");

		
		String userId = exchange.getRequest().getHeaders().getFirst("X-User-ID");
		String accessToken = exchange.getRequest().getHeaders().getFirst("Authorization");
		
		RegisterRequest registerRequest = getuserDetails(accessToken);
		
		if(userId == null && accessToken != null) {
			userId = registerRequest.getKeycloakId();
		}
		
		if(userId != null && accessToken != null) {
			String finalUserId = userId;
			return userFeignClient.validateUserByKeycloakId(userId)
					.flatMap(exist -> {
						if(!exist) {
							// register user
							log.info("registerRequest: {}", registerRequest);
							if(registerRequest != null) {
								return userFeignClient.register(registerRequest).flatMap(userResponse -> {
									log.info("User is created, synched with keycloak: {}", finalUserId);
									return Mono.empty();
								});
							}
							return Mono.empty();
						} else {
							log.info("User already exist, so skipping sync: {}", finalUserId);
							return Mono.empty();
						}
					})
					.then(Mono.defer(() -> {
						ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
								.header("X-User-ID", finalUserId)
								.build();
						return chain.filter(exchange.mutate().request(mutatedRequest).build());
					}));
		}
		
		return chain.filter(exchange);
	}

	private RegisterRequest getuserDetails(String accessToken) {
		try {
			
			String tokenWithoutBearer = accessToken.replace("Bearer ", "").trim();
			SignedJWT signedJWT = SignedJWT.parse(tokenWithoutBearer);
			JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
			
			RegisterRequest registerRequest = new RegisterRequest();
			registerRequest.setEmail(claims.getStringClaim("email"));
			registerRequest.setKeycloakId(claims.getStringClaim("sub"));
			registerRequest.setPassword("divya@1999");
			registerRequest.setFirstName(claims.getStringClaim("given_name"));
			registerRequest.setLastName(claims.getStringClaim("family_name"));
			
			return registerRequest;
			
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	

}
