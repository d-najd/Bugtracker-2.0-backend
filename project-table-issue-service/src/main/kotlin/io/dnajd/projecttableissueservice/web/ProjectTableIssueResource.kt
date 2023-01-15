package io.dnajd.projecttableissueservice.web

import io.dnajd.projecttableissueservice.model.ProjectTableIssue
import io.dnajd.projecttableissueservice.model.ProjectTableIssueRepository
import io.dnajd.projecttableissueservice.util.QueryConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import reactor.kotlin.core.publisher.zip
import kotlin.jvm.optionals.getOrNull

@RequestMapping("/api")
@RestController
class ProjectTableIssueResource(val repository: ProjectTableIssueRepository) {
    @Autowired
    private lateinit var queryConstructor: QueryConstructor

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

    /**
     * FIXME the issues can be left in inconsistent state, similar issue exists in ProjectTableResource.kt so I won't
     *  bother to write the details here
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @OptIn(ExperimentalStdlibApi::class)
    @PatchMapping("/{id}/swapPositionWith/{sId}")
    fun swapIssuePositions(
        @PathVariable("id") id: Long,
        @PathVariable("sId") sId: Long,
    ) {
        repository.findById(id).getOrNull()?.let { fIssue ->
            repository.findByIdAndTableId(sId, fIssue.tableId).getOrNull()?.let { sIssue ->
                val query1 = "UPDATE project_table_issue SET position = ${fIssue.position} WHERE id = ${sIssue.id}"
                val query2 = "UPDATE project_table_issue SET position = ${sIssue.position} WHERE id = ${fIssue.id}"

                queryConstructor.executeUpdate(query1, query2)
                return
            }
            throw IllegalArgumentException("Can't find item with id $sId, the tasks have to be in the same table")
        }
        throw IllegalArgumentException("Can't find item with id $id")
    }

    /**
     *
     */
    @PatchMapping("/{id}/movePositionTo/{sId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @OptIn(ExperimentalStdlibApi::class)
    fun moveIssuePositions(
        @PathVariable("id") id: Long,
        @PathVariable("sId") sId: Long
    ) {
        repository.findById(id).getOrNull()?.let { fIssue ->
            repository.findByIdAndTableId(sId, fIssue.tableId).getOrNull()?.let { sIssue ->
                if(sIssue.position > fIssue.position) {
                    val query1 = "UPDATE project_table_issue SET position = position - 1 WHERE table_id = ${fIssue.tableId} AND position BETWEEN ${fIssue.position + 1} AND ${sIssue.position};"
                    val query2 = "UPDATE project_table_issue SET position = ${sIssue.position} WHERE id = ${fIssue.id};"
                    queryConstructor.executeUpdate(query1, query2)
                } else {
                    val query1 = "UPDATE project_table_issue SET position = position + 1 WHERE table_id = ${fIssue.tableId} AND position BETWEEN ${sIssue.position} AND ${fIssue.position - 1};"
                    val query2 = "UPDATE project_table_issue SET position = ${sIssue.position} WHERE id = ${fIssue.id};"
                    queryConstructor.executeUpdate(query1, query2)
                }
                return
            }
            throw IllegalArgumentException("Can't find item with id $sId, the tasks have to be in the same table")
        }
        throw IllegalArgumentException("Can't find item with id $id")
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long
    ) {
        repository.deleteById(id)
    }

}
