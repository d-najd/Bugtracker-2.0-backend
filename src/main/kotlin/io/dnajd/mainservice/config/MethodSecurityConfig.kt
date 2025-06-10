package io.dnajd.mainservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
class MethodSecurityConfig(
    val permissionEvaluator: PermissionEvaluator
){
    @Bean
    fun methodSecurityExpressionHandler(): MethodSecurityExpressionHandler {
        val expressionHandler =
            DefaultMethodSecurityExpressionHandler()
        expressionHandler.setPermissionEvaluator(permissionEvaluator)

        return expressionHandler
    }
}