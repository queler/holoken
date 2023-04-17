package com.holokenmod.creation

import com.holokenmod.grid.GridSize
import com.holokenmod.options.CurrentGameOptionsVariant.instance
import com.holokenmod.options.DigitSetting
import com.holokenmod.options.GameOptionsVariant.Companion.createClassic
import com.holokenmod.options.GameVariant
import com.holokenmod.options.GridCageOperation
import com.holokenmod.options.SingleCageUsage
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class TestGridCreator {
    //TODO
    @Disabled
    @Test
    fun test3x3GridCreationWithoutRandomValues() {
        instance.digitSetting = DigitSetting.FIRST_DIGIT_ONE
        instance.showOperators = true
        instance.singleCageUsage = SingleCageUsage.FIXED_NUMBER
        instance.cageOperation = GridCageOperation.OPERATIONS_ALL
        val creator = GridCreator(
            RandomizerMock(),
            ShufflerStub(),
            GameVariant(
                GridSize(3, 3),
                createClassic()
            )
        )
        val grid = creator.createRandomizedGridWithCages()
        MatcherAssert.assertThat(grid.getCellAt(0, 0).value, CoreMatchers.`is`(1))
        MatcherAssert.assertThat(grid.getCellAt(0, 1).value, CoreMatchers.`is`(2))
        MatcherAssert.assertThat(grid.getCellAt(0, 2).value, CoreMatchers.`is`(3))
        MatcherAssert.assertThat(grid.getCellAt(1, 0).value, CoreMatchers.`is`(2))
        MatcherAssert.assertThat(grid.getCellAt(1, 1).value, CoreMatchers.`is`(3))
        MatcherAssert.assertThat(grid.getCellAt(1, 2).value, CoreMatchers.`is`(1))
        MatcherAssert.assertThat(grid.getCellAt(2, 0).value, CoreMatchers.`is`(3))
        MatcherAssert.assertThat(grid.getCellAt(2, 1).value, CoreMatchers.`is`(1))
        MatcherAssert.assertThat(grid.getCellAt(2, 2).value, CoreMatchers.`is`(2))
    }
}