package io.sh

class Matrix(width: Int, height: Int) {
    private val matrix = Array(width) { BooleanArray(height) { false } }

    fun occupy(x: Int,y: Int, width: Int, height: Int){
        for (i in y .. height){
            for (j in x .. width){
                matrix[j][i] = true
            }
        }
    }
}
