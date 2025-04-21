package io.dnajd.mainservice.domain.project_authority

import dev.krud.shapeshift.enums.AutoMappingStrategy
import dev.krud.shapeshift.resolver.annotation.AutoMapping
import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority

@Entity
@IdClass(ProjectAuthorityIdentity::class)
@Table(name = "project_user_authority")
@AutoMapping(ProjectAuthorityDto::class, AutoMappingStrategy.BY_NAME)
@DefaultMappingTarget(ProjectAuthorityDto::class)
data class ProjectAuthority(
    @Id
    @Column(updatable = false)
    val username: String = "",

    @Id
    @Column(updatable = false)
    val projectId: Long = -1L,

    @Id
    @Column(updatable = false)
    @JvmField
    val authority: String = "",
) : GrantedAuthority {
    override fun getAuthority(): String {
        return authority
    }
}

data class ProjectAuthorityIdentity(
    val username: String = "",
    val projectId: Long = -1L,
    val authority: String = "",
)