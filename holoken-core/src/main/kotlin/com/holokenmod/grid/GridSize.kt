package com.holokenmod.grid

import org.apache.commons.lang3.StringUtils
import kotlin.math.max

data class GridSize(
    val width: Int,
    val height: Int
) {
    val surfaceArea: Int
        get() = width * height

    override fun toString(): String {
        return width.toString() + "x" + height
    }

    val amountOfNumbers: Int
        get() = max(width, height)

    val isSquare: Boolean
        get() = width == height

    companion object {
        @JvmStatic
        fun create(gridSizeString: String): GridSize {
            if (StringUtils.isNumeric(gridSizeString)) {
                val size = gridSizeString.toInt()
                return GridSize(size, size)
            }
            val parts = StringUtils.split(gridSizeString, "x")
            val width = parts[0].toInt()
            val height = parts[1].toInt()
            return GridSize(width, height)
        }
    }
}