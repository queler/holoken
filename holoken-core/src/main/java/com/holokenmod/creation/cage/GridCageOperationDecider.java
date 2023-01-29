package com.holokenmod.creation.cage;

import com.holokenmod.Randomizer;
import com.holokenmod.grid.GridCage;
import com.holokenmod.grid.GridCageAction;
import com.holokenmod.options.GridCageOperation;

import java.util.Optional;

class GridCageOperationDecider {
	private final Randomizer randomizer;
	private final GridCage cage;
	private final GridCageOperation operationSet;
	
	GridCageOperationDecider(Randomizer randomizer, GridCage cage, GridCageOperation operationSet) {
		this.randomizer = randomizer;
		this.cage = cage;
		this.operationSet = operationSet;
	}
	
	Optional<GridCageAction> decideOperation() {
		if (cage.getCells().size() == 1) {
			return Optional.empty();
		}
		
		if (operationSet == GridCageOperation.OPERATIONS_MULT) {
			return Optional.of(GridCageAction.ACTION_MULTIPLY);
		}
		
		CageCalculationDecision decision = getCalculationDecision(operationSet);
		
		switch (decision) {
			case ADDITION_AND_SUBTRACTION:
				return Optional.of(decideBetweenAdditionAndSubtraction(cage, operationSet));
			case MULTIPLICATION_AND_DIVISION:
				return Optional.of(decideBetweenMultiplicationAndDivision(cage, operationSet));
		}
		
		throw new RuntimeException("Decision could not be calculated.");
	}
	
	private GridCageAction decideBetweenAdditionAndSubtraction(GridCage cage, GridCageOperation operationSet) {
		if (cage.getCells().size() > 2 || operationSet == GridCageOperation.OPERATIONS_ADD_MULT) {
			return GridCageAction.ACTION_ADD;
		}
		
		final double randomValue = randomizer.nextDouble();
		
		if (randomValue >= 0.25) {
			return GridCageAction.ACTION_SUBTRACT;
		}
		
		return GridCageAction.ACTION_ADD;
	}
	
	private GridCageAction decideBetweenMultiplicationAndDivision(GridCage cage, GridCageOperation operationSet) {
		if (cage.getCells().size() > 2 || operationSet == GridCageOperation.OPERATIONS_ADD_MULT) {
			return GridCageAction.ACTION_MULTIPLY;
		}
		
		final double randomValue = randomizer.nextDouble();
		
		if (randomValue >= 0.25 && cage.canHandleDivide()) {
			return GridCageAction.ACTION_DIVIDE;
		}
		
		return GridCageAction.ACTION_MULTIPLY;
	}
	
	private CageCalculationDecision getCalculationDecision(GridCageOperation operationSet) {
		if (operationSet == GridCageOperation.OPERATIONS_ADD_SUB) {
			return CageCalculationDecision.ADDITION_AND_SUBTRACTION;
		}
		
		final double randomValue = randomizer.nextDouble();
		
		if (randomValue >= 0.5) {
			return CageCalculationDecision.MULTIPLICATION_AND_DIVISION;
		} else {
			return CageCalculationDecision.ADDITION_AND_SUBTRACTION;
		}
	}
}