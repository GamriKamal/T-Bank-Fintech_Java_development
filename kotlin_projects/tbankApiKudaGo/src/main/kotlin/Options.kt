package org.example

data class Options(
    var delimiter: String = " | ",
    var prefix: String = "{ ",
    var suffix: String = " }",
    var coloredOutput: Boolean = false
)

val shadesOfBlue = listOf(
    "\u001B[34m",
    "\u001B[36m",
    "\u001B[94m",
    "\u001B[96m",
    "\u001B[38;5;33m",
    "\u001B[38;5;153m"
)
