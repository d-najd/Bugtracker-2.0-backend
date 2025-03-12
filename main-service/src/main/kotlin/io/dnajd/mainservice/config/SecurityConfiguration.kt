package io.dnajd.mainservice.config

import io.dnajd.mainservice.infrastructure.JwtAudienceValidator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.jwt.*
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}") private val issuerUrl: String,
    @Value("\${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") private val jwkSetUrl: String,
    @Value("\${spring.security.oauth2.resourceserver.jwt.audiences}") private val audiences: List<String>
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.cors { cors -> cors.disable() }
            .authorizeHttpRequests { auth ->
                auth.anyRequest().authenticated()
            }.oauth2ResourceServer { oauth2 -> oauth2.jwt { decoder() } }.build()
    }

    fun decoder(): JwtDecoder {
        val validators = mutableListOf<OAuth2TokenValidator<Jwt>>()
        validators.add(JwtTimestampValidator())
        validators.add(JwtIssuerValidator(issuerUrl))
        // validators.add(JwtAudienceValidator(audiences))

        val decoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUrl)
        decoder.jwsAlgorithms { DelegatingOAuth2TokenValidator(validators) }
        return decoder.build()
    }
}