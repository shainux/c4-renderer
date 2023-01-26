package io.sh.model.renderer

import io.sh.Renderer
import io.sh.model.parser.ChartNode
import io.sh.model.parser.Elements
import java.awt.Dimension
import java.awt.Point

data class Node(
    val type: Elements,
    val id: String,
    val slug: List<String>,
    val name: String? = null,
    val description: String? = null,
    var position: Point = Point(0,0),
    var dimension: Dimension = boxSize
//    val attr: Attributes = Attributes()
) : SvgNode {
    companion object {
        fun from(el: ChartNode, parentSlug: List<String>): Node {
            val id = el.id ?: "${el.type.name}_${System.nanoTime()}"
            val slug = parentSlug.toMutableList()
            slug.add(id)
            return Node(el.type, id, slug, el.name, el.description)
        }
    }

    override fun render() = Renderer.box(type, id, name, description, position, type.cssClasses)

    /*data class Attributes(
        var id: String = "",
        var x: Int = 0,
        var y: Int = 0,
        var w: Int = boxSize.width,
        var h: Int = boxSize.height,
        val baseFontSize: Int = 12,
        val stroke: String = "black",
        val strokeWidth: Int = 2,
        val fillColor: String = "gray",
        val opacity: Float? = null
    ) {
        fun getAll(): Array<Pair<String, Any?>> = listOf(
            attrX to x, attrY to y, attrWidth to w, attrHeight to h, attrStrokeType to stroke,
            attrStrokeWidth to strokeWidth, attrFill to fillColor, attrOpacity to opacity
        ).filter { it.second != null }.toTypedArray()
    }*/
}
