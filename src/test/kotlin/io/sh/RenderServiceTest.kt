package io.sh

import io.sh.Renderer
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class RenderServiceTest {

    @Test
    fun nodeLayoutIsRightTest() {
        val inputArr =  listOf(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 15, 19, 23, 27, 35)
        val expectedV = listOf(1, 1, 2, 2, 2, 2, 2, 3,  3,  3,  3, 4, 4, 4, 5)

        for ((idx, i) in inputArr.withIndex()) {
            val l = Renderer.calculateNodeLayout(i)
            assertEquals(expectedV[idx], l.second)
        }
    }

    @Test
    fun renderChartSuccessTest(){
        ChartService.renderChart("rendering/simpleSystems.dsl", "rendering/styles.css")

    }
}
