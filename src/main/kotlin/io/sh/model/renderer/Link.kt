package io.sh.model.renderer

import io.sh.Renderer
import io.sh.model.parser.Coordinate
import io.sh.model.parser.Relationship
import java.awt.Point

data class Link(
    val id: String? = null,
    var path: List<Point> = emptyList(),
    val label: String? = null,
    val labelPosition: Point = Point(0,0),
    val stroke: String = "black",
    val belongsToGroupLink: Boolean = false
): SvgNode {
    companion object{
        fun from(r: Relationship): Link = Link(
                "link_${r.from}_${r.to}",
                r.path?.map(Coordinate::toPoint)?: emptyList(),
                r.label,
                r.labelPosition?.toPoint()?:Point(0,0)
            )
    }
    override fun render() = Renderer.Link(id, label, labelPosition, path, "")
}
