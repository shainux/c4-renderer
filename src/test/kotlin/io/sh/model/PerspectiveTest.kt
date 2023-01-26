package io.sh.model

import io.sh.TestHelper
import io.sh.model.parser.Coordinate
import io.sh.model.parser.Dimensions
import io.sh.model.parser.Perspective
import io.sh.model.parser.Relationship
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PerspectiveTest {

    @Test
    fun relationshipsParsedCorrectTest(){
        val expected = listOf(
            Perspective(
                viewId = "first_view_id",
                label = "first view label",
                description = "first view description",
                position = Coordinate(1, 1, 5),
                dimensions = Dimensions(100, 200),
                relationships = listOf(
                    Relationship(
                        from = "this", to = "some_id",
                        path = listOf(Coordinate(1, 1), Coordinate(2, 10, 1), Coordinate(4, 5)),
                        label = "first view label",
                        labelPosition = Coordinate(2, 3),
                        description = "first view description"
                    )

                )
            ),
            Perspective(
                viewId = "second_view_id",
                label = "second view label",
                description = "second view description",
                position = Coordinate(1, 2, 5),
                dimensions = Dimensions(200, 200),
            )
        )
        val testDsl = TestHelper.readDslFrom("individual/perspectives.dsl")
        val actual = testDsl.parseList(Perspective::parse)

        assertTrue(actual.containsAll(expected) && expected.containsAll(actual))
    }
}
