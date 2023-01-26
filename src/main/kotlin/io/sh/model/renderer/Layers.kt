package io.sh.model.renderer/*
package io.sh.renderer.model

import io.sh.exception.UnexpectedTokenException
import io.sh.parser.model.ChartElement
import io.sh.parser.model.Elements
import io.sh.parser.model.Relationship
import java.util.LinkedList

data class Layers(
    val enterprise: MutableList<SvgNode> = mutableListOf(),
    val enterpriseGroup: MutableList<SvgNode> = mutableListOf(),
    val system: MutableList<SvgNode> = mutableListOf(),
    val systemGroup: MutableList<SvgNode> = mutableListOf(),
    val container: MutableList<SvgNode> = mutableListOf(),
    val containerGroup: MutableList<SvgNode> = mutableListOf(),
    val component: MutableList<SvgNode> = mutableListOf(),
    val componentGroup:MutableList<SvgNode> = mutableListOf(),
    val element: MutableList<SvgNode> = mutableListOf(),
    val elementGroup:MutableList<SvgNode> = mutableListOf(),
    val connections: MutableMap<String, String> = mutableMapOf()
){
    fun addElement(el: ChartElement){
        val id = el.id?: "${el.type.name}_${System.nanoTime()}"
        el.relationships?.let{addConnections(id, it)}
        if(el.type.isNode){
            val node = Node(el.type.svgLabel, id, el.name, el.description)
            when (el.type){
                Elements.SoftwareSystem -> system.add(node)
                Elements.Container -> container.add(node)
                Elements.Component -> container.add(node)
                Elements.Element -> container.add(node)
                else -> throw UnexpectedTokenException("Cannot place node (${el.id ?: el.name ?: "unspecified"}")
            }
        } else {
            val gr = Group(id, el.name, el.description)
            when (el.type){
                Elements.DeploymentEnvironment -> TODO("Not implemented")
                Elements.Enterprise -> enterprise.add(gr)
                Elements.Group -> {
                    if(el.children.isEmpty()){ return }
                    val grType = el.children[0].type
                    if(el.children.any{ it.type != grType }){
                        throw IllegalArgumentException("Group can contain only elements of one type ($grType).")
                    }
                    when (grType){
                        Elements.Enterprise -> enterpriseGroup.add(gr)
                        Elements.SoftwareSystem -> systemGroup.add(gr)
                        Elements.Container -> containerGroup.add(gr)
                        Elements.Component -> componentGroup.add(gr)
                        Elements.Element -> elementGroup.add(gr)
                        else -> throw UnexpectedTokenException("Cannot place group (${el.id ?: el.name ?: "unspecified"}")
                    }
                }
                else -> throw UnexpectedTokenException("Cannot place ${el.type.label} (${el.id ?: el.name ?: "unspecified"}")
            }
        }
    }

    fun getAll():LinkedList<SvgNode>{
        val res = LinkedList<SvgNode>()
        res.addAll(elementGroup)
        res.addAll(element)
        res.addAll(componentGroup)
        res.addAll(component)
        res.addAll(containerGroup)
        res.addAll(container)
        res.addAll(systemGroup)
        res.addAll(system)
        res.addAll(enterpriseGroup)
        res.addAll(enterprise)
        return res
    }

    fun countAllNodes(): Int = element.size+component.size+container.size+system.size+enterprise.size

    private fun addConnections(id: String, relationships: List<Relationship>){
        for (r in relationships){
            r.
        }
    }
}
*/
