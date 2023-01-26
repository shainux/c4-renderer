package io.sh

import io.sh.extension.append
import io.sh.model.renderer.*
import org.apache.batik.anim.dom.SAXSVGDocumentFactory
import org.apache.batik.anim.dom.SVGDOMImplementation
import org.apache.batik.svggen.SVGGraphics2D
import org.apache.batik.svggen.SVGSyntax
import org.apache.batik.util.XMLResourceDescriptor
import org.junit.jupiter.api.Test
import org.w3c.dom.DOMImplementation
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter


class BatikTest {

    @Test
    fun batikTest() {
        val svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI
        val doc: Document = SVGDOMImplementation
            .getDOMImplementation()
            .createDocument(svgNS, "svg", null)
        val defs = doc.createElementNS(svgNS, "defs")
        defs.setAttributeNS(null, "id", "generic def")
        val styleSheet = doc.createElementNS(svgNS, SVGSyntax.SVG_STYLE_TAG)
        styleSheet.textContent = "rect { fill: red; stroke: blue; stroke-width: 3 }"
        defs.append(styleSheet)

// get the root element (the svg element)
        val svgRoot: Element = doc.documentElement
        svgRoot.append(defs)
//        val pi= doc.createProcessingInstruction("xml-stylesheet", """type="text/css" href="myStyles.css"""")
//        doc.insertBefore(pi, svgRoot)

// set the width and height attribute on the root svg element

// set the width and height attribute on the root svg element
        svgRoot.setAttributeNS(null, "width", "400")
        svgRoot.setAttributeNS(null, "height", "450")

// create the rectangle

// create the rectangle
        val rectangle: Element = doc.createElementNS(svgNS, "rect")
        rectangle.setAttributeNS(null, "x", "10")
        rectangle.setAttributeNS(null, "y", "20")
        rectangle.setAttributeNS(null, "width", "100")
        rectangle.setAttributeNS(null, "height", "50")
        rectangle.setAttributeNS(null, "style", "fill:red")

// attach the rectangle to the svg root element

// attach the rectangle to the svg root element
        svgRoot.appendChild(rectangle)


        OutputStreamWriter(FileOutputStream("output.svg"), "UTF-8").use { out ->
            SVGGraphics2D(doc).stream(doc.documentElement, out, false, false)
        }
    }
}
