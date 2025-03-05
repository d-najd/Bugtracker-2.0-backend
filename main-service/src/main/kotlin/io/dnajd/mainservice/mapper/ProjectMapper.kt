package io.dnajd.mainservice.mapper

import io.dnajd.mainservice.domain.Project
import io.dnajd.mainservice.dto.ProjectDto
import io.dnajd.mainservice.infrastructure.mapper.BaseMapper
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.NullValuePropertyMappingStrategy
import java.util.*

@Mapper(componentModel = "spring", uses = [BaseMapper::class])
interface ProjectMapper : BaseMapper<ProjectDto, Project> {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun mapRequestedFieldForUpdate(dto: ProjectDto, project: Project): Project {
        project.title = dto.title
        project.description = dto.description
        project.createdAt = Date()

        return project
    }
}