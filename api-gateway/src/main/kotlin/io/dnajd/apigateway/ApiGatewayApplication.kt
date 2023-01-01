package io.dnajd.apigateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory
import org.springframework.cloud.client.circuitbreaker.Customizer
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient

@SpringBootApplication
@EnableDiscoveryClient
class ApiGatewayApplication

fun main(args: Array<String>) {
	runApplication<ApiGatewayApplication>(*args)


	@Bean
	@LoadBalanced
	fun loadBalancedRestTemplate(): RestTemplate {
		return RestTemplate()
	}

	@Bean
	@LoadBalanced
	fun loadBalancedWebClientBuilder(): WebClient.Builder {
		return WebClient.builder()
	}

	@Bean
	fun instanceSupplier(context: ConfigurableApplicationContext): ServiceInstanceListSupplier {
		return ServiceInstanceListSupplier.builder()
			.withDiscoveryClient()
			.withHealthChecks()
			.build(context);
	}
}
