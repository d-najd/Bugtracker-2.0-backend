package io.dnajd.mainservice.domain.user

import com.cosium.spring.data.jpa.entity.graph.domain2.DynamicEntityGraph
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraphType
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import dev.krud.shapeshift.enums.AutoMappingStrategy
import dev.krud.shapeshift.resolver.annotation.AutoMapping
import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import dev.krud.shapeshift.resolver.annotation.MappedField
import dev.krud.shapeshift.transformer.ImplicitCollectionMappingTransformer
import io.dnajd.mainservice.domain.project_authority.ProjectAuthority
import io.dnajd.mainservice.domain.table_issue.TableIssue
import io.dnajd.mainservice.infrastructure.ImplicitCollectionMappingTransformerFixed
import io.dnajd.mainservice.infrastructure.mapper.LazyInitializedCondition
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.annotations.CreationTimestamp
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import jakarta.persistence.*
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import java.util.*
import kotlin.collections.HashSet

@Entity
@Table(
    name = "user",
    uniqueConstraints = [UniqueConstraint(columnNames = ["gmail"])]
)
@AutoMapping(UserDto::class, AutoMappingStrategy.BY_NAME)
@DefaultMappingTarget(UserDto::class)
data class User(
    @Id
    @NotEmpty
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    @JvmField
    val username: String = "",

    @NotEmpty
    @Column(nullable = false, updatable = false)
    val gmail: String = "",

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-d HH:mm:ss")
    @Column(nullable = false)
    @NotNull
    val createdAt: Date = Date(),

    @OneToMany(
        cascade = [CascadeType.REMOVE],
        fetch = FetchType.LAZY,
    )
    @JoinColumn(name = "username")
    @MappedField(condition = LazyInitializedCondition::class, transformer = ImplicitCollectionMappingTransformerFixed::class)
    val projectAuthorities: MutableList<ProjectAuthority> = mutableListOf(),
) : UserDetails {
    companion object {
        fun entityGraph(
            includeAuthorities: Boolean = false,
            graphType: EntityGraphType = EntityGraphType.LOAD,
        ): DynamicEntityGraph {
            val graph = DynamicEntityGraph.builder(graphType)

            if (includeAuthorities) {
                graph.addPath(User::projectAuthorities.name)
            }

            return graph.build()
        }
    }

    @JsonIgnore
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return projectAuthorities
    }

    override fun getUsername(): String {
        return username
    }

    @JsonIgnore
    override fun getPassword(): String {
        return ""
    }

    @JsonIgnore
    override fun isAccountNonExpired(): Boolean {
        return super.isAccountNonExpired()
    }

    @JsonIgnore
    override fun isAccountNonLocked(): Boolean {
        return super.isAccountNonLocked()
    }

    @JsonIgnore
    override fun isCredentialsNonExpired(): Boolean {
        return super.isCredentialsNonExpired()
    }
}