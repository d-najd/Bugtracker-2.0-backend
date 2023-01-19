package io.dnajd.projecttableservice.web

import io.dnajd.projecttableservice.model.ProjectTable
import io.dnajd.projecttableservice.model.ProjectTableRepository
import io.dnajd.projecttableservice.util.QueryConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import kotlin.jvm.optionals.getOrNull

@RequestMapping("/api")
@RestController
class ProjectTableResource(val repository: ProjectTableRepository) {
    @Autowired
    private lateinit var queryConstructor: QueryConstructor

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
        val transientPojo = if(pojo.position == -1) {
            val tableWithMaxPosition = repository.findAllByProjectId(projectId = pojo.projectId).maxBy { it.position }
            pojo.copy(position = tableWithMaxPosition.position + 1)
        } else pojo
        return repository.save(transientPojo.copy(
            issues = mutableListOf(),
        ))
    }

    @PutMapping
    fun update(
        @RequestBody pojo: ProjectTable,
    ): ProjectTable {
        throw NotImplementedError("Doesn't work for some reason")
        /*
        repository.findById(pojo.id).orElseThrow { throw IllegalArgumentException("Project Table with id ${pojo.id} does not exist") }
        return repository.saveAndFlush(pojo)
         */
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @OptIn(ExperimentalStdlibApi::class)
    @PatchMapping("/{id}/title/{newTitle}")
    fun renameProjectTable(
        @PathVariable("id") id: Long,
        @PathVariable("newTitle") newTitle: String,
    ) {
        repository.findById(id).getOrNull()?.let {
            it.title = newTitle
            repository.saveAndFlush(it)
            return
        }
        throw IllegalArgumentException()
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @OptIn(ExperimentalStdlibApi::class)
    @PatchMapping("/{id}/swapPositionWith/{sId}")
    fun swapProjectTablePositions(
        @PathVariable("id") id: Long,
        @PathVariable("sId") sId: Long,
    ) {
        repository.findById(id).getOrNull()?.let { fTable ->
            repository.findByIdAndProjectId(sId, fTable.projectId).getOrNull()?.let {sTable ->
                val query1 = "UPDATE project_table SET position = ${fTable.position} WHERE id = ${sTable.id}"
                val query2 = "UPDATE project_table SET position = ${sTable.position} WHERE id = ${fTable.id}"

                queryConstructor.executeUpdate(query1, query2)
                return
            }
        }
        throw IllegalArgumentException()
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long
    ) {
        repository.deleteById(id)
    }

}
