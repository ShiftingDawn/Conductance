package conductance.api.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import conductance.api.CAPI;

public final class TextHelper {

	public static final NumberFormat NUMBER_FORMAT = NumberFormat.getIntegerInstance();
	public static final Component ENERGY_FORMAT = Component.literal(ChatFormatting.BOLD + "⚡" + ChatFormatting.RESET);
	public static final Component ENERGY_FORMAT_PER_TICK = Component.literal(ChatFormatting.BOLD + "⚡" + ChatFormatting.RESET + "/t");
	private static final DecimalFormat BUCKET_FORMAT = new DecimalFormat("0.##");

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

	public static String getFormattedFluidAmount(final int amount) {
		if (amount < CAPI.BUCKET) {
			return amount + "mB";
		}
		if (amount < CAPI.BUCKET * 1000) {
			return TextHelper.BUCKET_FORMAT.format(amount / 1000.0) + "B";
		}
		return TextHelper.BUCKET_FORMAT.format(amount / 1000_000.0) + "kB";
	}

	public static Component getFormattedRecipeDuration(final int ticks) {
		if (ticks < 20) {
			return Component.translatable("info.conductance.generic.duration.ticks", ticks);
		} else if (ticks < 200) {
			return Component.translatable("info.conductance.generic.duration.seconds_small", TextHelper.BUCKET_FORMAT.format(ticks / 20.0), ticks);
		} else {
			return Component.translatable("info.conductance.generic.duration.seconds", TextHelper.BUCKET_FORMAT.format(ticks / 20.0));
		}
	}

	private TextHelper() {
	}
}
