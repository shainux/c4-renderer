package io.sh.extension

import org.w3c.dom.Element

fun Element.set(attribute: Pair<String, Any>): Element {
    this.setAttributeNS(null, attribute.first, attribute.second.toString())
    return this
}
fun Element.append(child: Element): Element {
    this.appendChild(child)
    return this
}

fun Element.appendTo(parent:Element): Element {
    parent.appendChild(this)
    return this
}
