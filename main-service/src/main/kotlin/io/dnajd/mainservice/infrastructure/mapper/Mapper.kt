package io.dnajd.mainservice.infrastructure.mapper

import dev.krud.shapeshift.ShapeShiftBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Mapper {
    @Bean
    fun shapeShiftMapper() = ShapeShiftBuilder().build()
}