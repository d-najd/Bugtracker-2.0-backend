package io.dnajd.mainservice.domain.project

import com.fasterxml.jackson.annotation.JsonFormat
import dev.krud.shapeshift.enums.AutoMappingStrategy
import dev.krud.shapeshift.resolver.annotation.AutoMapping
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import org.hibernate.annotations.CreationTimestamp
import java.util.*

@Entity
@Table(name = "project")
@AutoMapping(ProjectDto::class, AutoMappingStrategy.BY_NAME)
data class Project(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = -1L,

    @NotEmpty
    var title: String = "",

    @Column(length = 65535)
    var description: String? = null,

    @JsonFormat(pattern = "yyyy-MM-d HH:mm:ss")
    @NotEmpty
    @CreationTimestamp
    var createdAt: Date = Date(),
)

fun Project.mapForUpdate(request: ProjectDto): Project {
    this.description = request.description
    this.title = request.title

    return this
}

class ProjectList(val data: List<Project>)