package io.sh.model.parser

data class ChartNode(
    val type: Elements,                                // type of element (person, group, container, etc.)
    var id: String? = null,
    var name: String? = null,
    var description: String? = null,
    var tags: List<String>? = null,                    // can be one word, comma separated. E.g.: [abc, abc_de, fd]
    var url: String? = null,                           // url to attach to the element
    var properties: Map<String, String>? = null,       // set of random key-value pairs
    var perspectives: List<Perspective>? = null,       // additional details for different views
    var relationships: List<Relationship>? = null,
    var docs: List<String>?= null,                     // paths to docs (MD)
    var adrs: List<String>?= null,                     // paths to ADRs (MD)
    var children: MutableList<ChartNode> = mutableListOf()
) {
    companion object {
        fun parse(dsl: Dsl): ChartNode {
            val tknLine = TokenLine.parse(dsl.getLine())
            val element = Elements.forLabel(tknLine.element)
            val el = ChartNode(element, tknLine.id, tknLine.name, tknLine.description)
            if (dsl.getLine().endsWith(openingToken)) {
                while (dsl.hasNext() && dsl.nextLine().isNotClosingToken()) {
                    if(dsl.getLine().isBlank()){continue}
                    when (dsl.getLine().openingWord()) {
                        idTkn -> el.id = dsl.getString(true)
                        nameTkn -> el.name = dsl.getString()
                        descriptionTkn -> el.description = dsl.getString()
                        tagsTkn -> el.tags = dsl.getListOfStrings(openingSquareBracket, closingSquareBracket)
                        urlTkn -> el.url = dsl.getString()
                        propertiesTkn -> el.properties = dsl.getProperties()
                        perspectivesTkn -> el.perspectives = dsl.parseList(Perspective.Companion::parse)
                        relationshipsTkn -> el.relationships = dsl.parseList(Relationship.Companion::parse)
                        docsTkn -> el.docs = dsl.getListOfStrings(openingSquareBracket, closingSquareBracket)
                        adrsTkn -> el.adrs = dsl.getListOfStrings(openingSquareBracket, closingSquareBracket)
                        else ->  el.children.add(parse(dsl))        // will throw an error in case of unexpected token
                    }
                }
                if (dsl.getLine().isNotClosingToken()) {
                    throw Error("Unexpected EOF: Unterminated element.")
                }
            }
            return el
        }
    }
}
