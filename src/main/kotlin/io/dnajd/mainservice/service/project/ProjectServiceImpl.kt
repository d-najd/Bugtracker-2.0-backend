package io.dnajd.mainservice.service.project

import dev.krud.shapeshift.ShapeShift
import io.dnajd.mainservice.domain.project.Project
import io.dnajd.mainservice.domain.project.ProjectDto
import io.dnajd.mainservice.domain.project.ProjectDtoList
import io.dnajd.mainservice.domain.project_authority.ProjectAuthority
import io.dnajd.mainservice.domain.project_table.ProjectTable
import io.dnajd.mainservice.infrastructure.PreAuthorizePermission
import io.dnajd.mainservice.infrastructure.mapper.mapChangedFields
import io.dnajd.mainservice.repository.ProjectAuthorityRepository
import io.dnajd.mainservice.repository.ProjectRepository
import io.dnajd.mainservice.repository.ProjectTableRepository
import jakarta.transaction.Transactional
import org.hibernate.Hibernate
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
@Transactional
class ProjectServiceImpl(
    private val repository: ProjectRepository,
    private val projectAuthorityRepository: ProjectAuthorityRepository,
    private val projectTableRepository: ProjectTableRepository,
    private val mapper: ShapeShift,
) : ProjectService {
    companion object {
        private val log = LoggerFactory.getLogger(ProjectServiceImpl::class.java)
    }

    override fun getAllByUsername(username: String): ProjectDtoList {
        val persistedProjects: List<ProjectDto> = mapper.mapCollection(repository.getAllByUsername(username))

        return ProjectDtoList(persistedProjects)
    }

    override fun get(id: Long): ProjectDto {
        return mapper.map(repository.getReferenceById(id))
    }

    override fun create(username: String, projectDto: ProjectDto): ProjectDto {
        val transientProject: Project = mapper.map<ProjectDto, Project>(projectDto).copy(owner = username)
        val persistedProject = repository.saveAndFlush(transientProject)

        val ownerProjectAuthority = ProjectAuthority(
            username = username,
            projectId = persistedProject.id,
            authority = PreAuthorizePermission.Owner.value,
        )
        projectAuthorityRepository.saveAndFlush(ownerProjectAuthority)

        val todoTable = ProjectTable(
            projectId = persistedProject.id,
            title = "TO-DO",
            position = 0,
        )

        val inProgressTable = ProjectTable(
            projectId = persistedProject.id,
            title = "In Progress",
            position = 1,
        )

        val doneTable = ProjectTable(
            projectId = persistedProject.id,
            title = "Done",
            position = 2,
        )

        projectTableRepository.saveAllAndFlush(listOf(todoTable, inProgressTable, doneTable))
        return mapper.map(persistedProject)
    }

    override fun update(id: Long, projectDto: ProjectDto): ProjectDto {
        val persistedProject = repository.getReferenceById(id)
        val transientProject = mapper.mapChangedFields(persistedProject, projectDto)

        return mapper.map(repository.saveAndFlush(transientProject))
    }

    override fun delete(id: Long) {
        repository.deleteDirectlyById(id)
    }
}