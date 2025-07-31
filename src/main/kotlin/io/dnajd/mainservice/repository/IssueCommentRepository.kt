package io.dnajd.mainservice.repository

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository
import io.dnajd.mainservice.domain.issue_comment.IssueComment
import org.springframework.stereotype.Repository

@Repository
interface IssueCommentRepository: EntityGraphJpaRepository<IssueComment, Long> {

}