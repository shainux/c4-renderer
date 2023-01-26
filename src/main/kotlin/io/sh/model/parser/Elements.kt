package io.sh.model.parser

/**
 * @param label     - DSL spelling
 * @param isNode    - true to render it as node, false to render it as group (boundary around group of nodes)
 */
enum class Elements (val label:String, val svgLabel: String, val cssClasses: String) {
    Workspace("workspace", "Workspace", "workspace"),
    Enterprise("enterprise", "Enterprise","enterprise"),
    Group("group", "Group", "group"),
    Person("person", "Person", "person"),
    ExtPerson("externalPerson", "Person", "person ext"),
    SoftwareSystem("softwareSystem", "Software System", "system"),
    ExtSystem("externalSystem", "External System", "system ext"),
    Container("container", "Container", "container"),
    DeploymentEnvironment("deploymentEnvironment", "Deployment Environment", "deployment"),
    Component("component", "Component", "component"),
    Element("element", "Element", "element");
    companion object{
        fun forLabel(lbl: String) = Elements.values().find{it.label == lbl}
            ?: throw Error("Unexpected token $lbl. One of elements expected.")
    }
}
