package ring.mobiles.backbone;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * A Bank is a place where money is stored. It can be the money on the Mobile's person
 * or the mone in an actual bank in the game world.
 * @author projectmoon
 *
 */
public class Bank implements Serializable {
	public static final long serialVersionUID = 1;
	
	private BigInteger theMoney;
	
	public Bank() {
		theMoney = BigInteger.valueOf(0);
	}
	
	public Bank(int money) {
		theMoney = BigInteger.valueOf(money);
	}
	
	public Bank(long money) {
		theMoney = BigInteger.valueOf(money);
	}
	
	public Bank(BigInteger money) {
		theMoney = money;
	}
	
	public void deposit(int moreMoney) {
		theMoney = theMoney.add(BigInteger.valueOf(moreMoney));
	}
	
	public int withdraw(int someMoney) {
		if (theMoney.compareTo(BigInteger.valueOf(someMoney)) > 0) {
			theMoney = theMoney.subtract(BigInteger.valueOf(someMoney));
			return someMoney;
		}
		else {
			throw new IllegalArgumentException("You don't have that much money in your bank.");
		}
	}
	
	public void setAmount(long money) {
		theMoney = BigInteger.valueOf(money);
	}
	
	public BigInteger getAmount() {
		return theMoney;
	}
}
