package io.dnajd.mainservice.service.project

import io.dnajd.mainservice.domain.project.*

interface ProjectService {
    fun findAll(): List<Project>

    fun getAllByUsername(username: String): ProjectDtoList

    fun findById(id: Long): Project

    fun getById(id: Long): ProjectDto

    fun createProject(projectDto: ProjectDto): ProjectDto

    fun updateProject(id: Long, projectDto: ProjectDto): ProjectDto

    fun deleteById(id: Long)
}