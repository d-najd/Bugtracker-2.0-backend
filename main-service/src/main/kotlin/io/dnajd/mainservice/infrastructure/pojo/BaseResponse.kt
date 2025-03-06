package io.dnajd.mainservice.infrastructure.pojo

abstract class BaseResponse(var id: Long)

abstract class BaseListResponse<response: BaseRequest>(
    var data: List<response>
)
