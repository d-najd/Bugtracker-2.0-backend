package io.dnajd.projecttableissueservice.web

import io.dnajd.projecttableissueservice.model.ProjectTableIssue
import io.dnajd.projecttableissueservice.model.ProjectTableIssueRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RequestMapping("/api")
@RestController
class ProjectTableIssueResource(val repository: ProjectTableIssueRepository) {

    @GetMapping("/testing/getAll")
    fun getAll(): ProjectTableIssueHolder {
        return ProjectTableIssueHolder(repository.findAll())
    }

    @GetMapping("/tableId/{tableId}")
    fun getAllByTableId(
        @PathVariable tableId: Long
    ): ProjectTableIssueHolder {
        return ProjectTableIssueHolder(repository.findAllByTableId(tableId))
    }

    @PostMapping
    fun post(
        @RequestBody pojo: ProjectTableIssue,
    ): ProjectTableIssue {
        return repository.save(pojo)
    }

    @PutMapping
    fun update(
        @RequestBody pojo: ProjectTableIssue,
    ): ProjectTableIssue {
        repository.findById(pojo.id).orElseThrow { throw IllegalArgumentException("Project Table with id ${pojo.id} does not exist") }
        return repository.saveAndFlush(pojo)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long
    ) {
        repository.deleteById(id)
    }

}
