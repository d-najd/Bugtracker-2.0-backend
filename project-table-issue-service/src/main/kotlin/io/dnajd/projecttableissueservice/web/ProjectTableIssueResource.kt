package io.dnajd.projecttableissueservice.web

import io.dnajd.projecttableissueservice.model.ProjectTableIssueRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api")
@RestController
class ProjectTableIssueResource(val repository: ProjectTableIssueRepository) {

    @GetMapping("/testing/getAll")
    fun getAll(): ProjectTableIssueHolder {
        return ProjectTableIssueHolder(repository.findAll())
    }

}
