package io.sh.model.parser

import java.awt.Point

data class Coordinate(
    val x: Int,
    val y: Int,
    val z: Int? = null
) {
    companion object {
        fun parse(dsl: Dsl): Coordinate {
            val str = dsl.getLine().substringAfter(space)

            if (str[0] != openingBracket || str[str.length - 1] != closingBracket) {
                throw Error("Unexpected formatting of dimensions: Please use '(x,y[,z])' format.")
            }
            try {
                val dims = str.substring(1, str.length - 1).split(",").map{it.trim().toInt()}
                if (dims.size != 2 && dims.size != 3) {
                    throw Error("Unexpected formatting of dimensions: Please use '(x,y[,z])' format.")
                }
                return Coordinate(dims[0], dims[1], dims.getOrNull(2))
            } catch (nex: java.lang.NumberFormatException) {
                throw Error("Unexpected formatting of dimensions: ${nex.localizedMessage}")
            }
        }

        fun fromString(str: String): Coordinate {
            val vertices = str.split(coordsSeparator).map(String::toInt)
            return when (vertices.size) {
                2 -> Coordinate(vertices[0],vertices[1])
                3 -> Coordinate(vertices[0], vertices[1], vertices[2])
                else -> throw Error("Malformed coordinates.")
            }
        }
    }

    fun toPoint() = Point(x, y)
}
