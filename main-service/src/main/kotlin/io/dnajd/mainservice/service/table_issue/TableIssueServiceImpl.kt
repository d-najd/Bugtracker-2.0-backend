package io.dnajd.mainservice.service.table_issue

import io.dnajd.mainservice.repository.TableIssueRepository
import dev.krud.shapeshift.ShapeShift
import io.dnajd.mainservice.domain.table_issue.TableIssue
import io.dnajd.mainservice.domain.table_issue.TableIssueDto
import io.dnajd.mainservice.domain.table_issue.TableIssueDtoList
import io.dnajd.mainservice.domain.table_issue.TableIssueList
import io.dnajd.mainservice.infrastructure.exception.ResourceNotFoundException
import io.dnajd.mainservice.infrastructure.mapper.mapChangedFields
import io.dnajd.mainservice.service.project.ProjectServiceImpl
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
@Transactional
class TableIssueServiceImpl(
    private val issueRepository: TableIssueRepository,
    private val mapper: ShapeShift,
): TableIssueService {
    companion object {
        private val log = LoggerFactory.getLogger(ProjectServiceImpl::class.java)
    }

    override fun findAll(): TableIssueList {
        return TableIssueList(issueRepository.findAll())
    }

    override fun getAllByTableId(tableId: Long): TableIssueDtoList {
        val persistedIssues = issueRepository.findAllByTableId(tableId)

        return TableIssueDtoList(mapper.mapCollection(persistedIssues))
    }

    override fun findById(id: Long): TableIssue {
        return issueRepository.findById(id).orElseThrow {
            log.error("Resource ${TableIssue::class.simpleName} with id $id not found")
            throw ResourceNotFoundException(TableIssue::class)
        }
    }

    override fun getById(id: Long): TableIssueDto {
        return mapper.map(findById(id))
    }

    override fun createIssue(tableId: Long, reporterUsername: String, dto: TableIssueDto): TableIssueDto {
        val issuesInTableCount = issueRepository.countByTableId(tableId)

        var transientIssue: TableIssue = mapper.map(dto)
        transientIssue = transientIssue.copy(
            tableId = tableId,
            reporter = reporterUsername,
            position = issuesInTableCount
        )

        val persistedIssue = issueRepository.save(transientIssue)
        return mapper.map(persistedIssue)
    }

    override fun updateIssue(id: Long, dto: TableIssueDto): TableIssueDto {
        val persistedIssue = findById(id)
        val transientIssue = mapper.mapChangedFields(persistedIssue, dto)

        return mapper.map(issueRepository.saveAndFlush(transientIssue))
    }

    override fun swapIssuePositions(fId: Long, sId: Long) {
        TODO("FINISH THIS")
        /*
        val firstIssue = findById(fId)
        val secondIssue = findById(sId)

        if (firstIssue.tableId != secondIssue.tableId) {
            log.error("Issues don't belong to the same table $fId $sId")
            throw IllegalArgumentException("Issues don't belong to the same table $fId $sId")
        }

        issueRepository.swapPositions(firstIssue.position, secondIssue.position)
         */
    }

    override fun changeTable(id: Long, tableId: Long) {
        TODO("Not yet implemented")
    }

    override fun setParentIssue(id: Long, parentIssueId: Long) {
        TODO("Not yet implemented")
    }

    override fun deleteIssue(id: Long) {
        TODO("NOT PROPERLY IMPLEMENTED, DELETING TABLE CAN LEAVE OTHER ISSUES IN TABLE IN BROKEN STATE")

        /*
        val persistedTable = findById(id)
        tableRepository.delete(persistedTable)
         */
    }
}