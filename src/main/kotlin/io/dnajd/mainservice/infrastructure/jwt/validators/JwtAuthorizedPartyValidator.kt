package io.dnajd.mainservice.infrastructure.jwt.validators

import io.jsonwebtoken.lang.Assert
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult
import org.springframework.security.oauth2.jwt.Jwt

/**
 * Validates whether the given authorized party is specified inside the token
 */
class JwtAuthorizedPartyValidator(private val authorizedParty: String) : OAuth2TokenValidator<Jwt> {
    override fun validate(token: Jwt?): OAuth2TokenValidatorResult {
        Assert.notNull(token, "Token can't be null")

        return if (token!!.getClaimAsString("azp").equals(authorizedParty)) {
            OAuth2TokenValidatorResult.success()
        } else {
            OAuth2TokenValidatorResult.failure(OAuth2Error("Invalid azp field"))
        }
    }
}
