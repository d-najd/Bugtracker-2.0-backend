package io.dnajd.mainservice.domain.user

import com.fasterxml.jackson.annotation.JsonFormat
import dev.krud.shapeshift.enums.AutoMappingStrategy
import dev.krud.shapeshift.resolver.annotation.AutoMapping
import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import dev.krud.shapeshift.resolver.annotation.MappedField
import dev.krud.shapeshift.transformer.ImplicitCollectionMappingTransformer
import io.dnajd.mainservice.domain.issue_assignee.IssueAssignee
import io.dnajd.mainservice.domain.project_authority.ProjectAuthority
import io.dnajd.mainservice.infrastructure.mapper.LazyInitializedCondition
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.annotations.CreationTimestamp
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import jakarta.persistence.*
import org.hibernate.Hibernate
import java.util.*

@Entity
@Table(name = "user")
@AutoMapping(UserDto::class, AutoMappingStrategy.BY_NAME)
@DefaultMappingTarget(UserDto::class)
data class User(
    @Id
    @NotEmpty
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    @JvmField
    var username: String = "",

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-d HH:mm:ss")
    @Column(nullable = false)
    @NotNull
    var createdAt: Date = Date(),

    @OneToMany(
        cascade = [CascadeType.REMOVE],
        fetch = FetchType.LAZY,
    )
    @JoinColumn(name = "username")
    @MappedField(condition = LazyInitializedCondition::class, transformer = ImplicitCollectionMappingTransformer::class)
    var projectAuthorities: MutableList<ProjectAuthority> = mutableListOf(),
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return projectAuthorities
    }

    override fun getPassword(): String {
        return ""
    }

    override fun getUsername(): String {
        return username
    }
}