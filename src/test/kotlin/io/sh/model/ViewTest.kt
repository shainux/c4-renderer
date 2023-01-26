package io.sh.model

import io.sh.TestHelper
import io.sh.model.parser.View
import io.sh.model.parser.ViewTypes
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ViewTest {

    @Test
    fun viewParsedSuccessfullyTest(){
        val testDsl = TestHelper.readDslFrom("individual/view.dsl")
        val actual = View.parse(testDsl)

        val expected = View(
            id = "systemLandscape_id",
            name = "System Landscape",
            type = ViewTypes.SystemLandscape,
            description = "Description of [] System {Landscape} Diagram",
            include = listOf("system1_id", "system2_id", "customer_id", "employee_id")
        )

        assertEquals(expected, actual)
    }

    @Test
    fun viewParamsPrecedenceCorrectTest(){
        val testDsl = TestHelper.readDslFrom("individual/view_param_override.dsl")
        val actual = View.parse(testDsl)

        val expected = View(
            id = "other_id",
            name = "some other name",
            type = ViewTypes.SystemLandscape,
            description = "some other description multiline",
            include = listOf("system1_id", "system2_id", "customer_id", "employee_id")
        )

        assertEquals(expected, actual)
    }
}
