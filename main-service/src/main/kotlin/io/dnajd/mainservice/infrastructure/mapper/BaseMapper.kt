package io.dnajd.mainservice.infrastructure.mapper

interface BaseMapper<Dto, Entity> {
    fun toDtoToEntity(input: Entity): Dto
    fun toEntityToDto(input: Dto): Entity
    fun fromEntityToDtoList(input: List<Entity>): List<Dto>
    fun fromDtoToEntityList(input: List<Dto>): List<Entity>
}