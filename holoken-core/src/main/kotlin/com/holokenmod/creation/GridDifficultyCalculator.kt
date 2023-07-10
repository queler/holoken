package com.holokenmod.creation

import com.holokenmod.creation.cage.GridSingleCageCreator
import com.holokenmod.grid.Grid
import com.holokenmod.options.GameDifficulty
import com.holokenmod.options.GameDifficultyRatings
import com.holokenmod.options.GameVariant
import mu.KotlinLogging
import java.math.BigInteger
import kotlin.math.ln
import kotlin.math.roundToLong

private val logger = KotlinLogging.logger {}

class GridDifficultyCalculator(private val grid: Grid) {
    fun calculate(): Double {
        var difficulty = BigInteger.ONE
        for (cage in grid.cages) {
            val cageCreator = GridSingleCageCreator(grid.variant, cage)
            difficulty =
                difficulty.multiply(BigInteger.valueOf(cageCreator.possibleNums.size.toLong()))
        }
        val value = ln(difficulty.toDouble())

        logger.info { "difficulty: $value" }

        return value
    }

    fun info(): String {
        val difficultyValue = calculate().roundToLong().toDouble()
        return difficultyValue.roundToLong().toString()
    }

    val isGridVariantSupported: Boolean
        get() = isSupported(grid.variant)

    val difficulty: GameDifficulty
        get() = getDifficulty(calculate())

    private fun getDifficulty(difficultyValue: Double): GameDifficulty {
        val ratings = GameDifficultyRatings.byVariant(grid.variant)
            ?: return GameDifficulty.VERY_EASY

        return ratings.getDifficulty(difficultyValue)
    }

    companion object {
        fun isSupported(variant: GameVariant): Boolean {
            return GameDifficultyRatings.isSupported(variant)
        }
    }
}
