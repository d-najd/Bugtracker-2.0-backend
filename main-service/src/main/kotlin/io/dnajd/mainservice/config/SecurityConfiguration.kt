package io.dnajd.mainservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.WebSecurityConfigurer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
// @EnableMethodSecurity
public class SecurityConfiguration {
    companion object {
        public const val clientSecret = "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"
        public const val clientId = "523144607813-ccib1llvilpg1e6httmo9a0d839bhh9h.apps.googleusercontent.com"
        public const val testClientId = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjkxNGZiOWIwODcxODBiYzAzMDMyODQ1MDBjNWY1NDBjNmQ0ZjVlMmYiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI1MjMxNDQ2MDc4MTMtZm5uZzNwbWFpNDQ2anQwYWpjMDEyYWF2NmtlZG84dTQuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI1MjMxNDQ2MDc4MTMtY2NpYjFsbHZpbHBnMWU2aHR0bW85YTBkODM5YmhoOWguYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTUxMTAwNTQ4MzAzNjc5MDc0NjUiLCJlbWFpbCI6ImhmaGpkc2Jic2hzQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoiSGZoamQgU2Jic2hzIiwicGljdHVyZSI6Imh0dHBzOi8vbGgzLmdvb2dsZXVzZXJjb250ZW50LmNvbS9hL0FDZzhvY0l5ZnZLWVMyM0toeEcweU5Kell5dTRUZGl4Vm02S012OGprN2lRdjVIR1BKeXNvQT1zOTYtYyIsImdpdmVuX25hbWUiOiJIZmhqZCIsImZhbWlseV9uYW1lIjoiU2Jic2hzIiwiaWF0IjoxNzQxNzA5MDY0LCJleHAiOjE3NDE3MTI2NjR9.AtG7hjTVryMbhJiQV9LNsIEY8cAqf-CGbeYT2wiV9NH6ed7fxyC9hs4RXhrg-Awrs2pjVOUTFtgDBsFB_JzJlEU7fl7XKCFxxWg9woKHFxRlgFiO76t3YwJy8dv8jJpiwekJA4ISFn7oKCR8G-nxzlvHMynFk3dSWIP9SRora8gla74Ts9ikIRysi323j6-v4Iv8OUjiTGDaMgUXDwoaKaLjr4j5-bkbo4hck9R9acgJ649otlhJ1lPSlVZNsghfWIaT6T7NvXm_Hz19QtaiBhUzupyPz6_Bho8q2o7xe325uHtkRUDZBYiGW6Mp4xAcJVy2q9wfA-rM5yIt-zNjmw"
    }

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.csrf { it.disable() }
            .authorizeRequests()
            .requestMatchers("/**")
            // .antMatchers("/authenticate", "/register")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .build()
    }

}
