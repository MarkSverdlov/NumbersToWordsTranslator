package translator;

/**
 * The class implements a static method {@code translate()} which gets as a parameter 
 * of type {@code long} and if it's between {@code LOWEST_NUMBER_TRANSLATABLE} to {@code HIGHEST_NUMBER_TRANSLATABLE}
 * the method outputs a String which names the number in English.
 * 
 * The class also implement all the helper methods, enums and other auxiliary code that supports the {@code translate()}
 * method.
 * 
 * @author Mark
 *
 */
public class Translator {
	private static final int DECIMAL = 10;
	
	private static final long LOWEST_NUMBER_TRANSLATEABLE = 0L;

	private static final long HIGHEST_NUMBER_TRANSLATABLE = (long) 1e15 - 1;

	private enum Unit {
		One, Two, Three, Four, Five, Six, Seven, Eight, Nine, Zero;

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
	
	/**
	 * The method translates the 3 rightmost digits of {@code number} into English.
	 * @param number with >3 digits
	 * @return a {@code StringBuilder} representing the translation of the the 3 rightmost
	 * digits into English.
	 */
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
	
	/** 
	 * This is the main and most important method in the class. It is implemented by cutting {@code number} into
	 * chunks of three decimal digits, each being translated individually into a {@code StringBuilder} of the form
	 * {@code new StringBuilder(String.format("%d hundreds %d %d", hundreds, tens, units))} or alternatively of the form
	 * {@code new StringBuilder(String.format("%d hundreds %d", hundreds, teens))} if appropriate.
	 * The individual translations are then sequentially appended into {@code translation} with appropriates commas, spaces and
	 * words. Finally, the method returns {@code translation.toString()}.
	 * @param number to be translated
	 * @return a string representing the number in English, or null if translation is unsuccessful.
	 */
	public static String translate(long number) {
		if (number > HIGHEST_NUMBER_TRANSLATABLE || number < LOWEST_NUMBER_TRANSLATEABLE)
			return null; // translation failed
		if (number == 0)
			return "zero";
		StringBuilder translation = new StringBuilder("");
		long trillions = number / (long) 1e12;
		if (trillions % 1e3 != 0) {
			translation.append(translateOrderOfMagnitude(trillions));
			if (trillions != 1)
				translation.append(" trillions");
			else
				translation.append(" trillion");
		}
		
		long billions = number / (long) 1e9;
		if (billions % 1e3 != 0) {
			if (trillions != 0)
				translation.append(", ");
			translation.append(translateOrderOfMagnitude(billions));
			if (billions != 1)
				translation.append(" billions");
			else
				translation.append(" billion");
		}
		
		long millions = number / (long) 1e6;
		if (millions % 1e3 != 0) {
			if (billions != 0)
				translation.append(", ");
			translation.append(translateOrderOfMagnitude(millions));
			if (millions != 1)
				translation.append(" millions");
			else
				translation.append(" million");
		}
		
		long thousands = number / (long) 1e3;
		if (thousands % 1e3 != 0) {
			if (millions != 0)
				translation.append(", ");
			translation.append(translateOrderOfMagnitude(thousands));
			if (thousands != 1)
				translation.append(" thousands");
			else
				translation.append(" thousand");
		}
		
		long units = number;
		if (units % 1e3 != 0) {
			if (thousands != 0)
				translation.append(", ");
			translation.append(translateOrderOfMagnitude(units));
		}
		
		return translation.toString();
	}
}
