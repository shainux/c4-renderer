package io.sh.model.renderer.component
/*
import io.sh.extension.appendTo
import io.sh.model.renderer.*
import org.w3c.dom.Element

data class Text(
    val id: String = "text_${System.nanoTime()}",
    val str: String,
    val attr: Attributes
) : SvgNode {
    override fun render(canvas: Canvas, parent: Element){
        canvas.new(textElement, attrId to id, *attr.getAll()).appendTo(parent)
    }

    data class Attributes(
        val x: Int = 0,
        val y: Int = 0,
        val dominantBaseline: String = "middle",
        val fontFamily: String = "Arial, Helvetica, sans-serif",
        val fontSize: Int = 12,
        val fontDecor: String = "none",
        val color: String = "black"
    ) {
        fun getAll():Array<Pair<String, Any?>> {
            val attributes = mutableListOf(
                attrX to x, attrY to y,
                attrDominantBaseline to dominantBaseline,
                attrFontFamily to fontFamily, attrFontSize to fontSize,
                attrColor to color
            )
            when (fontDecor) {
                "bold" -> attributes.add(attrFontWeight to "bold")
                "italic" -> attributes.add(attrFontStyle to "italic")
                "bold-italic" -> {
                    attributes.add(attrFontWeight to "bold")
                    attributes.add(attrFontStyle to "italic")
                }
            }
            return attributes.toTypedArray()
        }
    }
}*/
