package io.dnajd.userauthorityservice.model

import org.springframework.data.jpa.repository.JpaRepository

interface UserAuthorityRepository: JpaRepository<UserAuthority, UserAuthorityIdentity> {

    fun findByProjectIdAndAuthority(projectId: Long, authority: UserAuthorityType): List<UserAuthority>

    fun findAllByUsername(username: String): List<UserAuthority>

    fun findAllByAuthorityAndProjectIdIn(authority: UserAuthorityType, projectIds: List<Long>): List<UserAuthority>

}