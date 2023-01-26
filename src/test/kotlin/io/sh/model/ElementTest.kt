package io.sh.model

import io.sh.TestHelper
import io.sh.model.parser.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ElementTest {

    @Test
    fun elementParsedSuccessfullyTest() {
        val testDsl = TestHelper.readDslFrom("individual/softwareSystem.dsl")
        val actual = ChartNode.parse(testDsl)

        val relationship1 = Relationship(from = "user_id", to = "this", label = "consumes")
        val relationship2 = Relationship(from = "this", to = "another_id", label = "interacts")
        val relationship3 = Relationship(
            from = "this",
            to = "one_more_id",
            path = listOf(Coordinate(1, 1, 1), Coordinate(10, 50, 3), Coordinate(120, 240, 1)),
            label = "some label",
            labelPosition = Coordinate(20, 20, 2),
            description = "some description"
        )

        val prospective1 = Perspective(
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
        )

        val prospective2 = Perspective(
            viewId = "second_view_id",
            label = "second view label",
            description = "second view description",
            position = Coordinate(1, 2, 5),
            dimensions = Dimensions(200, 200),
        )

        val child1 = ChartNode(
            type = Elements.Container,
            id = "first_container_id",
            name = "First Container",
            description = "multiline description must replace the one from the top",
            tags = listOf("tag11", "tag12"),
            properties = mapOf("key1" to "value1", "key2" to "complex value 2", "key3" to "val3"),
            perspectives = listOf(prospective1, prospective2),
            children = mutableListOf(
                ChartNode(type = Elements.Component, id = "component_11_id", name = "component_11"),
                ChartNode(type = Elements.Component),
                ChartNode(type = Elements.Component, id = "component_12_id"),
                ChartNode(type = Elements.Component, name = "component_13")
            )
        )

        val child2 = ChartNode(
            type = Elements.Container,
            id = "second_container_id",
            name = "Second Container",
            description = "multiline second container's description must replace the one from the top",
            tags = listOf("tag21", "tag22"),
            properties = mapOf("key1" to "value1", "key2" to "complex value 2", "key3" to "val3"),
            perspectives = listOf(prospective1, prospective2),
            children = mutableListOf(
                ChartNode(type = Elements.Component, id = "component_21_id", name = "component_21"),
                ChartNode(type = Elements.Component),
                ChartNode(type = Elements.Component, id = "component_22_id"),
                ChartNode(type = Elements.Component, name = "component_23")
            )
        )

        val expected = ChartNode(
            type = Elements.SoftwareSystem,
            id = "softwareSystem_id",
            name = "someSoftwareSystem",
            description = "Description of Software System single line",
            tags = listOf("tag1", "tag2", "tag3"),
            url = "https://www.example.com/some-uri?with=params",
            relationships = listOf(relationship1, relationship2, relationship3),
            docs = listOf("/some/relative/path.md", "/some/another/relative/path.md"),
            children = mutableListOf(child1, child2)
        )

        assertEquals(expected, actual)
    }
}
