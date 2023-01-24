package io.dnajd.userauthorityservice.web

import io.dnajd.userauthorityservice.model.UserAuthority

data class UserAuthorityHolder(
    val data: List<UserAuthority> = emptyList()
)