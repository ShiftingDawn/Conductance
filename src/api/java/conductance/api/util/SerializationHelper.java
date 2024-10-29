package conductance.api.util;

import java.util.Arrays;
import net.minecraft.Util;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public final class SerializationHelper {

	public static JsonArray toJsonArray(final Boolean... numbers) {
		return Util.make(new JsonArray(), arr -> Arrays.stream(numbers).forEachOrdered(arr::add));
	}

	public static JsonArray toJsonArray(final byte... numbers) {
		return Util.make(new JsonArray(), arr -> {
			for (final byte b : numbers) {
				arr.add(b);
			}
		});
	}

	public static JsonArray toJsonArray(final short... numbers) {
		return Util.make(new JsonArray(), arr -> {
			for (final short s : numbers) {
				arr.add(s);
			}
		});
	}

	public static JsonArray toJsonArray(final int... numbers) {
		return Util.make(new JsonArray(), arr -> {
			for (final int i : numbers) {
				arr.add(i);
			}
		});
	}

	public static JsonArray toJsonArray(final long... numbers) {
		return Util.make(new JsonArray(), arr -> {
			for (final long l : numbers) {
				arr.add(l);
			}
		});
	}

	public static JsonArray toJsonArray(final float... numbers) {
		return Util.make(new JsonArray(), arr -> {
			for (final float f : numbers) {
				arr.add(f);
			}
		});
	}

	public static JsonArray toJsonArray(final double... numbers) {
		return Util.make(new JsonArray(), arr -> {
			for (final double d : numbers) {
				arr.add(d);
			}
		});
	}

	public static JsonArray toJsonArray(final Character... numbers) {
		return Util.make(new JsonArray(), arr -> Arrays.stream(numbers).forEachOrdered(arr::add));
	}

	public static JsonArray toJsonArray(final String... numbers) {
		return Util.make(new JsonArray(), arr -> Arrays.stream(numbers).forEachOrdered(arr::add));
	}

	public static JsonArray toJsonArray(final JsonElement... numbers) {
		return Util.make(new JsonArray(), arr -> Arrays.stream(numbers).forEachOrdered(arr::add));
	}

	private SerializationHelper() {
	}
}
