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

	private enum Tens {
		Ten, Twenty, Thirty, Fourty, Fifty, Sixty, Seventy, Eighty, Ninety, Zero;

		static Tens toTens(int tens) {
			switch (tens) {
			case 1:
				return Tens.Ten;
			case 2:
				return Tens.Twenty;
			case 3:
				return Tens.Thirty;
			case 4:
				return Tens.Fourty;
			case 5:
				return Tens.Fifty;
			case 6:
				return Tens.Sixty;
			case 7:
				return Tens.Seventy;
			case 8:
				return Tens.Eighty;
			case 9:
				return Tens.Ninety;
			case 0:
				return Tens.Zero;
			default:
				throw new IllegalArgumentException("Number: " + tens + " is not a digit.");
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
	
	private static String getUnits(int number) {
		Unit unit = Unit.toUnit(returnNthDigit(number, 0));
		if (unit == Unit.Zero)
			return "";
		return unit.toString();
	}

	private static String getTens(int number) {
		Tens tens = Tens.toTens(returnNthDigit(number, 1));
		if (tens == Tens.Zero)
			return "";
		return tens.toString();
	}

	private static String getHundreds(int number) {
		Unit hundreds = Unit.toUnit(returnNthDigit(number, 2));
		if (hundreds == Unit.Zero)
			return "";
		if (hundreds == Unit.One)
			return hundreds.toString() + " hundred";
		else
			return hundreds.toString() + " hundreds";
	}
	
	private static String translateOrderOfMagnitude (int number) {

		return getHundreds(number) + getTens(number) + getUnits(number);
	}
}
