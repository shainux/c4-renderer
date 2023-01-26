package io.sh.model.parser

import java.time.LocalDateTime

data class Script(
    val script: List<String>,
    val stack: List<String>,
    val lastModified: Long
)
