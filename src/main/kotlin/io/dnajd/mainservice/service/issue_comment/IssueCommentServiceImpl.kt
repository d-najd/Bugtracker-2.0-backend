package io.dnajd.mainservice.service.issue_comment

import dev.krud.shapeshift.ShapeShift
import io.dnajd.mainservice.domain.issue_comment.IssueComment
import io.dnajd.mainservice.domain.issue_comment.IssueCommentDto
import io.dnajd.mainservice.infrastructure.mapper.mapChangedFields
import io.dnajd.mainservice.repository.IssueCommentRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class IssueCommentServiceImpl(
    private val repository: IssueCommentRepository,
    private val mapper: ShapeShift,
) : IssueCommentService {
    override fun create(issueId: Long, commenterUsername: String, dto: IssueCommentDto): IssueCommentDto {
        val transientComment = mapper.map<IssueCommentDto, IssueComment>(dto)
            .copy(
                issueId = issueId,
                user = commenterUsername
            )

        val persistedComment = repository.save(transientComment)
        return mapper.map(persistedComment)
    }

    override fun update(id: Long, dto: IssueCommentDto): IssueCommentDto {
        val persistedComment = repository.getReferenceById(id)
        val transientComment = mapper.mapChangedFields(persistedComment, dto)

        return mapper.map(repository.saveAndFlush(transientComment))
    }

    override fun delete(id: Long) {
        repository.deleteById(id)
    }
}