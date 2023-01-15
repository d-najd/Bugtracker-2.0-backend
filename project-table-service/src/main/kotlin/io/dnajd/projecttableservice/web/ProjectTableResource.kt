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
        return repository.save(pojo)
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

    /**
     * FIXME calling this method can leave the project table in inconsistent state because there are multiple queries,
     *  for example lets say user1 swaps table1 { position = 1 } with table2 { position = 2 }, now lets say the first
     *  query gets executed and in the meantime user2 attempts to swap table2 { position = 2 } with table1 { position = -1 }
     *  now if user1's request manages to execute before any of the queries for user2 start being executed table2 will
     *  have { position = -1 } thus corrupting any further position swapping in the current table, a way to swap positions
     *  in 1 query without using position like -1 will solve this problem
     */
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

    /*
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long
    ) {
        repository.deleteById(id)
    }
     */

}
