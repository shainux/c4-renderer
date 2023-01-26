package io.sh.model.parser

import io.sh.exception.DslFormatException

data class Workspace(
    var name: String? = null,
    var description: String? = null,
    var dimensions: Dimensions? = null,
    var model: List<ChartNode> = emptyList(),
    var views: List<View>? = null,
    var version: String? = null
) {
    var lastModified: Long = System.currentTimeMillis()
    companion object {

        fun parse(dsl: Dsl): Workspace {
            try {
                val tknLine = TokenLine.parse(dsl.getLine())
                if (tknLine.element != Elements.Workspace.label) {
                    throw Error("Unexpected start of the file '${dsl.getLine()}'. Workspace not found.")
                }
                if (dsl.getLastLine().isNotClosingToken()) {
                    throw DslFormatException("Unexpected closing line `${dsl.getLastLine()}`. Closing bracket expected")
                }
                val ws = Workspace(name = tknLine.name, description = tknLine.description)
                while (dsl.hasNext() && dsl.nextLine().isNotClosingToken()) {
                    if(dsl.getLine().isBlank()){
                        continue
                    }
                    when (dsl.getLine().openingWord()) {
                        nameTkn -> ws.name = dsl.getString()
                        descriptionTkn -> ws.description = dsl.getString()
                        dimensionsTkn -> ws.dimensions = Dimensions.parse(dsl)
                        modelTkn -> ws.model = dsl.parseModel()
                        viewsTkn -> ws.views = dsl.parseViews()
                        versionTkn -> ws.version = dsl.getString()
                        else -> throw Error("Unexpected token ${dsl.getLine().openingWord()}.")
                    }
                }
                return ws
            } catch (err: Error) {
                throw DslFormatException("${err.message} (${dsl.getStackTrace()})")
            }
        }
    }
}
