package io.dnajd.projecttableissueservice.web

import io.dnajd.projecttableissueservice.model.ProjectTableIssue
import io.dnajd.projecttableissueservice.model.ProjectTableIssueRepository
import io.dnajd.projecttableissueservice.util.QueryConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.ConcurrentModificationException

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

    @GetMapping("/{id}")
    fun get(
        @PathVariable id: Long,
    ): ProjectTableIssue {
        return repository.findById(id).orElseThrow {
            throw IllegalArgumentException("Can't find item with id: $id")
        }
    }

    @PostMapping
    fun post(
        @RequestBody pojo: ProjectTableIssue,
    ): ProjectTableIssue {
        val maxPos = repository.findAllByTableId(tableId = pojo.tableId).maxByOrNull { it.position }?.position ?: -1
        val transientPojo = pojo.copy(position = maxPos + 1)
        return repository.save(transientPojo.copy(
            createdAt = Date(),
            updatedAt = null,
            comments = mutableListOf(),
            labels = mutableListOf(),
            assigned = mutableListOf(),
            table = null,
            childIssues = mutableListOf(),
        ))
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable("id") id: Long,
        @RequestParam("title") title: String? = null,
        @RequestParam("description") description: String? = null,
        @RequestParam("severity") severity: Int? = null,
        @RequestParam("returnBody") shouldReturnBody: Boolean = true,
    ): ProjectTableIssue? {
        val persistedIssue = repository.findById(id).orElseThrow { throw IllegalArgumentException("Project Table with id $id does not exist") }
        val returnBody = repository.saveAndFlush(
            persistedIssue.copy(
                title = title ?: persistedIssue.title,
                description = description ?: persistedIssue.description,
                severity = severity ?: persistedIssue.severity,
                updatedAt = Date(),
            )
        )
        return if(shouldReturnBody) returnBody else null
    }

    /**
     * FIXME the issues can be left in inconsistent state, similar issue exists in ProjectTableResource.kt so I won't
     *  bother to write the details here
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{id}/swapPositionWith/{sId}")
    fun swapIssuePositions(
        @PathVariable("id") id: Long,
        @PathVariable("sId") sId: Long,
    ) {
        val fIssue = repository.findById(id).orElseThrow {
            throw IllegalArgumentException("Can't find item with id $id")
        }
        val sIssue = repository.findByIdAndTableId(id = id, tableId = fIssue.tableId).orElseThrow {
            throw IllegalArgumentException("Can't find item with id $sId, the tasks have to be in the same table")
        }

        val query1 = "UPDATE project_table_issue SET position = ${fIssue.position} WHERE id = ${sIssue.id}"
        val query2 = "UPDATE project_table_issue SET position = ${sIssue.position} WHERE id = ${fIssue.id}"
        queryConstructor.executeUpdate(query1, query2)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{id}/swapTable/{tableId}")
    fun changeIssueTable(
        @PathVariable("id") id: Long,
        @PathVariable("tableId") tableId: Long,
    ) {
        val issue = repository.findById(id).orElseThrow {
            throw IllegalArgumentException("Can't find item with id $id")
        }
        if(issue.tableId == tableId) {
            throw IllegalArgumentException("Can't set issue to the same table, issue id $id, table id $tableId")
        }
        val swapTableIssues = repository.findAllByTableId(tableId).ifEmpty {
            // TODO add edge case where the table is empty, it should check using the table service if its online
            throw IllegalArgumentException("Can't find issues related to table $tableId")
        }
        val prevTableIssues = repository.findAllByTableId(issue.tableId).ifEmpty {
            throw ConcurrentModificationException("Please try again in a few seconds")
        }

        val query1 = "UPDATE project_table_issue SET position = position + 1 WHERE table_id = $tableId;"
        val query2 = "UPDATE project_table_issue SET position = 0 AND table_id = $tableId WHERE id = $id"
        val query3 = "UPDATE project_table_issue SET table_id = $tableId WHERE id = $id"
        val query4 = "UPDATE project_table_issue SET position = position - 1 WHERE table_id = ${issue.tableId} AND position > ${issue.position}"
        queryConstructor.executeUpdate(query1, query2, query3, query4)
    }

    /**
     *
     */
    @PatchMapping("/{id}/movePositionTo/{sId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveIssuePositions(
        @PathVariable("id") id: Long,
        @PathVariable("sId") sId: Long
    ) {
        val fIssue = repository.findById(id).orElseThrow {
            throw IllegalArgumentException("Can't find item with id $id")
        }
        val sIssue = repository.findByIdAndTableId(id = sId, tableId = fIssue.tableId).orElseThrow {
            throw IllegalArgumentException("Can't find item with id $sId, the tasks have to be in the same table")
        }
        if(sIssue.position > fIssue.position) {
            val query1 = "UPDATE project_table_issue SET position = position - 1 WHERE table_id = ${fIssue.tableId} AND position BETWEEN ${fIssue.position + 1} AND ${sIssue.position};"
            val query2 = "UPDATE project_table_issue SET position = ${sIssue.position} WHERE id = ${fIssue.id};"
            queryConstructor.executeUpdate(query1, query2)
        } else {
            val query1 = "UPDATE project_table_issue SET position = position + 1 WHERE table_id = ${fIssue.tableId} AND position BETWEEN ${sIssue.position} AND ${fIssue.position - 1};"
            val query2 = "UPDATE project_table_issue SET position = ${sIssue.position} WHERE id = ${fIssue.id};"
            queryConstructor.executeUpdate(query1, query2)
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long
    ) {
        repository.deleteById(id)
    }

}
