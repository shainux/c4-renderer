package io.sh.model.renderer

import java.awt.Dimension
import java.text.SimpleDateFormat

val boxSize = Dimension(240, 120)
const val boxPadding = 50

//const val aspectRatio = 1.6f                    // 16:10
const val layoutRatio = 2f

const val pattern = "MM.dd.yyyy"
val df = SimpleDateFormat(pattern)

const val baseFontSize = 12
