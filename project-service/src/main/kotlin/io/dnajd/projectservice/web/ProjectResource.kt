package io.dnajd.projectservice.web

import io.dnajd.projectservice.model.ProjectRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/project")
@RestController
class ProjectResource(val projectRepository: ProjectRepository) {

    // for testing
    @GetMapping
    fun getAll(): ProjectHolder {
        return ProjectHolder(projectRepository.findAll())
    }

    @GetMapping("/user/{userID}")
    fun getAll(
        @PathVariable("userID") userID: Long
    ): ProjectHolder {
        return ProjectHolder(projectRepository.findAllById(userID))
    }

}