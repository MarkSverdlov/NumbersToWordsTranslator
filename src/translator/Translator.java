package translator;

public class Translator {
	private static final int DECIMAL = 10;

	private static final long HIGHEST_NUMBER_TRANSLATABLE = (long) 1e15 - 1;

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

		@Override
		public String toString() {
			return super.toString().toLowerCase();
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

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}

	private enum OrderOfMagnitude {
		Thousands, Millions, Billions, Trilions;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}

		public String toStringSingular() {
			switch (this) {
			case Billions:
				return "billion";
			case Millions:
				return "million";
			case Thousands:
				return "thousand";
			case Trilions:
				return "trillion";
			default:
				return null;

			}
		}
	}
	
	private enum Teens {
		Eleven, Twelve, Thirteen, Fourteen, Fifteen, Sixteen, Seventeen, Eighteen, Nineteen;
		
		static Teens toTeens(int teen) {
			switch (teen) {
			case 11:
				return Teens.Eleven;
			case 12:
				return Teens.Twelve;
			case 13:
				return Teens.Thirteen;
			case 14:
				return Teens.Fourteen;
			case 15:
				return Teens.Fifteen;
			case 16:
				return Teens.Sixteen;
			case 17:
				return Teens.Seventeen;
			case 18:
				return Teens.Eighteen;
			case 19:
				return Teens.Nineteen;
			default:
				throw new IllegalArgumentException("Number: " + teen + " is not a teen.");
			}
		}
		
		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
		
		static boolean isTeen(long number) {
			return number <= 19 && number >= 11;
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
	private static int returnNthDigit(long number, int n) {
		for (int i = 0; i < n; i++) {
			number /= DECIMAL;
		}

		return (int) (number % DECIMAL);
	}

	private static String getUnits(long number) {
		Unit unit = Unit.toUnit(returnNthDigit(number, 0));
		if (unit == Unit.Zero)
			return "";
		return unit.toString();
	}

	private static String getTens(long number) {
		Tens tens = Tens.toTens(returnNthDigit(number, 1));
		if (tens == Tens.Zero)
			return "";
		return tens.toString();
	}

	private static String getHundreds(long number) {
		Unit hundreds = Unit.toUnit(returnNthDigit(number, 2));
		if (hundreds == Unit.Zero)
			return "";
		if (hundreds == Unit.One)
			return hundreds.toString() + " hundred";
		else
			return hundreds.toString() + " hundreds";
	}

	private static StringBuilder translateOrderOfMagnitude(long number) {
		StringBuilder translation = new StringBuilder();
		translation.append(getHundreds(number));
		if (Teens.isTeen(number % 100)) {
			if (!translation.isEmpty())
				translation.append(" ");
			translation.append(Teens.toTeens((int) (number % 100)));
			return translation; // the teen segment in the logic override the tens & units segments.
		}
		if (!getTens(number).isEmpty() && !translation.isEmpty())
			translation.append(" ");
		translation.append(getTens(number));
		if (!getUnits(number).isEmpty() && !translation.isEmpty())
			translation.append(" ");
		translation.append(getUnits(number));
		return translation;
	}

	public static String translate(long number) {
		if (number > HIGHEST_NUMBER_TRANSLATABLE)
			return null; // translation failed
		if (number == 0)
			return "zero";
		StringBuilder translation = new StringBuilder("");
		long trillions = number / (long) 1e12;
		if (trillions != 0) {
			translation.append(translateOrderOfMagnitude(trillions));
			if (trillions != 1)
				translation.append(" trillions");
			else
				translation.append(" trillions");
		}
		
		long billions = number / (long) 1e9;
		if (billions != 0) {
			if (trillions != 0)
				translation.append(", ");
			translation.append(translateOrderOfMagnitude(billions));
			if (billions != 1)
				translation.append(" billions");
			else
				translation.append(" billion");
		}
		
		long millions = number / (long) 1e6;
		if (millions != 0) {
			if (billions != 0)
				translation.append(", ");
			translation.append(translateOrderOfMagnitude(millions));
			if (millions != 1)
				translation.append(" millions");
			else
				translation.append(" million");
		}
		
		long thousands = number / (long) 1e3;
		if (thousands != 0) {
			if (millions != 0)
				translation.append(", ");
			translation.append(translateOrderOfMagnitude(thousands));
			if (thousands != 1)
				translation.append(" thousands");
			else
				translation.append(" thousand");
		}
		
		long units = number;
		if (units != 0) {
			if (thousands != 0)
				translation.append(", ");
			translation.append(translateOrderOfMagnitude(units));
		}
		
		return translation.toString();
	}
}
