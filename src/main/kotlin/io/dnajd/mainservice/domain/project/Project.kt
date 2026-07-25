package io.dnajd.mainservice.domain.project

import dev.krud.shapeshift.enums.AutoMappingStrategy
import dev.krud.shapeshift.resolver.annotation.AutoMapping
import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import dev.krud.shapeshift.resolver.annotation.MappedField
import io.dnajd.mainservice.domain.projecttable.ProjectTable
import io.dnajd.mainservice.infrastructure.mapper.DontMapCondition
import io.dnajd.mainservice.infrastructure.usercontent.UserContentDirs
import io.dnajd.mainservice.infrastructure.usercontent.UserContentPathMapper
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.annotations.CreationTimestamp
import java.nio.file.Files
import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString

@Entity
@Table(name = "project")
@AutoMapping(ProjectDto::class, AutoMappingStrategy.BY_NAME)
@DefaultMappingTarget(ProjectDto::class)
data class Project(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = -1L,
	@field:NotEmpty
	@field:Size(max = 255)
	@Column(nullable = false, length = 255)
	val title: String = "",
	@Column
	val owner: String = "",
	@field:NotBlank
	@Column(columnDefinition = "TEXT")
	val description: String? = null,
	@field:NotBlank
	@field:NotNull
	@Column(nullable = false)
	@MappedField(DontMapCondition::class)
	val iconUri: String = generateDefaultIconUri(),
	@field:NotNull
	@CreationTimestamp
	@Column(nullable = false)
	val createdAt: Date? = null,
	@OneToMany(mappedBy = "projectId", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
	@MappedField(DontMapCondition::class)
	val tables: Set<ProjectTable> = emptySet(),
) {
	companion object {
		private var defaultProjectIconUris: Set<String>? = null

		private fun generateDefaultIconUri(): String {
			if (defaultProjectIconUris == null) {
				populateDefaultProjectIconUris()
			}
			return defaultProjectIconUris!!.random()
		}

		private fun populateDefaultProjectIconUris() {
			val defaultProjectIconsPath =
				Path("${UserContentDirs.ABSOLUTE_PATH}${UserContentDirs.BASE}${UserContentDirs.DEFAULT_PROJECT_ICON}")
			if (Files.notExists(defaultProjectIconsPath)) {
				throw IllegalStateException("Default projects missing")
			}
			val defaultProjectIcons = Files.list(defaultProjectIconsPath).toList()
			if (defaultProjectIcons.isEmpty()) {
				throw IllegalStateException("Default projects missing")
			}

			defaultProjectIconUris = defaultProjectIcons
				.map {
					UserContentPathMapper.absolutePathToUserContentUri(it.absolutePathString())
				}.toSet()
		}
	}
}
