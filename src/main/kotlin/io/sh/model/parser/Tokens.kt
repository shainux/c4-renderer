package io.sh.model.parser

const val include = "!include"
const val openingToken = "{"
const val closingToken = "}"
const val openingBracket = '('
const val closingBracket = ')'
const val openingSquareBracket = "["
const val closingSquareBracket = "]"
const val space = ' '
const val quote = '"'
const val comment = "//"
const val idTkn = "id"
const val nameTkn = "name"
const val descriptionTkn = "description"
const val dimensionsTkn = "dimensions"
const val modelTkn = "model"
const val viewsTkn = "views"
const val positionTkn = "position"
const val labelPositionTkn = "labelPosition"
const val tagsTkn = "tags"
const val urlTkn = "url"
const val propertiesTkn = "properties"
const val perspectivesTkn = "perspectives"
const val relationshipsTkn = "relationships"
const val docsTkn = "docs"
const val adrsTkn = "adrs"
const val coordsSeparator = ','
const val labelTkn = "label"
const val pathTkn = "path"
const val includeTkn = "include"
const val relationTkn = "->"
const val versionTkn = "version"

const val assignmentOperator = "="
const val relationOperator = "→"
const val stringOperator = "\""

val blockDelimiters = listOf(openingToken[0].code, closingToken[0].code, '['.code, ']'.code, '('.code, ')'.code)
val operators = listOf(assignmentOperator[0].code, relationOperator[0].code)
val specials = blockDelimiters + operators
val delimiters = listOf(' '.code, ','.code, ':'.code, ';'.code)+ specials

/**
 * Extracts the first word of the element description
 */
fun String.openingWord() = listOf(
    this.indexOf(space),
    this.indexOf(openingToken),
    this.indexOf(assignmentOperator)
).filter { it > 0 }.minOrNull()?.let{ this.substring(0, it) }?:this

/**
 * Checks if the string is not "}"
 */
fun String.isNotClosingToken(): Boolean = this != closingToken

/**
 * Checks if the string contains only alphabet characters, numbers and '_' (no spaces)
 */
fun String.hasWordCharactersOnly() = this.isNotBlank() && this.matches("^\\w*$".toRegex())

/**
 * Checks if the element description as only one line (no '{' or there is also '}')
 */
fun String.isOneLineElement(): Boolean = this.indexOf(openingToken)<0 || this.contains(closingToken)

/**
 * Splits string by spaces, comas, equals, semicolons and quoted substrings
 */
fun String.splitToWordsAndSubstrings(): List<String>{
    val quote = stringOperator[0].code
    val str = this.chars().iterator()
    var subStr = ""
    val list = mutableListOf<String>()
    var waitForSecondQuote = false
    while(str.hasNext()) {
        val next = str.next()
        if (next == quote) {
            if (waitForSecondQuote) {
                list.add(subStr)
                subStr = ""
                waitForSecondQuote = false
            } else {
                waitForSecondQuote = true
            }
            continue
        }
        if (waitForSecondQuote) {
            subStr += Char(next)
        } else {
            if (delimiters.contains(next)) {
                if(subStr.isNotBlank()) {
                    list.add(subStr)
                }
                subStr = ""
            } else {
                subStr += Char(next)
            }
            if(specials.contains(next)){
                list.add(Char(next).toString())
            }
        }
    }
    if(subStr.isNotBlank()){
        list.add(subStr)
    }
    return list
}
