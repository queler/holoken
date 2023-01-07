package com.holokenmod.creation;

import com.holokenmod.grid.Grid;
import com.holokenmod.grid.GridCage;
import com.holokenmod.grid.GridCageAction;
import com.holokenmod.grid.GridSize;
import com.holokenmod.options.DigitSetting;
import com.holokenmod.options.GameOptionsVariant;
import com.holokenmod.options.GameVariant;

public class GridBuilder {
	private final Grid grid;
	private int cageId = 0;
	
	public GridBuilder(int size) {
		this(size, size);
	}
	
	public GridBuilder(int size, DigitSetting digitSetting) {
		this(size, size, GameOptionsVariant.createClassic(digitSetting));
	}
	
	public GridBuilder(int size, GameOptionsVariant variant) {
		this(size, size, variant);
	}
	
	public GridBuilder(int width, int heigth) {
		this(width, heigth, GameOptionsVariant.createClassic());
	}
	
	public GridBuilder(int width, int heigth, GameOptionsVariant variant) {
		grid = new Grid(new GameVariant(new GridSize(width, heigth), variant));
		
		grid.addAllCells();
	}
	
	public GridBuilder addCage(int result, GridCageAction action, int... cellIds) {
		if ( cellIds == null || cellIds.length == 0) {
			throw new RuntimeException("No cell ids given.");
		}
		
		GridCage cage = new GridCage(grid);
		
		cage.setCageId(cageId++);
		cage.setAction(action);
		cage.setResult(result);
		
		for(int cellId : cellIds) {
			cage.addCell(grid.getCell(cellId));
		}
		
		grid.addCage(cage);

		return this;
	}
	
	public Grid createGrid() {
		grid.setCageTexts();
		
		return grid;
	}
}
