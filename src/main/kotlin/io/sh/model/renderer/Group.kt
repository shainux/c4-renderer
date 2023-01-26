package io.sh.model.renderer

import io.sh.model.parser.ChartNode
import io.sh.Renderer.group
import java.awt.Dimension
import java.awt.Point

data class Group(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
   // val attr: Attributes = Attributes(),
    val position: Point = Point(0,0),
    val dimension: Dimension = Dimension(100, 100),
    val children: MutableList<SvgNode> = mutableListOf()
): SvgNode {
    companion object{
        fun from(el: ChartNode, parentId: String): Group = Group(
            id = "${parentId}_${el.id?:"${el.type.name}_${System.nanoTime()}"}",
            name = el.name,
            description = el.description
        )
    }

    override fun render() = group(id, name, position, dimension, children, "")

    /*data class Attributes(
        val x: Int = 0,
        val y: Int = 0,
        val w: Int = boxSize.width,
        val h: Int = boxSize.height,
        val baseFontSize: Int = 12,
        val stroke: String = "black",
        val strokeWidth: Int = 2,
        val fillColor: String = "gray",
        val opacity: Float = 0f
    ) {
        fun getAll():Array<Pair<String, Any?>> = arrayOf(
            attrX to x, attrY to y, attrWidth to w, attrHeight to h, attrStrokeType to stroke,
            attrStrokeWidth to strokeWidth, attrFill to fillColor, attrOpacity to opacity
        )
    }*/
}
