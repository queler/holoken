package com.holokenmod;

import java.util.Random;

public class RandomSingleton implements Randomizer {
	private static final RandomSingleton INSTANCE = new RandomSingleton();
	
	private Random random;
	
	public static RandomSingleton getInstance() {
		return INSTANCE;
	}
	
	public void discard() {
		random = new Random();
	}
	
	public int nextInt(final int bound) {
		return random.nextInt(bound);
	}
	
	public double nextDouble() {
		return random.nextDouble();
	}
	
	public Random getRandom() {
		if (random == null) {
			random = new Random();
		}
		return random;
	}
}
