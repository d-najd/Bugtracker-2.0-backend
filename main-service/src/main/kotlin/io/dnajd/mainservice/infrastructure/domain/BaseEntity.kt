package io.dnajd.mainservice.infrastructure.domain

import jakarta.persistence.*
import java.io.Serializable

@MappedSuperclass
abstract class BaseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long
): Serializable {
    companion object {
        private const val serialVersionUID: Long = 1L;
    }
}

open class BaseListEntity<entity: BaseEntity>(
    open var data: List<entity>
)