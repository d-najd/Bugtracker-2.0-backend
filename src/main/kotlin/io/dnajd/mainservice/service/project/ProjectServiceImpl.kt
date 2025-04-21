package io.dnajd.mainservice.service.project

import dev.krud.shapeshift.ShapeShift
import io.dnajd.mainservice.domain.project.*
import io.dnajd.mainservice.infrastructure.exception.ResourceNotFoundException
import io.dnajd.mainservice.infrastructure.mapper.mapChangedFields
import io.dnajd.mainservice.repository.ProjectRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
@Transactional
class ProjectServiceImpl(
    private val repository: ProjectRepository,
    private val mapper: ShapeShift,
): ProjectService {
    companion object {
        private val log = LoggerFactory.getLogger(ProjectServiceImpl::class.java)
    }

    override fun findAll(): List<Project> {
        return repository.findAll()
    }

    override fun getAllByUsername(username: String): ProjectDtoList {
        log.error("This is just a placeholder implementation till RBAC is implemented")

        val persistedProjects: List<ProjectDto> = mapper.mapCollection(repository.findAll())

        return ProjectDtoList(persistedProjects)
    }

    override fun find(id: Long): Project {
        return repository.findById(id).orElseThrow {
            throw ResourceNotFoundException(Project::class)
        }
    }

    override fun get(id: Long): ProjectDto {
        return mapper.map(find(id))
    }

    override fun create(projectDto: ProjectDto): ProjectDto {
        // TODO fix this
        val transientProject: Project = mapper.map(projectDto)
        transientProject.owner = "user1"
        val persistedProject = repository.save(transientProject)

        return mapper.map(persistedProject)
    }

    override fun update(id: Long, projectDto: ProjectDto): ProjectDto {
        val persistedProject = find(id)
        val transientProject = mapper.mapChangedFields(persistedProject, projectDto)

        return mapper.map(repository.saveAndFlush(transientProject))
    }

    override fun delete(id: Long) {
        val persistedProject = find(id)

        repository.delete(persistedProject)
    }
}