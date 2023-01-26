package io.sh

import io.sh.model.parser.splitToWordsAndSubstrings
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ExtensionFunctionsTest {

    @Test
    fun stringSplitWorksCorrectlyTest(){
        val actual = "second_view_id{".splitToWordsAndSubstrings()
        val expected = listOf("second_view_id", "{")

        assertTrue(actual.containsAll(expected) && expected.containsAll(actual))
    }
}
