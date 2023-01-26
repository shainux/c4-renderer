@file:Suppress("UnstableApiUsage")

package io.sh

import com.google.common.graph.*
import io.sh.exception.FileNotFoundException
import io.sh.model.parser.*
import io.sh.model.renderer.*
import org.apache.batik.util.SVGConstants.*

const val resourcePath = "/dsl/"

object ChartService {

    fun renderChart(dslFile: String = "workspace.dsl", styleSheet: String = "styles.css") {
        val script = readDslScript(dslFile)
        val styles = readChartStyles(styleSheet)
        val workspace = Workspace.parse(Dsl(script.script, script.stack))
        workspace.lastModified = script.lastModified
        val chartData = modelToGraph(workspace.model)
        addRelationsToGraph(chartData)
        chartData.graph?.let{
            Renderer.storeToFile("output.svg", Canvas.from(workspace, Renderer.calculateLayout(it)), styles)
        }

    }
    private fun readChartStyles(filename: String): String = this
        .javaClass.getResource("$resourcePath$filename")?.readText()
            ?: throw FileNotFoundException(filename)

    @Suppress("MemberVisibilityCanBePrivate")                                       // is used in tests
    fun readDslScript(filename: String): Script {
        val file = this.javaClass.getResource("$resourcePath$filename")?.openConnection()
            ?: throw FileNotFoundException(filename)
        var lastModified = file.lastModified
        val cnt = file.getInputStream()
        val script = mutableListOf<String>()
        val stack = mutableListOf<String>()
        for ((lineCounter, line) in cnt.bufferedReader().readLines().withIndex()) {
            val stackPos = "$filename, line ${lineCounter + 1}"
            val trimmedLine = line.trim()
            if (trimmedLine.startsWith(include)) {
                val data = readDslScript(line.substringAfter(include).trim())
                script.addAll(data.script)
                stack.addAll(data.stack.map { "$stackPos -> $it" })
                if (lastModified < data.lastModified) {
                    lastModified = data.lastModified
                }
            } else {
                if (!trimmedLine.startsWith(comment)) {
                    script.add(trimmedLine)
                    stack.add(stackPos)
                }
            }
        }
        return Script(script, stack, lastModified)
    }

    private fun modelToGraph(model: List<ChartNode>): ChartData {
        val nw = NetworkBuilder
            .directed()
            .nodeOrder(ElementOrder.insertion<Node>())
            .allowsParallelEdges(true)
            .build<Node, Link>()

        val rootNode = Node(Elements.Workspace, "root", listOf("root"))
        val relations = ArrayDeque<Relationship>()
        val nodes = mutableMapOf<String, Node>()
        for (e in model) {
            val data = addChartNodeToGraph(e, rootNode, nw)
            nodes.putAll(data.nodes)
            relations.addAll(data.relations)
        }
        return ChartData(nodes, relations, nw)
    }

    private fun addChartNodeToGraph(chartNode: ChartNode, parent: Node, nw: MutableNetwork<Node, Link>): ChartData {
        val node = Node.from(chartNode, parent.slug)
        nw.addEdge(parent, node, Link(id = "${parent.id}_includes_${node.id}" ,belongsToGroupLink = true))
        val relations = ArrayDeque<Relationship>()

        val nodes = mutableMapOf(node.id to node)
        chartNode.relationships?.let { rs ->
            for (r in rs) {
                if (r.from == "this") {
                    r.from = node.id
                }
                if (r.to == "this") {
                    r.to = node.id
                }
                if (r.from != r.to) {
                    relations.add(r)
                }
            }
        }
        for (ch in chartNode.children) {
            val data = addChartNodeToGraph(ch, node, nw)
            relations.addAll(data.relations)
            nodes.putAll(data.nodes)
        }
        return ChartData(nodes, relations)
    }

    private fun addRelationsToGraph(chartData: ChartData) {
        for (r in chartData.relations) {
            val from = chartData.nodes[r.from] ?: throw IllegalArgumentException("Invalid relation format")
            val to = chartData.nodes[r.to] ?: throw IllegalArgumentException("Invalid relation format")
            chartData.graph?.addEdge(from, to, Link.from(r))
        }
    }

    data class ChartData(
        val nodes: Map<String, Node>,
        val relations: ArrayDeque<Relationship>,
        val graph: MutableNetwork<Node, Link>? = null
    )
}
