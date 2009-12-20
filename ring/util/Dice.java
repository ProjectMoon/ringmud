package ring.util;

import java.util.Random;

/**
 * A class for rolling dice!
 * @author projectmoon
 *
 */
public class Dice {
	private int numDice;
	private int diceType;
	private int bonus;
	
	public Dice(int numDice, int diceType) {
		this.numDice = numDice;
		this.diceType = diceType;
	}
	
	public Dice(int numDice, int diceType, int bonus) {
		this.numDice = numDice;
		this.diceType = diceType;
		this.bonus = bonus;
	}
	
	public Dice(String diceString) {
		throw new UnsupportedOperationException("diceString constructor not implemented yet.");
		//TODO dice string constructor
	}
	
	public int roll() {
		Random rand = new Random();
		int result = 0;
		
		for (int c = 0; c < numDice; c++) {
			result += rand.nextInt(diceType) + 1;
		}
		
		result += bonus;
		return bonus;
	}
}
