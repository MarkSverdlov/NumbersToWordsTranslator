package translator;

public class Translator {
	private static final int DECIMAL = 10;

	private enum Digit {
		One, Two, Three, Four, Five, Six, Seven, Eight, Nine;
	}

	private Digit toDigit(int digit) {
		switch (digit) {
		case 1:
			return Digit.One;
		case 2: return Digit.Two;
		case 3: return Digit.Three;
		case 4: return Digit.Four;
		
		}
	}

	/**
	 * the method returns the nth digit of the given number, from the rightmost
	 * digit being digit 0, and growing to the left.
	 * 
	 * @param number - the given number
	 * @param n      - the index of the digit
	 * @return the nth digit of the number
	 */
	private static Digit returnNthDigit(int number, int n) {
		for (int i = 0; i < n; i++) {
			number /= DECIMAL;
		}

		return number % DECIMAL;
	}
}
