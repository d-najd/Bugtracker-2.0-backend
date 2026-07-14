package io.dnajd.mainservice.infrastructure.jwt

import io.dnajd.mainservice.domain.token.JwtTokenHolder
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import java.security.Key
import java.util.*

object JwtUtil {
	// H512 Base64 encoded key
	private const val SECRET =
		"KUeiSYMzwk/I7E2p8SBSmhSnBVfP56iimMGM6J5yJLAdAtf3sN4MmC7a7fxMnXU2ahFG1Aaq+38tuwOANLbVXw=="
	private const val ACCESS_EXPIRATION_MILLIS = 24 * 60 * 60 * 1000L // 1 day
	private const val REFRESH_EXPIRATION_MILLIS = 30 * 24 * 60 * 60 * 1000L // 30 days
	private const val REFRESH_MAX_EXPIRATION_MILLIS = 365 * 24 * 60 * 60 * 1000L // 365 days
	private const val ISSUER = "d-najd.bugtracker.backend"
	private const val AUDIENCE = "d-najd.bugtracker.android"

	// Field is in epoch seconds like "exp" and "iat"
	private const val FIRST_ISSUE_DATE_FIELD = "first_issue_date"

	private const val TOKEN_TYPE = "token_type"
	private const val TOKEN_TYPE_ACCESS = "access"
	private const val TOKEN_TYPE_REFRESH = "refresh"

	fun generateUserTokens(username: String): JwtTokenHolder =
		JwtTokenHolder(
			generateAccessToken(username),
			generateRefreshToken(username),
		)

	fun refreshAccessAndRefreshTokens(refreshToken: String): JwtTokenHolder {
		val username = getUsernameFromToken(refreshToken)
		val firstIssueDateMilli = getFirstIssueDate(refreshToken).toInstant().toEpochMilli()
		val expirationDateMilli = getExpirationDateFromToken(refreshToken).toInstant().toEpochMilli()

		val maxExpirationMilli = firstIssueDateMilli + REFRESH_MAX_EXPIRATION_MILLIS
		val expirationMilli = minOf(maxExpirationMilli, expirationDateMilli)
		val subtractedExpirationMilli = expirationMilli - Date().toInstant().toEpochMilli()

		return JwtTokenHolder(
			generateAccessToken(username),
			generateRefreshToken(username, getFirstIssueDate(refreshToken), subtractedExpirationMilli),
		)
	}

	fun refreshAccessToken(username: String): JwtTokenHolder =
		JwtTokenHolder(
			generateAccessToken(username),
		)

	fun validateAccessToken(
		token: String,
		username: String,
	): Boolean = (validateToken(token, username) && getTokenType(token) == TOKEN_TYPE_ACCESS)

	fun validateRefreshToken(
		token: String,
		username: String,
	): Boolean {
		val firstIssueDate = getFirstIssueDate(token)

		return (
			validateToken(token, username) &&
				getTokenType(token) == TOKEN_TYPE_REFRESH
		)
	}

	fun getUsernameFromToken(token: String): String = getClaimFromToken(token) { it.subject }

	/**
	 * extracts Bearer token from supplied Authorization header
	 */
	fun extractTokenFromHeader(requestTokenHeader: String): String {
		if (requestTokenHeader.startsWith("Bearer ")) {
			return requestTokenHeader.substring(7)
		} else {
			throw IllegalArgumentException("JWT Token does not begin with Bearer String")
		}
	}

	private fun generateAccessToken(username: String): String {
		val claims: Map<String, Any> = mapOf(Pair(TOKEN_TYPE, TOKEN_TYPE_ACCESS))
		return doGenerateToken(username, ACCESS_EXPIRATION_MILLIS, claims)
	}

	/**
	 * @param expirationMillis how many milliseconds till expiration
	 */
	private fun generateRefreshToken(
		username: String,
		firstIssueDate: Date = Date(System.currentTimeMillis()),
		expirationMillis: Long = REFRESH_EXPIRATION_MILLIS,
	): String {
		val claims: Map<String, Any> = mapOf(
			Pair(TOKEN_TYPE, TOKEN_TYPE_REFRESH),
			Pair(FIRST_ISSUE_DATE_FIELD, firstIssueDate.toInstant().epochSecond),
		)
		return doGenerateToken(username, expirationMillis, claims)
	}

	private fun validateToken(
		token: String,
		username: String,
	): Boolean {
		val tokenUsername = getUsernameFromToken(token)
		return (
			tokenUsername == username &&
				!isTokenExpired(token) &&
				isAudienceCorrect(token) &&
				isIssuerCorrect(token)
		)
	}

	private fun getSigningKey(): Key {
		val keyBytes = Decoders.BASE64.decode(this.SECRET)
		return Keys.hmacShaKeyFor(keyBytes)
	}

	private fun doGenerateToken(
		subject: String,
		expirationMillis: Long,
		claims: Map<String, Any> = emptyMap(),
		headerParams: Map<String, Any> = emptyMap(),
	): String =
		Jwts
			.builder()
			.setClaims(claims)
			.setIssuer(ISSUER)
			.setAudience(AUDIENCE)
			.setHeaderParams(headerParams)
			.setSubject(subject)
			.setIssuedAt(Date())
			.setExpiration(Date(System.currentTimeMillis() + expirationMillis))
			.signWith(getSigningKey())
			.compact()

	private fun getTokenType(token: String): String = getClaimFromToken(token) { it[TOKEN_TYPE] }.toString()

	private fun getExpirationDateFromToken(token: String): Date = getClaimFromToken(token) { it.expiration }

	private fun isTokenExpired(token: String): Boolean {
		val expiration = getExpirationDateFromToken(token)
		return expiration.before(Date())
	}

	private fun isAudienceCorrect(token: String): Boolean = getClaimFromToken(token) { it.audience } == AUDIENCE

	private fun isIssuerCorrect(token: String): Boolean = getClaimFromToken(token) { it.issuer } == ISSUER

	private fun getFirstIssueDate(token: String): Date {
		val date = Date(getClaimFromToken(token) { it[FIRST_ISSUE_DATE_FIELD] } as Int * 1000L)
		return date
	}

	private fun <T> getClaimFromToken(
		token: String,
		claimsResolver: (Claims) -> T,
	): T {
		val claims = getAllClaimsFromToken(token)
		return claimsResolver(claims)
	}

	private fun getAllClaimsFromToken(token: String): Claims =
		Jwts
			.parserBuilder()
			.setSigningKey(getSigningKey())
			.build()
			.parseClaimsJws(token)
			.body
}
