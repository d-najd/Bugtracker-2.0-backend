package io.dnajd.mainservice.service.project

import io.dnajd.mainservice.domain.project.*
import org.springframework.stereotype.Service

@Service
interface ProjectService {
    fun findAll(): ProjectList

    fun getAllByUsername(username: String): ProjectListResponse

    fun findById(id: Long): Project

    fun getById(id: Long): ProjectResponse

    fun createProject(projectRequest: ProjectRequest): ProjectResponse

    fun updateProject(id: Long, projectRequest: ProjectRequest): ProjectResponse

    fun deleteProject(id: Long)
}