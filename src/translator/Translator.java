package translator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The class implements a static method {@code translate()} which gets as a
 * parameter of type {@code long} and if it's between
 * {@code LOWEST_NUMBER_TRANSLATABLE} to {@code HIGHEST_NUMBER_TRANSLATABLE} the
 * method outputs a String which names the number in English.
 * 
 * The class also implement all the helper methods, enums and other auxiliary
 * code that supports the {@code translate()} method.
 * 
 * @author Mark
 *
 */
public class Translator {
	private static final int DECIMAL = 10;

	public static class TranslatorConstants {
		public static class OrderOfMagnitude {
			private String singularName;
			private String pluralName;

			public OrderOfMagnitude(String singularName, String pluralName) {
				this.singularName = singularName;
				this.pluralName = pluralName;
			}

			public String getSingularName() {
				return singularName;
			}

			public String getPluralName() {
				return pluralName;
			}

		}

		private List<OrderOfMagnitude> listOfConstants;

		// default constructor
		public TranslatorConstants() {
			this.listOfConstants = new ArrayList<OrderOfMagnitude>();
			this.listOfConstants.add(new OrderOfMagnitude("", ""));
			this.listOfConstants.add(new OrderOfMagnitude(" thousand", " thousands"));
			this.listOfConstants.add(new OrderOfMagnitude(" million", " millions"));
			this.listOfConstants.add(new OrderOfMagnitude(" billion", " billions"));
			this.listOfConstants.add(new OrderOfMagnitude(" trillion", " trillions"));
		}

		public TranslatorConstants(List<OrderOfMagnitude> list) {
			if (list == null) // the list should never be null pointer.
				list = new ArrayList<OrderOfMagnitude>();
			this.listOfConstants = list;
		}

		/**
		 * returns the maximum length of a raw number string (without +, - or commas)
		 * that this translator know to translate.
		 * 
		 * @return maximum translatable length
		 */
		public int getUpperBound() {
			try {
				return 3 * getListOfConstants().size();
			} catch (NullPointerException e) {
				/* this doesn't happen */ return 0;
			}
		}

		public List<OrderOfMagnitude> getListOfConstants() {
			return listOfConstants;
		}

	}

	private TranslatorConstants constants;

	public Translator() {
		constants = new TranslatorConstants();
	}

	public Translator(List<TranslatorConstants.OrderOfMagnitude> list) {
		constants = new TranslatorConstants(list);
	}

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

	/**
	 * The method translates the 3 rightmost digits of {@code number} into English.
	 * 
	 * @param number with >=3 digits
	 * @return a {@code StringBuilder} representing the translation of the the 3
	 *         rightmost digits into English.
	 */
	private static StringBuilder translateOrderOfMagnitude(int number) {
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
	 * Checks if the input is a number.
	 * 
	 * The methods returns true in three cases: a. The input is a string of digits
	 * that doesn't begin with zero, with possible +/- before it. b. The input is a
	 * string of numbers with commas according to the American standard. c. The
	 * input is "-0", "+0" or "0".
	 * 
	 * @param input to be checked
	 * @return true if input is a number
	 */
	public class NotTranslatableException extends Exception {
		private static final long serialVersionUID = 7451609711063620553L;

		public NotTranslatableException(String number) {
			super(number + " isn't translateable due to this translator constraints.");
		}

		public TranslatorConstants getTranslatorConstants() {
			return constants;
		}

	}

	private static boolean isNumber(String input) {
		return Pattern.matches("[+-]?([1-9][0-9]*|[1-9][0-9]{0,2}([,]{1}[0-9]{3})*|[0])", input);
	}

	private static boolean isZero(String number) {
		return Pattern.matches("[+-]?[0]", number);
	}

	private static boolean isNegative(String number) {
		return Pattern.matches("-.*", number);
	}

	private boolean isTranslateable(String rawNumber) {
		return rawNumber.length() <= constants.getUpperBound();
	}

	public String translate(String number) throws NotTranslatableException {
		if (!isNumber(number)) {
			throw new IllegalArgumentException(number + " isn't a number.");
		}

		if (isZero(number)) {
			return "zero";
		}

		int sign = isNegative(number) ? -1 : 1;
		String rawNumber = number.replaceAll("[-+,]", ""); // removes all the non digits from the String.

		if (!isTranslateable(rawNumber)) {
			throw new NotTranslatableException(number);
		}

		int length = rawNumber.length();

		StringBuilder translation = new StringBuilder();

		List<TranslatorConstants.OrderOfMagnitude> list = constants.getListOfConstants();
		for (int i = list.size() - 1; i >= 0; i--) {
			String orderOfMagnitude;
				orderOfMagnitude = rawNumber.substring(Math.max(length - 3 * i - 3, 0), Math.max(length - 3 * i, 0));
			if (orderOfMagnitude.isBlank())
				continue;
			int orderOfMagnitudeAsNumber = Integer.parseInt(orderOfMagnitude);
			if (orderOfMagnitudeAsNumber != 0) {
				if (!translation.isEmpty())
					translation.append(", ");
				translation.append(translateOrderOfMagnitude(orderOfMagnitudeAsNumber));
				if (orderOfMagnitudeAsNumber == 1)
					translation.append(list.get(i).getSingularName());
				else
					translation.append(list.get(i).getPluralName());
			}
		}
		
		
		if (sign == -1)
			return "minus " + translation.toString();
		else
			return translation.toString();
	}
}
