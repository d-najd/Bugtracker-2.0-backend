package io.dnajd.mainservice.infrastructure.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import java.security.Key
import java.util.*

object JwtUtil {
    // H512 Base64 encoded key
    private const val secret =
        "KUeiSYMzwk/I7E2p8SBSmhSnBVfP56iimMGM6J5yJLAdAtf3sN4MmC7a7fxMnXU2ahFG1Aaq+38tuwOANLbVXw=="
    private const val accessExpirationMillis = 24 * 60 * 60 * 1000L // 1 day
    private const val refreshExpirationMillis = 180 * 24 * 60 * 60 * 1000L // 180 days
    private const val issuer = "d-najd.bugtracker"

    fun validateAccessToken(token: String, userDetails: UserDetails): Boolean {
        return (validateToken(token, userDetails) && getTokenType(token) == "access")
    }

    fun validateRefreshToken(token: String, userDetails: UserDetails): Boolean {
        return (validateToken(token, userDetails) && getTokenType(token) == "refresh")
    }

    fun generateAccessToken(userDetails: UserDetails): String {
        val claims: Map<String, Any> = mapOf(Pair("token_type", "access"))
        return doGenerateToken(userDetails.username, accessExpirationMillis, claims)
    }

    fun generateRefreshToken(userDetails: UserDetails): String {
        val claims: Map<String, Any> = mapOf(Pair("token_type", "refresh"))
        return doGenerateToken(userDetails.username, refreshExpirationMillis, claims)
    }

    /*
    fun canTokenBeRefreshed(token: String): Boolean {
        return (!isTokenExpired(token) || ignoreTokenExpiration(token))
    }
     */

    private fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = getUsernameFromToken(token)
        return (username == userDetails.username && !isTokenExpired(token))
    }

    private fun getSigningKey(): Key {
        val keyBytes = Decoders.BASE64.decode(this.secret)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    private fun doGenerateToken(subject: String, expirationMillis: Long, claims: Map<String, Any> = emptyMap(), headerParams: Map<String, Any> = emptyMap()): String {
        return Jwts
            .builder()
            .setIssuer(issuer)
            .setClaims(claims)
            .setHeaderParams(headerParams)
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expirationMillis))
            .signWith(getSigningKey())
            .compact()
    }

    private fun getUsernameFromToken(token: String): String {
        return getClaimFromToken(token) { it.subject }
    }

    private fun getTokenType(token: String): String {
        return getClaimFromToken(token) { it["token_type"] }.toString()
    }

    private fun getExpirationDateFromToken(token: String): Date {
        return getClaimFromToken(token) { it.expiration }
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration.before(Date())
    }

    private fun ignoreTokenExpiration(token: String): Boolean {
        // here you specify tokens, for that the expiration is ignored
        return false
    }

    private fun <T> getClaimFromToken(token: String, claimsResolver: (Claims) -> T): T {
        val claims = getAllClaimsFromToken(token)
        return claimsResolver(claims)
    }

    private fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJwt(token)
            .body
    }
}