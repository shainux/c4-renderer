package io.sh.model.renderer.component
/*
import io.sh.extension.appendTo
import io.sh.model.parser.Coordinate
import io.sh.model.renderer.*
import org.w3c.dom.Element

data class Connection(
    val id: String? = null,
    var from: String,
    var to: String,
    var path: List<Pair<Int, Int>>,
    val label: String? = null,
    val labelPosition: Coordinate = Coordinate(0,0),
    val stroke: String = "black"
): SvgNode {
    override fun render(canvas: Canvas, parent: Element){
        val connection = canvas.new(groupElement, attrId to "${id}_wrapper")
        val drawPath = ""
        val path = canvas.new(pathElement, attrId to id, attrD to drawPath).appendTo(connection)
        label?.let{
            Text("${id}_label", label,  Text.Attributes(labelPosition.x, labelPosition.y))
                .render(canvas, connection)
        }
        connection.appendTo(canvas.getRoot())
    }
}*/
