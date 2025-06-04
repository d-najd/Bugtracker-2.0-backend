package io.dnajd.mainservice.infrastructure.jwt

import io.dnajd.mainservice.domain.token.JwtUserTokenHolder
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import java.security.Key
import java.util.*

object JwtUtil {
    // H512 Base64 encoded key
    private const val secret =
        "KUeiSYMzwk/I7E2p8SBSmhSnBVfP56iimMGM6J5yJLAdAtf3sN4MmC7a7fxMnXU2ahFG1Aaq+38tuwOANLbVXw=="
    private const val accessExpirationMillis = 24 * 60 * 60 * 1000L // 1 day
    private const val refreshExpirationMillis = 30 * 24 * 60 * 60 * 1000L // 30 days
    private const val refreshMaxExpirationMillis = 365 * 24 * 60 * 60 * 1000L // 365 days
    private const val issuer = "d-najd.bugtracker.backend"
    private const val audience = "d-najd.bugtracker.android"

    private const val firstIssueDateField = "first_issue_date"

    private const val tokenType = "token_type"
    private const val tokenTypeAccess = "access"
    private const val tokenTypeRefresh = "access"

    fun generateUserTokens(username: String): JwtUserTokenHolder {
        return JwtUserTokenHolder(
            generateAccessToken(username),
            generateRefreshToken(username),
        )
    }

    fun validateAccessToken(token: String, username: String): Boolean {
        return (validateToken(token, username) && getTokenType(token) == tokenTypeAccess)
    }

    fun validateRefreshToken(token: String, username: String): Boolean {
        return (validateToken(token, username) && getTokenType(token) == tokenTypeRefresh)
    }

    fun generateAccessToken(username: String): String {
        val claims: Map<String, Any> = mapOf(Pair(tokenType, tokenTypeAccess))
        return doGenerateToken(username, accessExpirationMillis, claims)
    }

    fun generateRefreshToken(username: String): String {
        val claims: Map<String, Any> = mapOf(
            Pair(tokenType, tokenTypeRefresh),
            Pair(firstIssueDateField, Date(System.currentTimeMillis()))
        )
        return doGenerateToken(username, refreshExpirationMillis, claims)
    }

    private fun validateToken(token: String, username: String): Boolean {
        val tokenUsername = getUsernameFromToken(token)
        return (tokenUsername == username &&
                !isTokenExpired(token) &&
                isAudienceCorrect(token) &&
                isIssuerCorrect(issuer)
                )
    }

    private fun getSigningKey(): Key {
        val keyBytes = Decoders.BASE64.decode(this.secret)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    private fun doGenerateToken(
        subject: String,
        expirationMillis: Long,
        claims: Map<String, Any> = emptyMap(),
        headerParams: Map<String, Any> = emptyMap()
    ): String {
        return Jwts
            .builder()
            .setIssuer(issuer)
            .setAudience(audience)
            .setClaims(claims)
            .setHeaderParams(headerParams)
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expirationMillis))
            .signWith(getSigningKey())
            .compact()
    }

    fun getUsernameFromToken(token: String): String {
        return getClaimFromToken(token) { it.subject }
    }

    private fun getTokenType(token: String): String {
        return getClaimFromToken(token) { it[tokenType] }.toString()
    }

    private fun getExpirationDateFromToken(token: String): Date {
        return getClaimFromToken(token) { it.expiration }
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration.before(Date())
    }

    private fun isAudienceCorrect(token: String): Boolean {
        return getClaimFromToken(token) { "aud" } == audience
    }

    private fun isIssuerCorrect(token: String): Boolean {
        return getClaimFromToken(token) { "iss" } == issuer
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