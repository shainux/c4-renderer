package io.sh.model

import io.sh.TestHelper
import io.sh.model.parser.Coordinate
import io.sh.model.parser.Relationship
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class RelationshipTest {

    @Test
    fun relationshipsParsedCorrectTest(){
        val expected = listOf(
            Relationship(
                from = "this", to = "some_id",
                path = listOf(Coordinate(1, 1), Coordinate(2, 10, 1), Coordinate(4, 5)),
                label = "first view label",
                labelPosition = Coordinate(2, 3),
                description = "first view description"
            ),
            Relationship(from = "user_id", to = "this", label = "consumes"),
            Relationship(from = "this", to = "another_id", label = "interacts"),
            Relationship(
                from = "this",
                to = "one_more_id",
                path = listOf(Coordinate(1, 1, 1), Coordinate(10, 50, 3), Coordinate(120, 240, 1)),
                label = "some label",
                labelPosition = Coordinate(20, 20, 2),
                description = "some description"
            )
        )
        val testDsl = TestHelper.readDslFrom("individual/relationships.dsl")
        val actual = testDsl.parseList(Relationship::parse)

        assertTrue(actual.containsAll(expected) && expected.containsAll(actual))
    }

    @Test
    fun relationshipParsedCorrectTest(){
        val expected = listOf(
            Relationship(
                from = "this", to = "some_id",
                path = listOf(Coordinate(1, 1), Coordinate(2, 10, 1), Coordinate(4, 5)),
                label = "first view label",
                labelPosition = Coordinate(2, 3),
                description = "first view description"
            )
        )
        val testDsl = TestHelper.readDslFrom("individual/relationship.dsl")
        val actual = testDsl.parseList(Relationship::parse)

        assertTrue(actual.containsAll(expected) && expected.containsAll(actual))
    }
}
