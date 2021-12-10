package com.holokenmod.backtrack.hybrid;

import com.holokenmod.Grid;
import com.holokenmod.creation.GridCageCreator;

import java.util.List;
import java.util.concurrent.ThreadFactory;

public class BackTrackThreadFactory implements ThreadFactory {
	private final Grid grid;
	private final List<GridCageCreator> cageCreators;
	private final BackTrackSolutionListener solutionListener;
	private final boolean isPreSolved;
	
	public BackTrackThreadFactory(Grid grid, List<GridCageCreator> cageCreators, boolean isPreSolved, BackTrackSolutionListener solutionListener) {
		this.grid = grid;
		this.cageCreators = cageCreators;
		this.isPreSolved = isPreSolved;
		this.solutionListener = solutionListener;
	}
	
	@Override
	public Thread newThread(Runnable r) {
		BackTrackThread thread = new BackTrackThread(r, createGrid(), cageCreators, isPreSolved, solutionListener);
		
		return thread;
	}
	
	private Grid createGrid() {
		return grid.copyEmpty();
	}
}