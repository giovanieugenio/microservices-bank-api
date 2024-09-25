package com.giobank.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	@Bean
	public RouteLocator giobankRoute(RouteLocatorBuilder locatorBuilder){
		return locatorBuilder.routes()
				.route(r -> r
						.path("/giobank/accounts/**")
						.filters(f -> f.rewritePath(
								"/giobank/accounts/?(?<remaining>.*)","/${remaining}")
								.addResponseHeader("Response-Time", LocalDateTime.now().toString()))
						.uri("lb://ACCOUNTS")
				)
				.route(r -> r
						.path("/giobank/loans/**")
						.filters(f -> f.rewritePath(
								"/giobank/loans/?(?<remaining>.*)","/${remaining}")
								.addResponseHeader("Response-Time", LocalDateTime.now().toString()))
						.uri("lb://LOANS")
				)
				.route(r -> r
						.path("/giobank/cards/**")
						.filters(f -> f.rewritePath(
								"/giobank/cards/?(?<remaining>.*)","/${remaining}")
								.addResponseHeader("Response-Time", LocalDateTime.now().toString()))
						.uri("lb://CARDS")
				)
				.build();
	}
}
