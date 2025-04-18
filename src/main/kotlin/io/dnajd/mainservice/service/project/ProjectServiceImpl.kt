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
    private val projectRepository: ProjectRepository,
    private val mapper: ShapeShift,
): ProjectService {
    companion object {
        private val log = LoggerFactory.getLogger(ProjectServiceImpl::class.java)
    }

    override fun findAll(): List<Project> {
        return projectRepository.findAll()
    }

    override fun getAllByUsername(username: String): ProjectDtoList {
        log.error("This is just a placeholder implementation till RBAC is implemented")

        val persistedProjects: List<ProjectDto> = mapper.mapCollection(projectRepository.findAll())

        return ProjectDtoList(persistedProjects)
    }

    override fun findById(id: Long): Project {
        return projectRepository.findById(id).orElseThrow {
            log.error("Resource ${Project::class.simpleName} with id $id not found")
            throw ResourceNotFoundException(Project::class)
        }
    }

    override fun getById(id: Long): ProjectDto {
        return mapper.map(findById(id))
    }

    override fun createProject(projectDto: ProjectDto): ProjectDto {
        // TODO fix this
        val transientProject: Project = mapper.map(projectDto)
        transientProject.owner = "user1"
        val persistedProject = projectRepository.save(transientProject)

        return mapper.map(persistedProject)
    }

    override fun updateProject(id: Long, projectDto: ProjectDto): ProjectDto {
        val persistedProject = findById(id)
        val transientProject = mapper.mapChangedFields(persistedProject, projectDto)

        return mapper.map(projectRepository.saveAndFlush(transientProject))
    }

    override fun deleteById(id: Long) {
        val persistedProject = findById(id)

        projectRepository.delete(persistedProject)
    }
}