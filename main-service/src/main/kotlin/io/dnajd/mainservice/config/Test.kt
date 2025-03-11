package io.dnajd.mainservice.config

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jwt.SignedJWT
import java.net.URL
import java.security.interfaces.RSAKey
import java.util.*
import java.util.concurrent.TimeUnit

val jwkSetCache = mutableMapOf<String, JWKSet>()
fun getCachedJWKSet(): JWKSet {
    val jwks = JWKSet.load(URL("https://www.googleapis.com/oauth2/v3/certs"))
    jwkSetCache["keys"] = jwks

    return jwkSetCache["keys"] as JWKSet
}

fun validateGoogleJwt(jwt: String, clientId: String): Boolean {
    return try {
        val signedJWT = SignedJWT.parse(jwt)
        val jwkSet = getCachedJWKSet()
        val jwk = jwkSet.getKeyByKeyId(signedJWT.header.keyID)
        val publicKey = jwk.toRSAKey().toRSAPublicKey()
        val verifier = RSASSAVerifier(publicKey)

        val isSignatureValid = signedJWT.verify(verifier)
        val claims = signedJWT.jwtClaimsSet

        isSignatureValid &&
                claims.audience.contains(clientId) &&
                claims.expirationTime.after(Date()) &&
                claims.issuer == "https://accounts.google.com"
    } catch (e: Exception) {
        false
    }
}

/*
@Bean
fun jwtDecoder(): JwtDecoder {
    return JwtDecoders.fromIssuerLocation("https://accounts.google.com")
}

class Auth0TokenVerifier(clientId: String) {
    private val jwkProvider = JwkProviderBuilder(URL("https://www.googleapis.com/oauth2/v3/certs"))
        .cached(10, 24, TimeUnit.HOURS) // Cache keys for 24 hours
        .build()

    fun validate(token: String): Boolean {
        return try {
            val jwt = JWT.decode(token)
            val jwk = jwkProvider.get(jwt.keyId)
            val algorithm = Algorithm.RSA256(jwk.publicKey as java.security.interfaces.RSAPublicKey, null)
            algorithm.verify(jwt)
            jwt.audience.contains(clientId) && !jwt.isExpired
        } catch (e: Exception) {
            false
        }
    }
}
 */

    /*
    private JWTVerifier buildJWTVerifier() throws CertificateException {
        var algo = Algorithm.RSA256(getRSAPublicKey(), null);
        return JWT.require(algo).build();
    }
     */

/*
fun buildJWTVerifier(): JWTVerifier {
    var algo = Algorithm.RSA256(RSAKey {  })

    return JWT.require(algo).build()
}
*/