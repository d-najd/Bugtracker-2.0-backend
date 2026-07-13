package io.dnajd.mainservice.service.issuecomment

import io.dnajd.mainservice.domain.issuecomment.IssueCommentDto

interface IssueCommentService {
    fun create(issueId: Long, commenterUsername: String, dto: IssueCommentDto): IssueCommentDto

    fun update(id: Long, dto: IssueCommentDto): IssueCommentDto

    fun delete(id: Long)
}