package com.holokenmod.creation;

import com.holokenmod.Grid;

import java.util.ArrayList;

class MultiplicationCreator {
	private final int target_sum;
	private final int n_cells;
	private final Grid grid;
	private final GridSingleCageCreator cageCreator;
	
	MultiplicationCreator(final GridSingleCageCreator cageCreator, final Grid grid, final int target_sum, final int n_cells) {
		this.cageCreator = cageCreator;
		this.grid = grid;
		this.target_sum = target_sum;
		this.n_cells = n_cells;
	}
	
	ArrayList<int[]> create() {
		if (target_sum == 0) {
			return new MultiplicationZeroCreator(
					cageCreator,
					grid,
					n_cells).create();
		}
		
		return new MultiplicationNonZeroCreator(
				cageCreator,
				grid,
				target_sum,
				n_cells).create();
	}
}