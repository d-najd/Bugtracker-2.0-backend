package io.dnajd.mainservice.domain.project_table

import com.cosium.spring.data.jpa.entity.graph.domain2.DynamicEntityGraph
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraphType
import com.fasterxml.jackson.annotation.JsonIgnore
import dev.krud.shapeshift.enums.AutoMappingStrategy
import dev.krud.shapeshift.resolver.annotation.AutoMapping
import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import dev.krud.shapeshift.resolver.annotation.MappedField
import dev.krud.shapeshift.transformer.ImplicitCollectionMappingTransformer
import io.dnajd.mainservice.domain.project.Project
import io.dnajd.mainservice.domain.table_issue.TableIssue
import io.dnajd.mainservice.infrastructure.mapper.DontMapCondition
import io.dnajd.mainservice.infrastructure.mapper.LazyInitializedCondition
import jakarta.persistence.*
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
@Table(name = "project_table", uniqueConstraints = [
    UniqueConstraint(columnNames = ["project_id", "title"]),
    UniqueConstraint(columnNames = ["project_id", "position"])
])
@AutoMapping(ProjectTableDto::class, AutoMappingStrategy.BY_NAME)
@DefaultMappingTarget(ProjectTableDto::class)
data class ProjectTable(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = -1L,

    @Column(updatable = false)
    var projectId: Long = -1L,

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "projectId", insertable = false, updatable = false)
    @MappedField(DontMapCondition::class)
    var project: Project? = null,

    @NotEmpty
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    var title: String = "",

    @NotNull
    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    @Min(0)
    var position: Int = -1,

    @OneToMany(mappedBy = "tableId", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = false)
    @MappedField(condition = LazyInitializedCondition::class, transformer = ImplicitCollectionMappingTransformer::class)
    var issues: MutableList<TableIssue> = mutableListOf()
) {
    companion object {
        /**
         * If [includeChildIssues] is then [includeIssues] will be ignored
         */
        fun entityGraph(
            includeIssues: Boolean = false,
            includeChildIssues: Boolean = false,
            graphType: EntityGraphType = EntityGraphType.LOAD,
        ): EntityGraph {
            val graph = DynamicEntityGraph.builder(graphType)

            if (includeChildIssues) {
                graph.addPath(ProjectTable::issues.name, TableIssue::childIssues.name)
            } else if (includeIssues) {
                graph.addPath(ProjectTable::issues.name)
            }

            return graph.build()
        }
    }
}

class ProjectTableList(val data: List<ProjectTable>)
