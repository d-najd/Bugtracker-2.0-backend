package io.dnajd.mainservice.service.project

import io.dnajd.mainservice.domain.project.*
import org.springframework.stereotype.Service

@Service
interface ProjectService {
    fun findAll(): ProjectList

    fun getAllByUsername(username: String): ProjectDtoList

    fun findById(id: Long): Project

    fun getById(id: Long): ProjectDto

    fun createProject(projectDto: ProjectDto): ProjectDto

    fun updateProject(id: Long, projectDto: ProjectDto): ProjectDto

    fun deleteProject(id: Long)
}