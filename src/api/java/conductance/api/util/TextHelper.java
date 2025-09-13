package conductance.api.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public final class TextHelper {

	public static final Component ENERGY_FORMAT = Component.literal(ChatFormatting.BOLD + "⚡" + ChatFormatting.RESET);
	public static final Component ENERGY_FORMAT_PER_TICK = Component.literal(ChatFormatting.BOLD + "⚡" + ChatFormatting.RESET + "/t");

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

	private TextHelper() {
	}
}
