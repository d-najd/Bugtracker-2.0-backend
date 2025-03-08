package io.dnajd.mainservice.domain.project

import com.fasterxml.jackson.annotation.JsonFormat
import dev.krud.shapeshift.enums.AutoMappingStrategy
import dev.krud.shapeshift.resolver.annotation.AutoMapping
import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import dev.krud.shapeshift.resolver.annotation.MappedField
import io.dnajd.mainservice.infrastructure.mapper.DontMapCondition
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import org.hibernate.annotations.CreationTimestamp
import java.util.*

@Entity
@Table(name = "project")
@AutoMapping(ProjectDto::class, AutoMappingStrategy.BY_NAME)
@DefaultMappingTarget(ProjectDto::class)
data class Project(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = -1L,

    @NotEmpty
    var title: String = "",

    @NotEmpty
    var owner: String = "",

    @Column(length = 65535)
    @NotBlank
    var description: String? = null,

    @JsonFormat(pattern = "yyyy-MM-d HH:mm:ss")
    @NotEmpty
    @CreationTimestamp
    var createdAt: Date? = null,
)

fun Project.mapForUpdate(request: ProjectDto): Project {
    this.description = request.description
    this.title = request.title

    return this
}

class ProjectList(val data: List<Project>)