package com.sayem.trackfit.gatewayserver.service.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sayem.trackfit.gatewayserver.dto.RegisterRequest;
import com.sayem.trackfit.gatewayserver.dto.UserResponse;

import jakarta.validation.Valid;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "USER-SERVICE", url = "http://localhost:8080")
public interface UserFeignClient {
	
	@GetMapping("/api/users/validateByKeycloakId/{keycloakId}")
	public Mono<Boolean> validateUserByKeycloakId(@PathVariable String keycloakId);
	
	@PostMapping("/api/users/register")
	public Mono<UserResponse> register(@Valid @RequestBody RegisterRequest request);
	
}
