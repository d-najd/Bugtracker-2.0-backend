package io.dnajd.mainservice.service.project

import io.dnajd.mainservice.domain.Project.*
import io.dnajd.mainservice.infrastructure.exception.ResourceNotFoundException
import io.dnajd.mainservice.mapper.ProjectMapper
import io.dnajd.mainservice.repository.ProjectRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@Transactional
class ProjectServiceImpl: ProjectService {
    @Autowired
    private lateinit var projectRepository: ProjectRepository

    // @Autowired
    // private lateinit var projectMapper: ProjectMapper

    companion object {
        private val log = LoggerFactory.getLogger(ProjectServiceImpl::class.java)
    }

    override fun findAll(): ProjectList {
        return ProjectList(projectRepository.findAll())
    }

    override fun getAllByUsername(username: String): ProjectListResponse {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): Project {
        return projectRepository.findById(id).orElseThrow {
            log.error("Resource ${Project::class.simpleName} with id $id not found")
            throw ResourceNotFoundException(Project::class)
        }
    }

    override fun getById(id: Long): ProjectResponse {
        return projectMapper.toDtoToEntity(findById(id))
    }

    override fun createProject(projectRequest: ProjectRequest): ProjectResponse {
        val transientProject = projectMapper.toEntityToDto(projectRequest)
        val persistedProject = projectRepository.save(transientProject)

        return projectMapper.toDtoToEntity(persistedProject)
    }

    override fun updateProject(id: Long, projectRequest: ProjectRequest): ProjectResponse {
        val persistedProject = findById(id)
        val transientProject = projectMapper.mapRequestedFieldForUpdate(projectRequest, persistedProject)

        return projectMapper.toDtoToEntity(projectRepository.saveAndFlush(transientProject))
    }

    override fun deleteProject(id: Long) {
        val persistedProject = findById(id)
        projectRepository.delete(persistedProject)
    }
}