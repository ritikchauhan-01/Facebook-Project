package com.infy.config;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RouterValidator {

	public static final List<String> openApiEndpoints = List.of(
			"/facebook/user-api/user/register",
			"/facebook/user-api/user/login",
			"/facebook/user-api/user/avatar/",
			"/facebook/post-api/post/postImage/"
			);

	public Predicate<ServerHttpRequest> isSecured = request -> openApiEndpoints.stream()
			.noneMatch(uri -> request.getURI().getPath().contains(uri));
}
