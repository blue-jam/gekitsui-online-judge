package bluejam.hobby.gekitsui.judge.tool.validator

class InvalidFormatException: Exception {
    constructor()

    constructor(message: String): super(message)

    constructor(cause: Throwable): super(cause)
}
