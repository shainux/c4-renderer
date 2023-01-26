package io.sh.model.parser

data class TokenLine (
    val element: String,
    val id: String? = null,
    var name: String? = null,
    val description: String? = null
){
    companion object{
        fun parse(line:String): TokenLine {
            var parts = line.splitToWordsAndSubstrings()
            val assignmentPosition = parts.indexOf(assignmentOperator)
            var isAssignment = false
            if(assignmentPosition != -1){
                if(assignmentPosition == 1 && parts[2].hasWordCharactersOnly()) {
                    isAssignment = true
                } else {
                    throw Error("Invalid assignment string. Please use 'id = token [details] format.")
                }
            }

            if(!parts[0].hasWordCharactersOnly()){
                throw Error("ID or token '${parts[0]}' contain invalid characters.")
            }

            parts = parts.filter { !(it.length == 1 && blockDelimiters.contains(it[0].code)) }

            return if(isAssignment){
                TokenLine(id=parts[0], element = parts[2], name = parts.getOrNull(3), description = parts.getOrNull(4))
            } else {
                TokenLine(element = parts[0], name = parts.getOrNull(1), description = parts.getOrNull(2))
            }
        }
    }
}
