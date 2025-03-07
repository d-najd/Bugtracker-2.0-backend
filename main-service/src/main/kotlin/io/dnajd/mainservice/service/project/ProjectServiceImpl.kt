package io.dnajd.mainservice.service.project

import dev.krud.shapeshift.ShapeShift
import io.dnajd.mainservice.domain.project.*
import io.dnajd.mainservice.infrastructure.exception.ResourceNotFoundException
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

    override fun findAll(): ProjectList {
        val se = projectRepository.findAll()
        return ProjectList(projectRepository.findAll())
    }

    override fun getAllByUsername(username: String): ProjectDtoList {
        TODO("Not yet implemented")
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
        val transientProject: Project = mapper.map(projectDto)
        val persistedProject = projectRepository.save(transientProject)

        return mapper.map(persistedProject)
    }

    override fun updateProject(id: Long, projectDto: ProjectDto): ProjectDto {
        val persistedProject = findById(id)
        val transientProject = persistedProject.mapForUpdate(projectDto)

        return mapper.map(projectRepository.saveAndFlush(transientProject))
    }

    override fun deleteProject(id: Long) {
        val persistedProject = findById(id)
        projectRepository.delete(persistedProject)
    }
}