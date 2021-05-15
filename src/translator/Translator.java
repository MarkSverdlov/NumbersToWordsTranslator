package translator;

public class Translator {
	private static final int DECIMAL = 10;

	private enum Digit {
		One, Two, Three, Four, Five, Six, Seven, Eight, Nine, Zero;

		int toNumber() {
			switch (this) {
			case Eight:
				return 8;
			case Five:
				return 5;
			case Four:
				return 4;
			case Nine:
				return 9;
			case One:
				return 1;
			case Seven:
				return 7;
			case Six:
				return 6;
			case Three:
				return 3;
			case Two:
				return 2;
			case Zero:
				return 0;
			default:
				return -1; // this shouldn't happen

			}
		}

		static Digit toDigit(int digit) {
			switch (digit) {
			case 1:
				return Digit.One;
			case 2:
				return Digit.Two;
			case 3:
				return Digit.Three;
			case 4:
				return Digit.Four;
			case 5:
				return Digit.Five;
			case 6:
				return Digit.Six;
			case 7:
				return Digit.Seven;
			case 8:
				return Digit.Eight;
			case 9:
				return Digit.Nine;
			case 0:
				return Digit.Zero;
			default:
				throw new IllegalArgumentException("Number: " + digit + " is not a digit.");
			}
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

		return Digit.toDigit(n % DECIMAL);
	}
}
