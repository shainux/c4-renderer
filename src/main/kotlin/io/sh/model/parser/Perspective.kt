package io.sh.model.parser

/**
 * Examples:
 * perspectives {
 *      view_id {
 *          label some label
 *          description "some description"
 *          position (x, y, z)
 *          dimension (width, height)
 *          relationships{
 *              this -> some_id
 *              path [(x,y,x) (x,y,z) (x,y,z)]
 *              label some label <- visible label on the chart
 *              labelPosition (x,y,z)
 *              description "some description" <- hint to the connection
 *           }
 *       }
 * }
 */
data class Perspective(
    val viewId: String,                                           // id of a view to which the perspective is created
    var label: String? = null,                                    // name of the element in the perspective
    var description: String? = null,
    var position: Coordinate? = null,                            // position of the visual element
    var dimensions: Dimensions? = null,                           // size of the visual element
    var relationships: List<Relationship>? = null
){
    companion object{
        fun parse(dsl: Dsl): Perspective {
            val vid = dsl.getLine().openingWord()
            if(!vid.hasWordCharactersOnly()){
                throw Error("View ID must consist of Alphanumeric characters and '_' only.")
            }
            val p = Perspective(viewId = vid)
            while(dsl.hasNext() && dsl.nextLine().isNotClosingToken()) {
                when(dsl.getLine().openingWord()){
                    labelTkn -> p.label = dsl.getString()
                    descriptionTkn -> p.description = dsl.getString()
                    positionTkn -> p.position = Coordinate.parse(dsl)
                    dimensionsTkn -> p.dimensions = Dimensions.parse(dsl)
                    relationshipsTkn -> p.relationships = dsl.parseList(Relationship.Companion::parse)
                }
            }
            if (dsl.getLine().isNotClosingToken()) {
                throw Error("Unexpected EOF: Unterminated element.")
            }
            return p
        }
    }
}
