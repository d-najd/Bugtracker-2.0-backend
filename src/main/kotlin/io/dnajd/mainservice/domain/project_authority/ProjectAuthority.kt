package io.dnajd.mainservice.domain.project_authority

import com.fasterxml.jackson.annotation.JsonIgnore
import dev.krud.shapeshift.resolver.annotation.MappedField
import io.dnajd.mainservice.domain.authority.AuthorityType
import io.dnajd.mainservice.domain.project.Project
import io.dnajd.mainservice.domain.user.User
import io.dnajd.mainservice.infrastructure.mapper.DontMapCondition
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority

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
    @Enumerated(EnumType.STRING)
    val authority: AuthorityType = AuthorityType.PROJECT_VIEW,
): GrantedAuthority {
    override fun getAuthority(): String {
        return authority.value
    }
}

data class ProjectAuthorityId(
    val username: String = "",
    val projectId: Long = -1L,
    val authority: AuthorityType = AuthorityType.PROJECT_VIEW,
)