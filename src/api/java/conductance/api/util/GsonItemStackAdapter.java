package conductance.api.util;

import java.lang.reflect.Type;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.ItemStack;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public final class GsonItemStackAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {

	public static final GsonItemStackAdapter INSTANCE = new GsonItemStackAdapter();

	private GsonItemStackAdapter() {
	}

	@Override
	public ItemStack deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		try {
			return ItemStack.CODEC.parse(
							RegistryOps.create(NbtOps.INSTANCE, ServerLifecycleHooks.getCurrentServer().registryAccess()),
							TagParser.parseTag(json.getAsString()))
					.result()
					.orElseThrow();
		} catch (final CommandSyntaxException e) {
			return null;
		}
	}

	@Override
	public JsonElement serialize(final ItemStack src, final Type typeOfSrc, final JsonSerializationContext context) {
		return new JsonPrimitive(src.save(ServerLifecycleHooks.getCurrentServer().registryAccess(), new CompoundTag()).toString());
	}
}
