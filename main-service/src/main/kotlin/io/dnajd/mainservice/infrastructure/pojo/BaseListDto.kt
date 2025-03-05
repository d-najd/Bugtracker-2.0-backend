package io.dnajd.mainservice.infrastructure.pojo

open class BaseListDto<dto: BaseDto>(
    open var data: List<dto>
)