package io.dnajd.projecttableservice.web

import io.dnajd.projecttableservice.model.ProjectTable
import io.dnajd.projecttableservice.model.ProjectTableRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.jvm.Throws
import kotlin.jvm.optionals.getOrNull

@RequestMapping("/api")
@RestController
class ProjectResource(val repository: ProjectTableRepository) {
    @GetMapping("/testing/getAll")
    fun getAll(): ProjectTableHolder {
        return ProjectTableHolder(repository.findAll())
    }

    @GetMapping("/projectId/{projectId}")
    fun getAllByProjectId(
        @PathVariable projectId: Long
    ): ProjectTableHolder {
        return ProjectTableHolder(repository.findAllByProjectId(projectId))
    }

    @PostMapping
    fun post(
        @RequestBody pojo: ProjectTable,
    ): ProjectTable {
        return repository.save(pojo)
    }

    @OptIn(ExperimentalStdlibApi::class)
    @PatchMapping("/{id}/title/{newTitle}")
    fun renameProjectTable(
        @PathVariable("id") id: Long,
        @PathVariable("newTitle") newTitle: String,
    ): ProjectTable {
        repository.findById(id).getOrNull()?.let {
            it.title = newTitle
            return repository.saveAndFlush(it)
        }
        throw IllegalArgumentException()
    }

    @PutMapping
    fun update(
        @RequestBody pojo: ProjectTable,
    ): ProjectTable {
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
