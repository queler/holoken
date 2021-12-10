package com.srlee.dlx;

import com.holokenmod.Grid;
import com.holokenmod.GridCage;
import com.holokenmod.creation.GridCageCreator;
import com.holokenmod.options.DigitSetting;
import com.holokenmod.options.GameVariant;

import java.util.ArrayList;
import java.util.Collection;

public class MathDokuDLX extends DLX {
	
	public MathDokuDLX(final Grid grid) {
		
		final int boardSize = grid.getGridSize();
		final int BOARD2 = boardSize * boardSize;
		
		// Number of columns = number of constraints =
		//		BOARD * BOARD (for columns) +
		//		BOARD * BOARD (for rows)	+
		//		Num cages (each cage has to be filled once and only once)
		// Number of rows = number of "moves" =
		//		Sum of all the possible cage combinations
		// Number of nodes = sum of each move:
		//      num_cells column constraints +
		//      num_cells row constraints +
		//      1 (cage constraint)
		int total_nodes = 0;
		
		final Collection<GridCageCreator> creators = new ArrayList<>();
		
		for (final GridCage cage : grid.getCages()) {
			creators.add(new GridCageCreator(grid, cage));
		}
		
		for (final GridCageCreator creator : creators) {
			total_nodes += creator.getPossibleNums().size() * (2 * creator.getNumberOfCells() + 1);
		}
		Init(2 * BOARD2 + creators.size(), total_nodes);
		
		int constraint_num;
		int move_idx = 0;
		
		DigitSetting digitSetting = GameVariant.getInstance().getDigitSetting();
		
		for (final GridCageCreator creator : creators) {
			for (final int[] onemove : creator.getPossibleNums()) {
				for (int i = 0; i < onemove.length; i++) {
					int numberToTestIndex = onemove[i];
					
					if (digitSetting == DigitSetting.FIRST_DIGIT_ONE) {
						numberToTestIndex--;
					}
					
					constraint_num = boardSize * numberToTestIndex + creator.getCell(i)
							.getColumn() + 1;
					AddNode(constraint_num, move_idx);    // Column constraint
					constraint_num = BOARD2 + boardSize * numberToTestIndex + creator.getCell(i)
							.getRow() + 1;
					AddNode(constraint_num, move_idx);    // Row constraint
				}
				constraint_num = 2 * BOARD2 + creator.getId() + 1;
				AddNode(constraint_num, move_idx);    // Cage constraint
				move_idx++;
			}
		}
	}
}