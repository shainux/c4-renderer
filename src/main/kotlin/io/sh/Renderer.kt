package io.sh

import com.google.common.graph.Network
import io.sh.extension.append
import io.sh.extension.appendTo
import io.sh.extension.set
import io.sh.model.parser.Elements
import io.sh.model.renderer.*
import org.apache.batik.dom.GenericDOMImplementation
import org.apache.batik.svggen.SVGGraphics2D
import org.apache.batik.util.SVGConstants
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.awt.Dimension
import java.awt.Point
import java.io.File
import java.io.StringWriter
import kotlin.math.ceil

@Suppress("UnstableApiUsage")
object Renderer {
    fun calculateLayout(graph: Network<Node, Link>): List<Node> {
        val root = graph.nodes().first { it.id == "root" }
        val (people, extSystem, intSystem) = groupNodes(graph.successors(root))
        val (nodesHor, nodesVert) = calculateNodeLayout(people.size, extSystem.size, intSystem.size)
        root.dimension = Dimension(
            nodesHor * (boxPadding + boxSize.width) + boxPadding,
            nodesVert * (boxPadding + boxSize.height) + boxPadding
        )
        val matrix = Matrix(root.dimension.width, root.dimension.height)

        // render people at top
        var cursorX = 0
        var cursorY = boxPadding
        var offsetX: Int
        var itmCounter: Int
        if (people.isNotEmpty()) {
            offsetX = (root.dimension.width - (boxSize.width + boxPadding) * people.size + boxPadding) / 2
            people.forEach { n ->
                n.position = Point(offsetX + cursorX, cursorY)
                cursorX += n.dimension.width + boxPadding
                matrix.occupy(n.position.x-40, n.position.y, boxSize.width, boxSize.height)
            }
            cursorY += boxSize.height + boxPadding
            cursorX = 0
        }
        offsetX = boxPadding
        // render ext systems at sides
        if (extSystem.isNotEmpty()) {
            itmCounter = 1
            extSystem.forEach { s ->
                s.position = Point(offsetX + cursorX, cursorY)
                cursorY += s.dimension.height + boxPadding
                if (itmCounter++ > nodesVert) {                     // flip side for the second half
                    cursorX = root.dimension.width - boxPadding - boxSize.width
                    cursorY += boxSize.height + boxPadding
                }
                matrix.occupy(s.position.x, s.position.y, boxSize.width, boxSize.height)
            }
            // render internal elements at the center
            cursorX = 0
            cursorY += boxSize.height + boxPadding
            offsetX = boxSize.width + boxPadding
        }

        itmCounter = 1
        var nodesLeft = intSystem.size
        if (nodesLeft < nodesHor) {
            offsetX += (root.dimension.width - (boxSize.width + boxPadding) * intSystem.size) / 2
        }
        intSystem.forEach { s ->
            s.position = Point(offsetX + cursorX, cursorY)
            cursorX += s.dimension.width + boxPadding
            if (itmCounter++ >= nodesHor) {                    // next line
                nodesLeft -= nodesHor
                if (nodesLeft < nodesHor) {
                    offsetX += (root.dimension.width - (boxSize.width + boxPadding) * nodesLeft) / 2
                }
                cursorX = 0
                cursorY += boxSize.height + boxPadding
                itmCounter = 1
            }
            matrix.occupy(s.position.x, s.position.y, boxSize.width, boxSize.height)
        }

        return listOf(root) + people + intSystem + extSystem
    }

    /**
     * @return List of people, List of external systems, List of internal systems
     */
    private fun groupNodes(nodes: Set<Node>): Triple<List<Node>, List<Node>, List<Node>> {
        val people = mutableListOf<Node>()
        val extSystem = mutableListOf<Node>()
        val intSystem = mutableListOf<Node>()
        for (n in nodes) {
            when (n.type) {
                Elements.Person, Elements.ExtPerson -> people.add(n)
                Elements.ExtSystem -> extSystem.add(n)
                else -> intSystem.add(n)
            }
        }
        return Triple(people, extSystem, intSystem)
    }

    fun calculateNodeLayout(peopleCount: Int, extSystemCount: Int, intSystemCount: Int): Pair<Int, Int> {
        if (intSystemCount < 4) {
            return Pair(intSystemCount, 1)
        }
        var v = 2
        var h = intSystemCount / 2f
        var curRatio: Float = h / v
        while (curRatio > layoutRatio) {
            v++
            h = intSystemCount.toFloat() / v
            curRatio = h / v
        }

        var hor = ceil(h).toInt()
        if (peopleCount > 0) {
            v++
            if (hor < peopleCount) {
                hor = peopleCount
            }
        }
        if (extSystemCount > 0) {
            hor++
            if (v < (extSystemCount / 2)) {
                v = extSystemCount / 2
                hor++
            }
        }

        return Pair(hor, v)
    }

    private fun new(qualifiedName: String, vararg attr: Pair<String, Any?>): Element {
        val e = doc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, qualifiedName)
        attr.forEach { a ->
            a.second?.let {
                e.setAttributeNS(null, a.first, it.toString())
            }
        }
        return e
    }

    fun storeToFile(fileName: String, canvas: Canvas, styles: String) {
        canvas.render()
        styleSheet.textContent = "\n$styles"

        svg.append(
            new(SVGConstants.SVG_DEFS_TAG)
                .append(arrowhead)
                .append(styleSheet)
        ).append(rootGroup)

        val file = File(fileName)
        val svgCont: String
        StringWriter().use { out ->
            SVGGraphics2D(doc).stream(svg, out, false, false)
            svgCont = out.buffer.toString()
        }
        val norm = svgCont.replace("&lt;", "<").replace("&gt;", ">")
        file.writeText(norm)
    }

    fun setImageSize(d: Dimension) {
        svg.set(SVGConstants.SVG_WIDTH_ATTRIBUTE to d.width)
            .set(SVGConstants.SVG_HEIGHT_ATTRIBUTE to d.height)
    }

    private val doc: Document = GenericDOMImplementation.getDOMImplementation()
        .createDocument(SVGConstants.SVG_NAMESPACE_URI, "svg", null)

    private val arrowhead = new(
        SVGConstants.SVG_MARKER_TAG,
        SVGConstants.SVG_ID_ATTRIBUTE to "arrowhead",
        SVGConstants.SVG_MARKER_WIDTH_ATTRIBUTE to 10,
        SVGConstants.SVG_MARKER_HEIGHT_ATTRIBUTE to 7,
        SVGConstants.SVG_REF_X_ATTRIBUTE to 0,
        SVGConstants.SVG_REF_Y_ATTRIBUTE to 3.5f,
        SVGConstants.SVG_ORIENT_ATTRIBUTE to "auto"
    ).append(new(SVGConstants.SVG_POLYGON_TAG, SVGConstants.SVG_POINTS_ATTRIBUTE to "0 0, 10 3.5, 0 7"))

    private val styleSheet = new(SVGConstants.SVG_STYLE_TAG)

    val rootGroup = new(SVGConstants.SVG_G_TAG, SVGConstants.SVG_ID_ATTRIBUTE to "rootGroup")
        .append(new(SVGConstants.SVG_RECT_TAG))                          // add background


    private val svg = doc.documentElement

    val wrapper: (id: String?, position: Point, dimension: Dimension, cssClasses: String) -> Element =
        { id, position, dimension, cssClasses ->
            val node = new(
                SVGConstants.SVG_SVG_TAG,
                SVGConstants.SVG_X_ATTRIBUTE to position.x,
                SVGConstants.SVG_Y_ATTRIBUTE to position.y,
                SVGConstants.SVG_WIDTH_ATTRIBUTE to dimension.width,
                SVGConstants.SVG_HEIGHT_ATTRIBUTE to dimension.height,
                SVGConstants.SVG_CLASS_ATTRIBUTE to cssClasses
            )
            id?.let { node.set(SVGConstants.SVG_ID_ATTRIBUTE to it) }
            node
        }

    val box: (
        type: Elements,
        id: String,
        name: String?,
        description: String?,
        position: Point,
        cssClasses: String
    ) -> Element = { type, id, name, description, position, cssClasses ->
        val node = wrapper(id, position, boxSize, cssClasses)
        val bg = new(SVGConstants.SVG_RECT_TAG).appendTo(node)
        var verOffset = 0

        if (type == Elements.ExtPerson || type == Elements.Person) {
            node.set(SVGConstants.SVG_Y_ATTRIBUTE to position.y - 40)
            node.set(SVGConstants.SVG_HEIGHT_ATTRIBUTE to boxSize.height + 40)
            verOffset = 60
            bg.set(SVGConstants.SVG_STYLE_ATTRIBUTE to "height:${boxSize.height - 10}px; width:${boxSize.width - 40}px; x:20px; y:50px")
            new(
                SVGConstants.SVG_CIRCLE_TAG,
                SVGConstants.SVG_R_ATTRIBUTE to 40,
                SVGConstants.SVG_CX_ATTRIBUTE to boxSize.width / 2,
                SVGConstants.SVG_CY_ATTRIBUTE to 40,
                SVGConstants.SVG_CLASS_ATTRIBUTE to cssClasses
            ).appendTo(node)
        }
        // node.append(bg)
        val txt = new(SVGConstants.SVG_FOREIGN_OBJECT_TAG, SVGConstants.SVG_STYLE_ATTRIBUTE to "y:${verOffset}px")
        txt.textContent =
            "\n" + """     <div class="boxElements" xmlns="http://www.w3.org/1999/xhtml">
                  |         <h2>${name ?: ""}</h2>
                  |         <p>[${type.svgLabel}]</p>
                  |         <p>${description ?: ""}</p>
                  |     </div>""".trimMargin() + "\n"
        txt.appendTo(node)
        node
    }

    var group: (id: String?, name: String?, position: Point, dimension: Dimension, children: List<SvgNode>, cssClasses: String) -> Element =
        { id, name, position, dimension, children, cssClasses ->
            val group = wrapper(id, position, dimension, cssClasses)

            name?.let {
                val txt = new(
                    SVGConstants.SVG_TEXT_TAG,
                    SVGConstants.SVG_X_ATTRIBUTE to 15,
                    SVGConstants.SVG_Y_ATTRIBUTE to dimension.width - baseFontSize,
                    SVGConstants.SVG_FONT_WEIGHT_ATTRIBUTE to "bold"
                )
                txt.textContent = it
                group.append(txt)
            }
            for (c in children) {
                group.append(c.render())
            }
            group
        }

    var Link: (id: String?, label: String?, labelPos: Point?, path: List<Point>, cssClasses: String) -> Element =
        { id, label, labelPos, path, cssClasses ->
            val group =
                wrapper(
                    id, path[0],
                    Dimension((path.first().x - path.last().x), (path.first().y - path.last().y)), cssClasses
                )
            group.append(
                new(
                    SVGConstants.SVG_PATH_TAG,
                    SVGConstants.SVG_ID_ATTRIBUTE to id,
                    SVGConstants.SVG_D_ATTRIBUTE to path
                )
            )
            label?.let {
                val txt = new(
                    SVGConstants.SVG_TEXT_TAG,
                    SVGConstants.SVG_X_ATTRIBUTE to (labelPos?.x ?: path[0].x),
                    SVGConstants.SVG_Y_ATTRIBUTE to (labelPos?.y ?: path[0].y)
                )
                txt.textContent = it
                group.append(txt)
            }
            group
        }
}
