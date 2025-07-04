package io.dnajd.mainservice.domain.issue_comment

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import dev.krud.shapeshift.enums.AutoMappingStrategy
import dev.krud.shapeshift.resolver.annotation.AutoMapping
import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*

@Entity
@Table(name = "project_table_issue_comment")
@AutoMapping(IssueCommentDto::class, AutoMappingStrategy.BY_NAME)
@DefaultMappingTarget(IssueCommentDto::class)
data class IssueComment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    val id: Long = -1L,

    @NotEmpty
    @Column(updatable = false)
    val user: String = "",

    @JsonIgnore
    @Column(updatable = false)
    val issueId: Long = -1L,

    @Column(columnDefinition = "TEXT")
    val message: String = "",

    @Column
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-d HH:mm:ss")
    @NotEmpty
    val createdAt: Date = Date(),

    @Column
    @JsonFormat(pattern = "yyyy-MM-d HH:mm:ss")
    @UpdateTimestamp
    @NotEmpty
    val editedAt: Date? = null,
)
