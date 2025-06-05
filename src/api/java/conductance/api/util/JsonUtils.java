package conductance.api.util;

import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.ItemStack;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

public final class JsonUtils {

	public static final JsonSerializer<ItemStack> ITEMSTACK_SERIALIZER;
	public static final JsonDeserializer<ItemStack> ITEMSTACK_DESERIALIZER;

	public static JsonArray toJsonArray(final Boolean... booleans) {
		return Util.make(new JsonArray(booleans.length), arr -> {
			for (final boolean b : booleans) {
				arr.add(b);
			}
		});
	}

	public static JsonArray toJsonArray(final byte... bytes) {
		return Util.make(new JsonArray(bytes.length), arr -> {
			for (final byte b : bytes) {
				arr.add(b);
			}
		});
	}

	public static JsonArray toJsonArray(final short... shorts) {
		return Util.make(new JsonArray(shorts.length), arr -> {
			for (final short s : shorts) {
				arr.add(s);
			}
		});
	}

	public static JsonArray toJsonArray(final int... ints) {
		return Util.make(new JsonArray(ints.length), arr -> {
			for (final int i : ints) {
				arr.add(i);
			}
		});
	}

	public static JsonArray toJsonArray(final long... longs) {
		return Util.make(new JsonArray(longs.length), arr -> {
			for (final long l : longs) {
				arr.add(l);
			}
		});
	}

	public static JsonArray toJsonArray(final float... floats) {
		return Util.make(new JsonArray(floats.length), arr -> {
			for (final float f : floats) {
				arr.add(f);
			}
		});
	}

	public static JsonArray toJsonArray(final double... doubles) {
		return Util.make(new JsonArray(doubles.length), arr -> {
			for (final double d : doubles) {
				arr.add(d);
			}
		});
	}

	public static JsonArray toJsonArray(final char... chars) {
		return Util.make(new JsonArray(chars.length), arr -> {
			for (final char c : chars) {
				arr.add(c);
			}
		});
	}

	public static JsonArray toJsonArray(final String... strings) {
		return Util.make(new JsonArray(strings.length), arr -> {
			for (final String s : strings) {
				arr.add(s);
			}
		});
	}

	public static JsonArray toJsonArray(final JsonElement... children) {
		return Util.make(new JsonArray(children.length), arr -> {
			for (final JsonElement c : children) {
				arr.add(c);
			}
		});
	}

	public static JsonObject getOrOverrideObject(final String name, final JsonObject parent) {
		if (!parent.has(name) || !parent.get(name).isJsonObject()) {
			parent.add(name, new JsonObject());
		}
		return parent.getAsJsonObject(name);
	}

	static {
		ITEMSTACK_SERIALIZER = (src, typeOfSrc, context) ->
				new JsonPrimitive(src.save(ServerLifecycleHooks.getCurrentServer().registryAccess(), new CompoundTag()).toString());
		ITEMSTACK_DESERIALIZER = (json, typeOfT, context) -> {
			try {
				return ItemStack.CODEC.parse(
								RegistryOps.create(NbtOps.INSTANCE, ServerLifecycleHooks.getCurrentServer().registryAccess()),
								TagParser.parseTag(json.getAsString()))
						.result()
						.orElseThrow();
			} catch (final CommandSyntaxException e) {
				return null;
			}
		};
	}

	private JsonUtils() {
	}
}
