package io.dnajd.mainservice.infrastructure.exception

import kotlin.reflect.KClass

class ResourceNotFoundException: RuntimeException {
    constructor(): super()

    constructor(message: String): super(message)

    constructor(message: String, throwable: Throwable): super(message, throwable)

    constructor(throwable: Throwable): super(throwable)

    constructor(clazz: KClass<*>): super("Resource: ${clazz.simpleName} not found")
}