package io.sh.model.parser

data class Dimensions(
    val width: Int,
    val height: Int
) {
    companion object {
        fun parse(dsl: Dsl): Dimensions {
            val str = dsl.getLine().substringAfter(space)

            if (str[0] != openingBracket || str[str.length - 1] != closingBracket) {
                throw Error("Unexpected formatting of dimensions: Please use '(width,height)' format.")
            }
            val dims = str.substring(1, str.length - 1).split(",").map{it.trim().toInt()}
            if (dims.size != 2) {
                throw Error("Unexpected formatting of dimensions: Please use '(width,height)' format.")
            }
            try {
                return Dimensions(dims[0], dims[1])
            } catch (nex: java.lang.NumberFormatException) {
                throw Error("Unexpected formatting of dimensions: ${nex.localizedMessage}")
            }
        }
    }
}
