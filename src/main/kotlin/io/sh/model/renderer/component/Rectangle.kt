package io.sh.model.renderer.component
/*
import io.sh.extension.appendTo
import io.sh.model.renderer.*
import org.w3c.dom.Element

data class Rectangle(
    val id: String,
    val attr: Attributes = Attributes()
) : SvgNode {
    companion object {
        fun fromNodeAttr(id: String, attr: Node.Attributes) = Rectangle(
            id = id,
            Attributes(
                x = attr.x, y = attr.y, w = attr.w, h = attr.h,
                stroke = attr.stroke, strokeWidth = attr.strokeWidth,
                fill = attr.fillColor, opacity = attr.opacity?:1f
            )
        )
    }

    override fun render(canvas: Canvas, parent: Element) {
        canvas.new(rectElement, attrId to id, *attr.getAll())
            .appendTo(parent)
    }

    data class Attributes(
        var x: Int = 0,
        var y: Int = 0,
        val w: Int = boxSize.width,
        val h: Int = boxSize.height,
        val rx: Int = 10,
        val ry: Int = 10,
        val stroke: String = "black",
        val strokeWidth: Int = 2,
        val fill: String = "transparent",
        val opacity: Float = 0f
    ) {
        fun getAll(): Array<Pair<String, Any?>> = arrayOf(
            attrX to x, attrY to y, attrWidth to w, attrHeight to h, attrRx to rx, attrRy to ry,
            attrStrokeType to stroke, attrStrokeWidth to strokeWidth, attrFill to fill, attrOpacity to opacity
        )
    }
}*/
