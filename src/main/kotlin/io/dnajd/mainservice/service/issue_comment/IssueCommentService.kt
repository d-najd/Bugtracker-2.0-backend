package io.dnajd.mainservice.service.issue_comment

import io.dnajd.mainservice.domain.issue_comment.IssueCommentDto

interface IssueCommentService {
    fun create(issueId: Long, commenterUsername: String, dto: IssueCommentDto): IssueCommentDto

    fun update(id: Long, dto: IssueCommentDto): IssueCommentDto

    fun delete(id: Long)
}