package io.dnajd.mainservice.infrastructure.domain

open class BaseListEntity<entity: BaseEntity>(
    open var data: List<entity>
)
