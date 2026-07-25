package io.dnajd.mainservice.domain.user

import com.cosium.spring.data.jpa.entity.graph.domain2.DynamicEntityGraph
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraphType
import com.fasterxml.jackson.annotation.JsonIgnore
import dev.krud.shapeshift.enums.AutoMappingStrategy
import dev.krud.shapeshift.resolver.annotation.AutoMapping
import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import dev.krud.shapeshift.resolver.annotation.MappedField
import io.dnajd.mainservice.domain.projectauthority.ProjectAuthority
import io.dnajd.mainservice.infrastructure.ImplicitCollectionMappingTransformerFixed
import io.dnajd.mainservice.infrastructure.mapper.LazyInitializedCondition
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.annotations.CreationTimestamp
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Entity
@Table(
	name = "user",
	uniqueConstraints = [UniqueConstraint(columnNames = ["gmail"])],
)
@AutoMapping(UserDto::class, AutoMappingStrategy.BY_NAME)
@DefaultMappingTarget(UserDto::class)
data class User(
	@field:Id
	@field:NotEmpty
	@field:Size(max = 255)
	@field:Column(nullable = false, length = 255)
	@field:JvmField
	val username: String = "",
	@field:NotEmpty
	@field:Column(nullable = false, updatable = false)
	val gmail: String = "",
	@field:CreationTimestamp
	@field:Column(nullable = false)
	@field:NotNull
	val createdAt: Date = Date(),
	@field:OneToMany(
		cascade = [CascadeType.REMOVE],
		fetch = FetchType.LAZY,
	)
	@field:JoinColumn(name = "username")
	@field:MappedField(condition = LazyInitializedCondition::class, transformer = ImplicitCollectionMappingTransformerFixed::class)
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
	override fun getAuthorities(): MutableCollection<out GrantedAuthority> = projectAuthorities

	override fun getUsername(): String = username

	@JsonIgnore
	override fun getPassword(): String = ""

	@JsonIgnore
	override fun isAccountNonExpired(): Boolean = super.isAccountNonExpired()

	@JsonIgnore
	override fun isAccountNonLocked(): Boolean = super.isAccountNonLocked()

	@JsonIgnore
	override fun isCredentialsNonExpired(): Boolean = super.isCredentialsNonExpired()
}
