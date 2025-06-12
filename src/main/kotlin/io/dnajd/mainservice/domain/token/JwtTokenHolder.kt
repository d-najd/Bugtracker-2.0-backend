package io.dnajd.mainservice.domain.token

data class JwtTokenHolder(
    val access: String? = null,
    val refresh: String? = null,
)
