package conductance.api.util;

import java.util.Arrays;
import java.util.function.Predicate;
import net.minecraft.Util;
import com.mojang.datafixers.util.Pair;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

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

	public static JsonObject getOrOverrideObject(final JsonObject parent, final String name) {
		if (!parent.has(name) || !parent.get(name).isJsonObject()) {
			parent.add(name, new JsonObject());
		}
		return parent.getAsJsonObject(name);
	}

	@Nullable
	public static Pair<JsonObject, String> findContainer(final JsonObject obj, final Predicate<JsonElement> predicate) {
		for (final String key : obj.keySet()) {
			if (predicate.test(obj.get(key))) {
				return Pair.of(obj, key);
			} else if (obj.get(key).isJsonObject()) {
				final Pair<JsonObject, String> found = SerializationHelper.findContainer(obj.getAsJsonObject(key), predicate);
				if (found != null) {
					return found;
				}
			}
		}
		return null;
	}

	private SerializationHelper() {
	}
}
