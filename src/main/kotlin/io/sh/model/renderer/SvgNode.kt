package io.sh.model.renderer

import org.w3c.dom.Element

interface SvgNode {
    fun render(): Element
}
