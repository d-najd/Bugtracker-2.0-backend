package io.dnajd.userauthorityservice.model

import org.springframework.data.jpa.repository.JpaRepository

interface UserAuthorityRepository: JpaRepository<UserAuthority, UserAuthorityIdentity> {

    fun findByProjectIdAndAuthority(projectId: Long, authority: UserAuthorityType): List<UserAuthority>

}