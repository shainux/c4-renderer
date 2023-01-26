package io.sh.model.parser

data class Dsl(
    private val lines: List<String>,
    private val stack: List<String>
){
    // current position in lines
    private var cursor: Int = 0

    fun getStackTrace() = stack[cursor]

    fun nextLine():String = lines[++cursor]

    fun hasNext() = cursor<lines.size

    fun getLine(idx: Int = cursor) = lines[idx]
    fun getLastLine() = lines.last()

    fun getString(wordCharsOnly: Boolean = false): String{
        var str = this.getLine().substringAfter(space)

        if(str[0] == quote) {                                       // string with quotes
            var lastQuotePos = str.indexOfLast{it == quote }
            if(lastQuotePos == 0) {                                 // multi-line string with quotes
                while (this.hasNext() && !this.nextLine().contains(quote)) {
                    str += " ${this.getLine()}"

                }
                if(!this.getLine().contains(quote) && this.hasNext() ){
                    // we reached EOF with open string
                    throw Error("Unexpected EOF: unfinished string expression.")
                }
                str += " ${this.getLine()}"                 // we need to add last quoted line
            }
            lastQuotePos = str.indexOfLast{it == quote }
            if(lastQuotePos!=str.length-1){                     // extra text after the quote
                throw Error("Unexpected token '${str[lastQuotePos+1]}' at pos. ${lastQuotePos+1}: End of line expected.")
            }
        }
        val res = str.trim(quote)
        if(wordCharsOnly && !res.hasWordCharactersOnly()){
            throw Error("String '$res' contains illegal characters.")
        }
        return res
    }

    /**
     * Examples:
     * properties {key1 val1 key2 val2}
     * properties {
     *      key1 val1
     *      key2 val2
     * }
     */
    fun getProperties(): Map<String,String>{
        val strings = ArrayDeque(getListOfStrings())
        val res = mutableMapOf<String, String>()
        while (strings.isNotEmpty()){
            res[strings.removeFirst()] = strings.removeFirst()
        }
        return res
    }

    /**
     * Examples:
     * docs {uri1 uri2 uri3}
     * properties {
     *      uri1
     *      uri2
     * }
     */
    fun getListOfStrings(openingTkn: String = openingToken, closingTkn: String = closingToken): List<String>{
        val openingIdx = this.getLine().indexOf(openingTkn)
        if(openingIdx<0){
            throw Error("Unexpected character: please use { itm1 itm2 itm3 } (for objects) or [ itm1 itm2 itm3 ] (for lists) format")
        }
        var str = this.getLine().substring(openingIdx+1)
        val closingIdx = str.indexOf(closingTkn)

        if(closingIdx>0){
            str = str.substring(0, closingIdx)
        } else {
            while(this.hasNext() && !this.nextLine().contains(closingTkn)){
                str += " ${this.getLine()}"
            }
            if(!this.getLine().contains(closingTkn)){
                throw Error("Unexpected EOF: unterminated element.")
            }
        }

        return str.splitToWordsAndSubstrings()
    }

    /**
     * Examples
     * path [(x,y,x) (x,y,z) (x,y,z)]
     * path [(x,y,x), (x,y,z), (x,y,z)]
     */
    fun getPath(): List<Coordinate>{
        val line = this.getLine()
        return Regex("\\((.*?)\\)")
            .findAll(line, line.indexOf(" "))
            .toList()
            .map{ Coordinate.fromString(it.groupValues.last()) }
    }

    fun parseModel(): List<ChartNode> {
        val elements = mutableListOf<ChartNode>()
        while(this.hasNext() && this.nextLine().isNotClosingToken()){
            if(this.getLine().isBlank()){continue}
            elements.add(ChartNode.parse(this))
        }
        return elements
    }

    fun <T> parseList(parsingMethod: (dsl: Dsl) -> T):List<T>{
        if(this.getLine().isOneLineElement()) {                         // check if opening token is there and no closing
            return emptyList()
        }

        val list = mutableListOf<T>()

        while(this.hasNext() && this.nextLine().isNotClosingToken()) {
            list.add(parsingMethod(this))
        }

        if(this.getLine().isNotClosingToken()){
            throw Error("Unexpected EOF: unterminated element.")
        }
        return list
    }

    fun parseViews(): List<View>{
        val views = mutableListOf<View>()
        while(this.hasNext() && this.nextLine().isNotClosingToken()){
            views.add(View.parse(this))
        }
        return views
    }
}
