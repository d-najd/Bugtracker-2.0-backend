package io.dnajd.mainservice.service.project

import io.dnajd.mainservice.domain.Project
import io.dnajd.mainservice.domain.ProjectList
import io.dnajd.mainservice.dto.ProjectDto
import io.dnajd.mainservice.dto.ProjectListDto
import org.springframework.stereotype.Service

@Service
interface ProjectService {
    fun findAll(): ProjectList

    fun getAllByUsername(username: String): ProjectListDto

    fun findById(id: Long): Project

    fun getById(id: Long): ProjectDto

    fun createProject(projectDto: ProjectDto): ProjectDto

    fun updateProject(projectDto: ProjectDto): ProjectDto

    fun deleteProject(id: Long)
}