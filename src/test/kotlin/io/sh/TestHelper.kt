package io.sh

import io.sh.model.parser.Dsl

object TestHelper {
    fun readDslFrom(path: String): Dsl = ChartService.readDslScript(path).let{ Dsl(it.script, it.stack) }
}
