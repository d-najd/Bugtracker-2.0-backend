package io.dnajd.userauthorityservice.model

import org.springframework.data.jpa.repository.JpaRepository

interface UserAuthorityRepository: JpaRepository<UserAuthority, UserAuthorityIdentity> {

    fun findAllByUsername(username: String): List<UserAuthority>

    fun findAllByProjectId(projectId: Long): List<UserAuthority>

    fun findAllByAuthorityAndProjectId(authority: UserAuthorityType, projectId: Long): List<UserAuthority>

    fun findAllByAuthorityAndProjectIdIn(authority: UserAuthorityType, projectIds: List<Long>): List<UserAuthority>

}