package conductance.api.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.annotation.Nonnull;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import com.google.common.base.CaseFormat;
import org.jetbrains.annotations.NotNull;

public final class TextHelper {

	public static final Component ENERGY_FORMAT = Component.literal(ChatFormatting.BOLD + "⚡" + ChatFormatting.RESET);
	public static final Component ENERGY_FORMAT_PER_TICK = Component.literal(ChatFormatting.BOLD + "⚡" + ChatFormatting.RESET + "/t");

	private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();
	private static final DecimalFormat TWO_PLACES_FORMAT = new DecimalFormat("#.##");

	private static final int SMALL_DOWN_NUMBER_BASE = '\u2080';
	private static final int SMALL_UP_NUMBER_BASE = '\u2070';
	private static final int SMALL_UP_NUMBER_TWO = '\u00B2';
	private static final int SMALL_UP_NUMBER_THREE = '\u00B3';
	private static final int NUMBER_BASE = '0';

	public static String toSmallUpNumbers(final String string) {
		return TextHelper.checkNumbers(string, TextHelper.SMALL_UP_NUMBER_BASE, true);
	}

	public static String toSmallDownNumbers(final String string) {
		return TextHelper.checkNumbers(string, TextHelper.SMALL_DOWN_NUMBER_BASE, false);
	}

	private static String checkNumbers(final String string, final int smallUpNumberBase, final boolean isUp) {
		final char[] charArray = string.toCharArray();
		for (int i = 0; i < charArray.length; ++i) {
			final int relativeIndex = charArray[i] - TextHelper.NUMBER_BASE;
			if (relativeIndex >= 0 && relativeIndex <= 9) {
				if (isUp) {
					if (relativeIndex == 2) {
						charArray[i] = TextHelper.SMALL_UP_NUMBER_TWO;
						continue;
					} else if (relativeIndex == 3) {
						charArray[i] = TextHelper.SMALL_UP_NUMBER_THREE;
						continue;
					}
				}
				final int newChar = smallUpNumberBase + relativeIndex;
				charArray[i] = (char) newChar;
			}
		}
		return new String(charArray);
	}

	public static String toLowerCaseUnderscore(final String string) {
		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < string.length(); ++i) {
			if (i != 0 && (Character.isUpperCase(string.charAt(i)) || (Character.isDigit(string.charAt(i - 1)) ^ Character.isDigit(string.charAt(i))))) {
				result.append("_");
			}
			result.append(Character.toLowerCase(string.charAt(i)));
		}
		return result.toString();
	}

	public static String toLowerCaseUnder(final String string) {
		return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, string);
	}

	public static String toRomanNumeral(final int number) {
		return "I".repeat(number).replace("IIIII", "V").replace("IIII", "IV").replace("VV", "X").replace("VIV", "IX").replace("XXXXX", "L").replace("XXXX", "XL").replace("LL", "C").replace("LXL", "XC").replace("CCCCC", "D").replace("CCCC", "CD").replace("DD", "M").replace("DCD", "CM");
	}

	public static String lowerUnderscoreToUpperCamel(final String string) {
		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < string.length(); ++i) {
			if (string.charAt(i) == '_') {
				continue;
			}
			if (i == 0 || string.charAt(i - 1) == '_') {
				result.append(Character.toUpperCase(string.charAt(i)));
			} else {
				result.append(string.charAt(i));
			}
		}
		return result.toString();
	}

	public static String formatNumbers(final long number) {
		return TextHelper.NUMBER_FORMAT.format(number);
	}

	public static String formatNumbers(final double number) {
		return TextHelper.NUMBER_FORMAT.format(number);
	}

	public static String formatNumber2Places(final float number) {
		return TextHelper.TWO_PLACES_FORMAT.format(number);
	}

	public static String lowerUnderscoreToEnglish(final String string) {
		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < string.length(); ++i) {
			if (string.charAt(i) == '_') {
				result.append(' ');
			} else if (i == 0 || string.charAt(i - 1) == '_') {
				result.append(Character.toUpperCase(string.charAt(i)));
			} else {
				result.append(string.charAt(i));
			}
		}
		return result.toString();
	}
}
