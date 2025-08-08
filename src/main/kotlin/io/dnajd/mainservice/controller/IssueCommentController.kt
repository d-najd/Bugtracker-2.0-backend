package io.dnajd.mainservice.controller

import io.dnajd.mainservice.domain.issue_comment.IssueCommentDto
import io.dnajd.mainservice.infrastructure.ScopedAuthorize
import io.dnajd.mainservice.infrastructure.Endpoints
import io.dnajd.mainservice.infrastructure.ScopedEvaluatorType
import io.dnajd.mainservice.infrastructure.ScopedPermission
import io.dnajd.mainservice.service.issue_comment.IssueCommentService
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(Endpoints.ISSUE_COMMENT)
class IssueCommentController(
    private val service: IssueCommentService
) {
    @PostMapping("/issueId/{issueId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ScopedAuthorize("#issueId", ScopedEvaluatorType.Issue, ScopedPermission.Create)
    fun create(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable issueId: Long,
        @RequestBody dto: IssueCommentDto
    ): IssueCommentDto {
        return service.create(issueId, userDetails.username, dto)
    }

    // I think the user should be able to edit the comment if they have already commented.
    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @ScopedAuthorize("#id", ScopedEvaluatorType.IssueComment, ScopedPermission.View)
    fun update(
        @PathVariable id: Long,
        @RequestBody dto: IssueCommentDto
    ): IssueCommentDto {
        return service.update(id, dto)
    }

    // I think the user should be able to edit the comment if they have already commented.
    @DeleteMapping("/{id}")
    @ScopedAuthorize("#id", ScopedEvaluatorType.IssueComment, ScopedPermission.View)
    fun delete(
        @PathVariable id: Long,
    ) {
        service.delete(id)
    }
}