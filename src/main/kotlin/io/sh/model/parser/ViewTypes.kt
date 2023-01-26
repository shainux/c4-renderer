package io.sh.model.parser

enum class ViewTypes(val label:String, val svgLabel: String) {
    SystemLandscape("systemlandscape", "System Landscape"),
    SystemContext("systemcontext", "System Context"),
    Container("container", "Container Diagram"),
    Component("component", "Component Diagram");
    companion object{
        fun forLabel(lbl: String) = values().find{it.label == lbl}
            ?: throw Error("Unexpected token $lbl. One of view types expected.")
    }
}
