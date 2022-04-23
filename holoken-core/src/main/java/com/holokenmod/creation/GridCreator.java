package com.holokenmod.creation;

import android.util.Log;

import com.holokenmod.Grid;
import com.holokenmod.GridCage;
import com.holokenmod.GridSize;
import com.holokenmod.RandomSingleton;
import com.holokenmod.backtrack.hybrid.MathDokuCage2BackTrack;
import com.holokenmod.options.DigitSetting;
import com.holokenmod.options.GameVariant;
import com.srlee.dlx.DLX;
import com.srlee.dlx.MathDokuDLX;

import java.util.Arrays;

public class GridCreator {
	private final GridSize gridSize;
	
	public GridCreator(final GridSize gridSize) {
		this.gridSize = gridSize;
	}
	
	public Grid createRandomizedGridWithCages() {
		RandomSingleton.getInstance().discard();
		
		Grid newGrid = new Grid(gridSize);
		
		newGrid.addAllCells();
		
		randomiseGrid(newGrid);
		createCages(newGrid);
		
		return newGrid;
	}
	
	private void createCages(Grid grid) {
		GridCageCreator creator = new GridCageCreator(grid);
		
		creator.createCages();
	}
	
	private void randomiseGrid(Grid grid) {
		GridRandomizer randomizer = new GridRandomizer(grid);
		
		randomizer.createGrid();
	}
	
	public Grid create() {
		final boolean debug = false;
		
		int dlxNumber = 0;
		int backTrack2Number = 0;
		int num_attempts = 0;
		
		long sumBacktrack2Duration = 0;
		long sumDLXDuration = 0;
		
		boolean useDLX = gridSize.isSquare() &&
				(GameVariant.getInstance().getDigitSetting() == DigitSetting.FIRST_DIGIT_ZERO
				|| GameVariant.getInstance().getDigitSetting() == DigitSetting.FIRST_DIGIT_ONE);
		
		Grid grid;
		do {
			grid = createRandomizedGridWithCages();
		
			num_attempts++;
			
			if (useDLX) {
				long dlxMillis = System.currentTimeMillis();
				final MathDokuDLX mdd = new MathDokuDLX(grid);
				// Stop solving as soon as we find multiple solutions
				dlxNumber = mdd.Solve(DLX.SolveType.MULTIPLE);
				long dlxDuration = System.currentTimeMillis() - dlxMillis;
				sumDLXDuration += dlxDuration;
				
				Log.d("MathDoku", "DLX Num Solns = " + dlxNumber + " in " + dlxDuration + " ms");
				
				if (dlxNumber == 0) {
					Log.d("dlx", grid.toString());
				}
			}
			
			if (!useDLX || debug) {
				long backtrack2Millis = System.currentTimeMillis();
				final MathDokuCage2BackTrack backTrack2 = new MathDokuCage2BackTrack(grid, true);
				backTrack2Number = backTrack2.solve();
				long backtrack2Duration = System.currentTimeMillis() - backtrack2Millis;
				sumBacktrack2Duration += backtrack2Duration;
				
				grid.clearUserValues();
				
				Log.d("Backtrack2", "Backtrack2 Num Solns = " + backTrack2Number + " in " + backtrack2Duration + " ms");
				
				if (backTrack2Number != dlxNumber) {
					Log.d("backtrack2", "difference: backtrack2 " + backTrack2Number + " - dlx " + dlxNumber + ":" + grid);
					
					//System.exit(0);
				}
				
				if (backTrack2Number == 1) {
					grid.clearUserValues();
				}
				
				if (backTrack2Number == 0) {
					Log.d("backtrack2", "found no solution: " + grid);
					
					for(GridCage cage : grid.getCages()) {
						Log.d("backtrack2",
								"cage "
								+ cage.getId());
						
						for(int[] possibleNums : new GridSingleCageCreator(grid, cage).getPossibleNums()) {
							Log.d("backtrack2", "    " + Arrays.toString(possibleNums));
						}
					}
					
					System.exit(0);
				}
			}
		} while ((useDLX && dlxNumber != 1) || (!useDLX && backTrack2Number != 1));
		
		long averageBacktrack2 = sumBacktrack2Duration / num_attempts;
		long averageDLX = sumDLXDuration / num_attempts;
		
		Log.d("MathDoku", "DLX Num Attempts = " + num_attempts + " in " + sumDLXDuration + " ms" + " (average " + averageDLX + " ms)");
		Log.d("MathDoku", "Backtrack 2 Num Attempts = " + num_attempts + " in " + sumBacktrack2Duration + " ms" + " (average " + averageBacktrack2 + " ms)");
		
		grid.clearUserValues();
		
		return grid;
	}
}