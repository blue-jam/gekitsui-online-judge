package bluejam.hobby.gekitsui.webapp.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class NotFoundException(message: String): Exception(message)

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class UnauthorizedException(message: String): Exception(message)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadRequestException(message: String): Exception(message)

@ResponseStatus(HttpStatus.FORBIDDEN)
class ForbiddenException(message: String): Exception(message)

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
class ServiceUnavailableException(message: String): Exception(message)
