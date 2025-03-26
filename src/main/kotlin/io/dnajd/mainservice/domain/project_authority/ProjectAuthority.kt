package io.dnajd.mainservice.domain.project_authority

import io.dnajd.mainservice.domain.authority.AuthorityType
import jakarta.persistence.*

@IdClass(ProjectAuthorityId::class)
@Entity
@Table(name = "project_user_authority")
data class ProjectAuthority(
    @Id
    @Column(updatable = false)
    val username: String = "",

    @Id
    @Column(updatable = false)
    val projectId: Long = -1L,

    @Id
    @Column(updatable = false)
    val authority: AuthorityType = AuthorityType.PROJECT_VIEW,
)

data class ProjectAuthorityId(
    val username: String = "",
    val projectId: Long = -1L,
    val authority: AuthorityType = AuthorityType.PROJECT_VIEW,
)