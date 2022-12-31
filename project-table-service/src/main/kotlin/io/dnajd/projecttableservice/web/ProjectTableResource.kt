package io.dnajd.projecttableservice.web

import io.dnajd.projecttableservice.model.ProjectTable
import io.dnajd.projecttableservice.model.ProjectTableRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/project/table")
@RestController
class ProjectTableResource(val projectTableRepository: ProjectTableRepository) {

    /*
    // for testing
    @GetMapping("/getAll")
    fun getAll(): ProjectHolder {
        return ProjectHolder(projectTableRepository.findAll())
    }
     */

    @GetMapping("/{id}")
    fun get(
        @PathVariable("id") id: Long
    ): ProjectTable {
        return projectTableRepository.findById(id).get()
    }

}
