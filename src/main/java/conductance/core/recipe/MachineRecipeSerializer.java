package conductance.core.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import conductance.api.CAPI;
import conductance.api.recipe.MachineRecipe;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.recipe.RecipeElementType;
import conductance.api.recipe.RecipeObject;

final class MachineRecipeSerializer implements RecipeSerializer<MachineRecipe> {

	public static final MachineRecipeSerializer INSTANCE = new MachineRecipeSerializer();
	private static final MapCodec<MachineRecipe> MAP_CODEC;
	private static final StreamCodec<RegistryFriendlyByteBuf, MachineRecipe> STREAM_CODEC;

	private MachineRecipeSerializer() {
	}

	@Override
	public MapCodec<MachineRecipe> codec() {
		return MachineRecipeSerializer.MAP_CODEC;
	}

	@Override
	@Deprecated
	public StreamCodec<RegistryFriendlyByteBuf, MachineRecipe> streamCodec() {
		return MachineRecipeSerializer.STREAM_CODEC;
	}

	private static void toNetwork(final RegistryFriendlyByteBuf buf, final MachineRecipe recipe) {
		buf.writeResourceLocation(recipe.getType().getId());
		MachineRecipeSerializer.writeRecipeMap(buf, recipe.getInputs());
		MachineRecipeSerializer.writeRecipeMap(buf, recipe.getOutputs());
	}

	private static MachineRecipe fromNetwork(final RegistryFriendlyByteBuf buf) {
		final ResourceLocation recipeType = buf.readResourceLocation();
		return new MachineRecipeImpl(
			Objects.requireNonNull(CAPI.regs().recipeTypes().getValue(recipeType), () -> "Cannot load unknown recipe type " + recipeType),
			MachineRecipeSerializer.loadRecipeMap(buf),
			MachineRecipeSerializer.loadRecipeMap(buf)
		);
	}

	private static void writeRecipeMap(final RegistryFriendlyByteBuf buf, final Map<RecipeElementType<?>, List<RecipeObject>> map) {
		buf.writeVarInt(map.size());
		map.forEach((key, list) -> {
			RecipeElementType.STREAM_CODEC.encode(buf, key);
			buf.writeVarInt(list.size());
			list.forEach(element -> key.toNetwork(buf, element));
		});
	}

	private static Map<RecipeElementType<?>, List<RecipeObject>> loadRecipeMap(final RegistryFriendlyByteBuf buf) {
		final int mapSize = buf.readVarInt();
		final Map<RecipeElementType<?>, List<RecipeObject>> result = new HashMap<>();
		for (int i = 0; i < mapSize; ++i) {
			final RecipeElementType<?> key = RecipeElementType.STREAM_CODEC.decode(buf);
			final int listSize = buf.readVarInt();
			final List<RecipeObject> list = result.computeIfAbsent(key, k -> new ArrayList<>(listSize));
			for (int j = 0; j < listSize; ++j) {
				list.add(key.fromNetwork(buf));
			}
		}
		return result;
	}

	static {
		MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			MachineRecipeType.CODEC.fieldOf("type").forGetter(MachineRecipe::getType),
			MachineRecipe.CONTENT_MAP_CODEC.fieldOf("inputs").forGetter(MachineRecipe::getInputs),
			MachineRecipe.CONTENT_MAP_CODEC.fieldOf("outputs").forGetter(MachineRecipe::getOutputs)
		).apply(instance, MachineRecipeImpl::new));
		STREAM_CODEC = StreamCodec.of(MachineRecipeSerializer::toNetwork, MachineRecipeSerializer::fromNetwork);
	}
}
