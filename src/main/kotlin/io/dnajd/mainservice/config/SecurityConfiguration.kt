package io.dnajd.mainservice.config

import io.dnajd.mainservice.infrastructure.Endpoints
import io.dnajd.mainservice.infrastructure.jwt.validators.JwtAudienceValidator
import io.dnajd.mainservice.infrastructure.jwt.validators.JwtAuthorizedPartyValidator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.jwt.*
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}") private val issuerUrl: String,
    @Value("\${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") private val jwkSetUrl: String,
    @Value("\${spring.security.oauth2.resourceserver.jwt.audiences}") private val audiences: List<String>,
    @Value("\${spring.security.oauth2.client.registration.google.client-id}") private val authorizedParty: String,
    private val userDetailsService: UserDetailsService,
) {
    private val jwtAccessTokenRequestFilter = JwtAccessTokenRequestFilter(userDetailsService)
    private val jwtRefreshTokenRequestFilter = JwtRefreshTokenRequestFilter(userDetailsService)

    @Bean
    @Order(1)
    fun oauthSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .cors { cors -> cors.disable() }
            .csrf { csrf -> csrf.disable() }
            .securityMatcher("${Endpoints.GOOGLE_AUTH}**" )
            .authorizeHttpRequests { auth ->
                auth.anyRequest().authenticated()
            }.oauth2ResourceServer { oauth2 ->
                oauth2.jwt { decoder() }
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .build()
    }


    /**
     * validate refresh tokens
     */
    @Bean
    @Order(2)
    fun jwtRefreshTokenSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .cors { cors -> cors.disable() }
            .csrf { csrf -> csrf.disable() }
            .securityMatcher("${Endpoints.JWT_REFRESH_AUTH}**" )
            .authorizeHttpRequests { auth ->
                auth.anyRequest().authenticated()
            }
            .addFilterBefore(jwtRefreshTokenRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    /**
     * validates access tokens
     */
    @Bean
    @Order(3)
    fun jwtAccessTokenSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .cors { cors -> cors.disable() }
            .csrf { csrf -> csrf.disable() }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("${Endpoints.GOOGLE_AUTH}**", "${Endpoints.JWT_REFRESH_AUTH}**").permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtAccessTokenRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }


    // prevent spring from automatically registering filters
    /*
    @Bean
    fun registration(): FilterRegistrationBean<*> {
        val registration: FilterRegistrationBean<*> = FilterRegistrationBean<OncePerRequestFilter>(jwtAccessTokenRequestFilter)
        registration.isEnabled = false

        return registration
    }
     */

    fun decoder(): JwtDecoder {
        val validators = mutableListOf<OAuth2TokenValidator<Jwt>>(
            JwtTimestampValidator(),
            JwtIssuerValidator(issuerUrl),
            JwtAudienceValidator(audiences),
            JwtAuthorizedPartyValidator(authorizedParty)
        )

        return NimbusJwtDecoder.withJwkSetUri(jwkSetUrl)
            .jwsAlgorithms { DelegatingOAuth2TokenValidator(validators) }
            .build()
    }
}