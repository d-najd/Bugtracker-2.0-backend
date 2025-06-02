package io.dnajd.mainservice.domain.token

data class JwtUserTokenHolder(
    val access: String,
    val refresh: String,
)
