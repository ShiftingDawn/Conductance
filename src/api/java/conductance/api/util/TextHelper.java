package conductance.api.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import conductance.api.CAPI;

public final class TextHelper {

	public static final NumberFormat NUMBER_FORMAT = NumberFormat.getIntegerInstance();
	public static final Component ENERGY_FORMAT = Component.literal(ChatFormatting.YELLOW + "" + ChatFormatting.BOLD + "⚡" + ChatFormatting.RESET);
	public static final Component ENERGY_FORMAT_PER_TICK = Component.literal(ChatFormatting.YELLOW + "" + ChatFormatting.BOLD + "⚡" + ChatFormatting.RESET + "/t");
	private static final DecimalFormat BUCKET_FORMAT = new DecimalFormat("0.##");
	private static final DecimalFormat TIME_FORMAT = new DecimalFormat("0.00");
	private static final char[] SUBSCRIPTS = {'₀', '₁', '₂', '₃', '₄', '₅', '₆', '₇', '₈', '₉'};

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

	public static String getNumberAsSubscript(int number) {
		final StringBuilder builder = new StringBuilder();
		while (number > 0) {
			builder.append(TextHelper.SUBSCRIPTS[number % 10]);
			number /= 10;
		}
		return builder.toString();
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

	public static Component getFormattedEnergy(final long energy) {
		return Component.literal(TextHelper.NUMBER_FORMAT.format(energy)).append(TextHelper.ENERGY_FORMAT);
	}

	public static String getFormattedTicks(final int ticks) {
		return TextHelper.TIME_FORMAT.format(ticks / 20.0);
	}

	private TextHelper() {
	}
}
