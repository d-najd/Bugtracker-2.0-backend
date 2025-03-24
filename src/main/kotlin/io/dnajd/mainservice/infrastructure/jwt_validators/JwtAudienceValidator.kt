package io.dnajd.mainservice.infrastructure.jwt_validators

import io.jsonwebtoken.lang.Assert
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult
import org.springframework.security.oauth2.jwt.Jwt

/**
 * Validates whether all the given audiences are specified inside the token
 */
class JwtAudienceValidator(private val audiences: Collection<String>) : OAuth2TokenValidator<Jwt> {
    init {
        if (audiences.isEmpty()) {
            throw IllegalArgumentException("Specify at-least one audience")
        }
    }

    override fun validate(token: Jwt?): OAuth2TokenValidatorResult {
        Assert.notNull(token, "Token can't be null")

        return if (token!!.audience.containsAll(audiences)) {
            OAuth2TokenValidatorResult.success()
        } else {
            OAuth2TokenValidatorResult.failure(OAuth2Error("Failed to validate all audiences"))
        }
    }
}
