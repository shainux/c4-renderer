package io.sh.model.parser

/**
 * Examples
 * views {
 *      view_id = systemlandscape "SystemLandscape" {
 *           include *
 *      }
 * }
 * views {
 *      systemlandscape {
 *           name Some_Name
 *           include [element_id1 element_id2 ]
 *           description "some_descritpion"
 *      }
 * }
 */
data class View(
    var id: String?,
    var name: String?=null,
    val type: ViewTypes,
    var description: String?=null,
    var include: List<String>? = null
){
    companion object{
        fun parse(dsl: Dsl): View {
            val tknLine = TokenLine.parse(dsl.getLine())
            val viewType = ViewTypes.forLabel(tknLine.element)
            val v = View(tknLine.id, tknLine.name, viewType, tknLine.description)
            if (dsl.getLine().endsWith(openingToken)) {
                while (dsl.hasNext() && dsl.nextLine().isNotClosingToken()) {
                    when (dsl.getLine().openingWord()) {
                        idTkn -> v.id = dsl.getString(true)
                        nameTkn -> v.name = dsl.getString()
                        descriptionTkn -> v.description = dsl.getString()
                        includeTkn -> v.include = dsl.getListOfStrings(openingSquareBracket, closingSquareBracket)
                        else -> throw Error("Unexpected token.")
                    }
                }
                if (dsl.getLine().isNotClosingToken()) {
                    throw Error("Unexpected EOF: Unterminated element.")
                }
            }
            return v
        }
    }
}
