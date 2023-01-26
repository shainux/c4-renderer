package io.sh.model.parser

/**
 * Examples:
 * relationships{
 *   this -> some_id [label]
 * }
 *
 * relationships{
 *   this -> some_id {
 *      path [(x,y,x) (x,y,z) (x,y,z)]
 *      label some label <- visible label on the chart
 *      labelPosition (x,y,z)
 *      description "some description" <- hint to the connection
 *   }
 * }
 */
data class Relationship(
    var from: String,
    var to: String,
    var path: List<Coordinate>? = null,
    var label: String? = null,
    var description: String? = null,
    var labelPosition: Coordinate? = null
){
    companion object{
        fun parse(dsl: Dsl): Relationship {
            val firstLine = dsl.getLine().replace(relationTkn, relationOperator).splitToWordsAndSubstrings()
            if (!(firstLine.size > 2 && firstLine[1] == relationOperator)){
                throw Error("Cannot parse relationship. Please use 'id1 -> id2 [label]' format")
            }

            val r = Relationship(from = firstLine[0], to = firstLine[2], label = firstLine.getOrNull(3))

            if(firstLine.last() != openingToken){
                return r
            }

            while(dsl.hasNext() && dsl.nextLine().isNotClosingToken()) {
                when(dsl.getLine().openingWord()){
                    labelTkn -> r.label = dsl.getString()
                    descriptionTkn -> r.description = dsl.getString()
                    labelPositionTkn -> r.labelPosition = Coordinate.parse(dsl)
                    pathTkn -> r.path = dsl.getPath()
                }
            }
            if (dsl.getLine().isNotClosingToken()) {
                throw Error("Unexpected EOF: Unterminated element.")
            }
            return r
        }
    }
}
