package translator;

public class Translator {
	private static final int DECIMAL = 10;

	private enum Unit {
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

		static Unit toUnit(int unit) {
			switch (unit) {
			case 1:
				return Unit.One;
			case 2:
				return Unit.Two;
			case 3:
				return Unit.Three;
			case 4:
				return Unit.Four;
			case 5:
				return Unit.Five;
			case 6:
				return Unit.Six;
			case 7:
				return Unit.Seven;
			case 8:
				return Unit.Eight;
			case 9:
				return Unit.Nine;
			case 0:
				return Unit.Zero;
			default:
				throw new IllegalArgumentException("Number: " + unit + " is not a digit.");
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
	private static int returnNthDigit(int number, int n) {
		for (int i = 0; i < n; i++) {
			number /= DECIMAL;
		}

		return n % DECIMAL;
	}
}
