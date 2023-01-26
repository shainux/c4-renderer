package io.sh.exception

class FileNotFoundException(fileName: String):Exception("Cannot find $fileName")
class DslFormatException(msg: String): Exception(msg)
class UnexpectedTokenException(msg: String): Exception(msg)
