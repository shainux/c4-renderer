package io.sh.model.renderer

import io.sh.extension.append
import io.sh.model.parser.ViewTypes
import io.sh.model.parser.Workspace
import io.sh.Renderer.rootGroup
import io.sh.Renderer.setImageSize
import java.util.*

class Canvas(
    var width: String = "1600",
    var height: String = "1000",
    var chartType: String? = null,
    var chartName: String? = null,
    var chartDescription: String? = null,
    var chartVersion: String? = null,
    val elements: List<Node>
){
    companion object{
        fun from(w: Workspace, elements: List<Node>): Canvas = Canvas(
                chartName = w.name,
                chartType = ViewTypes.SystemLandscape.svgLabel,
                chartVersion = "v. ${w.version?:"0.1"}, last edited: ${df.format(Date(w.lastModified))}",
                chartDescription = w.description,
                elements = elements
            )
    }
    fun render(): Canvas {
        for(e in elements){
            if(e.id == "root"){
                setImageSize(e.dimension)
            } else {
                rootGroup.append(e.render())
            }
        }
        return this
    }

    /*private fun adjustSize(totalElements: Int): Canvas {
        val layout = calculateNodeLayout(totalElements)
        this.width = ((layout.first+2) * (boxSize.width + boxPadding)).toString()
        this.height = ((layout.second+2) * (boxSize.height + boxPadding)).toString()
        return this
    }
    private fun calculateNodeLayout(totalNodes: Int): Pair<Int, Int>{
        if(totalNodes<4) { return Pair(totalNodes,1) }
        var v = 2
        var h = totalNodes/2f
        var curRatio:Float = h/v
        while (curRatio > layoutRatio){
            v++
            h = totalNodes.toFloat()/v
            curRatio = h/v
        }

        return Pair(ceil(h).toInt(), v)
    }*/

    /*fun addElement(qualifiedName: String, id: String?=null, svgAttr: SvgAttr?=null): Element {
        val e = new(qualifiedName, *svgAttr?.getAll())
        id?.let{ e.setAttributeNS(null, "id", it)}
        svgAttr?.serialiseAttributes(e)
        return e
    }*/


}
