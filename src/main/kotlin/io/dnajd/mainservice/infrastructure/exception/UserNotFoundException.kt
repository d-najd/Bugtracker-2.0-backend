package io.dnajd.mainservice.infrastructure.exception

class UserNotFoundException: RuntimeException {
    constructor(): super()

    constructor(message: String): super(message)

    constructor(message: String, throwable: Throwable): super(message, throwable)

    constructor(throwable: Throwable): super(throwable)
}
