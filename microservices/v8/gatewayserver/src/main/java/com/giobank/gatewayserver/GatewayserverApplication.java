package com.giobank.gatewayserver;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import java.time.Duration;
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
								.addResponseHeader("Response-Time", LocalDateTime.now().toString())
								.circuitBreaker(config -> config.setName("accountsCircuitBreaker")
										.setFallbackUri("forward:/contact-support")))
						.uri("lb://ACCOUNTS")
				)
				.route(r -> r
						.path("/giobank/loans/**")
						.filters(f -> f.rewritePath(
								"/giobank/loans/?(?<remaining>.*)","/${remaining}")
								.addResponseHeader("Response-Time", LocalDateTime.now().toString())
								.retry(config -> config.setRetries(3)
										.setMethods(HttpMethod.GET)
										.setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true))
						)
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

	@Bean
	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCostumizer(){
		return factory -> factory.configureDefault(
				id -> new Resilience4JConfigBuilder(id)
						.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
						.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4))
								.build())
						.build());
	}
}
