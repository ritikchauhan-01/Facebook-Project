package com.infy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

	@Autowired
	AuthenticationFilter authInHeaderFilter;

	@Autowired
	AuthenticationParamFilter authInParamFilter;

	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("Facebook-PostMS",
						r -> r.path("/facebook/post-api/**").filters(f -> f.filter(authInHeaderFilter))
								.uri("lb://Facebook-PostMS"))
				.route("Facebook-UserMS",
						r -> r.path("/facebook/user-api/**").filters(f -> f.filter(authInHeaderFilter))
								.uri("lb://Facebook-UserMS"))
				.route("Facebook-FriendMS",
						r -> r.path("/facebook/friend-api/**").filters(f -> f.filter(authInHeaderFilter))
								.uri("lb://Facebook-FriendMS"))
				.route("Facebook-NotificationMS-Websocket",
						r -> r.path("/facebook/notification-api/received/**").filters(f -> f.filter(authInParamFilter))
								.uri("lb:ws://Facebook-NotificationMS"))
				.route("Facebook-NotificationMS", r -> r.path("/facebook/notification-api/**")
						.filters(f -> f.filter(authInHeaderFilter)).uri("lb://Facebook-NotificationMS"))
				.build();
	}
}
