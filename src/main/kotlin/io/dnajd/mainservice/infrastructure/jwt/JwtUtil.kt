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
    const val expirationMs = 3600

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = getUsernameFromToken(token)
        return (username == userDetails.username && !isTokenExpired(token))
    }

    fun generateToken(userDetails: UserDetails, claims: Map<String, Any> = hashMapOf()): String {
        return doGenerateToken(claims, userDetails.username)
    }

    fun canTokenBeRefreshed(token: String): Boolean {
        return (!isTokenExpired(token) || ignoreTokenExpiration(token))
    }

    private fun getSigningKey(): Key {
        val keyBytes = Decoders.BASE64.decode(this.secret)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    private fun doGenerateToken(claims: Map<String, Any?>, subject: String): String {
        return Jwts
            .builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expirationMs * 1000))
            .signWith(getSigningKey())
            .compact()
    }

    private fun getUsernameFromToken(token: String): String {
        return getClaimFromToken(token) { it.subject }
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