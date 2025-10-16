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
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import conductance.api.CAPI;
import conductance.api.recipe.MachineRecipe;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.recipe.RecipeDataMap;
import conductance.api.recipe.RecipeDataToken;
import conductance.api.recipe.RecipeElement;
import conductance.api.recipe.RecipeElementType;

final class MachineRecipeSerializer implements RecipeSerializer<MachineRecipe> {

	private final MapCodec<MachineRecipe> mapCodec;
	private final StreamCodec<RegistryFriendlyByteBuf, MachineRecipe> streamCodec;

	MachineRecipeSerializer(final MachineRecipeType recipeType) {
		this.mapCodec = RecordCodecBuilder.mapCodec(instance -> instance.group(
			MachineRecipeType.CODEC.fieldOf("type").forGetter(MachineRecipe::getType),
			MachineRecipe.CONTENT_MAP_CODEC.fieldOf("inputs").forGetter(MachineRecipe::getInputs),
			MachineRecipe.CONTENT_MAP_CODEC.fieldOf("outputs").forGetter(MachineRecipe::getOutputs),
			MachineRecipe.CONTENT_MAP_CODEC.fieldOf("per_tick_inputs").forGetter(MachineRecipe::getPerTickInputs),
			MachineRecipe.CONTENT_MAP_CODEC.fieldOf("per_tick_outputs").forGetter(MachineRecipe::getPerTickOutputs),
			Codec.INT.fieldOf("duration").forGetter(MachineRecipe::getRecipeDuration),
			Codec.INT.fieldOf("program").forGetter(MachineRecipe::getProgram),
			RecipeDataToken.makeMapCodec(recipeType).optionalFieldOf("data", new RecipeDataMap(List.of())).forGetter(MachineRecipe::getRecipeDataMap)
		).apply(instance, MachineRecipe::new));
		this.streamCodec = StreamCodec.of(MachineRecipeSerializer::toNetwork, MachineRecipeSerializer::fromNetwork);
	}

	@Override
	public MapCodec<MachineRecipe> codec() {
		return this.mapCodec;
	}

	@Override
	@Deprecated
	public StreamCodec<RegistryFriendlyByteBuf, MachineRecipe> streamCodec() {
		return this.streamCodec;
	}

	private static void toNetwork(final RegistryFriendlyByteBuf buf, final MachineRecipe recipe) {
		buf.writeResourceLocation(recipe.getType().getId());
		MachineRecipeSerializer.writeRecipeMap(buf, recipe.getInputs());
		MachineRecipeSerializer.writeRecipeMap(buf, recipe.getOutputs());
		MachineRecipeSerializer.writeRecipeMap(buf, recipe.getPerTickInputs());
		MachineRecipeSerializer.writeRecipeMap(buf, recipe.getPerTickOutputs());
		buf.writeVarInt(recipe.getRecipeDuration());
		buf.writeVarInt(recipe.getProgram());
		MachineRecipeSerializer.writeDataMap(buf, recipe.getRecipeDataMap());
	}

	private static MachineRecipe fromNetwork(final RegistryFriendlyByteBuf buf) {
		final ResourceLocation recipeTypeId = buf.readResourceLocation();
		final MachineRecipeType recipeType = Objects.requireNonNull(CAPI.regs().recipeTypes().getValue(recipeTypeId), () -> "Cannot load unknown recipe type " + recipeTypeId);
		return new MachineRecipe(
			recipeType,
			MachineRecipeSerializer.loadRecipeMap(buf),
			MachineRecipeSerializer.loadRecipeMap(buf),
			MachineRecipeSerializer.loadRecipeMap(buf),
			MachineRecipeSerializer.loadRecipeMap(buf),
			buf.readVarInt(),
			buf.readVarInt(),
			MachineRecipeSerializer.readDataMap(buf, recipeType)
		);
	}

	private static void writeRecipeMap(final RegistryFriendlyByteBuf buf, final Map<RecipeElementType<?>, List<RecipeElement>> map) {
		buf.writeVarInt(map.size());
		map.forEach((key, list) -> {
			RecipeElementType.STREAM_CODEC.encode(buf, key);
			buf.writeVarInt(list.size());
			list.forEach(element -> key.toNetwork(buf, element));
		});
	}

	private static void writeDataMap(final RegistryFriendlyByteBuf buf, final RecipeDataMap dataMap) {
		buf.writeVarInt(dataMap.getData().size());
		dataMap.getData().values().forEach(pair -> {
			buf.writeUtf(pair.token().name());
			pair.toNetwork(buf);
		});
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private static RecipeDataMap readDataMap(final RegistryFriendlyByteBuf buf, final MachineRecipeType recipeType) {
		final List<RecipeDataToken.Pair<?>> dataList = new ArrayList<>();
		for (int i = 0; i < buf.readVarInt(); ++i) {
			final String name = buf.readUtf();
			final RecipeDataToken<?> token = recipeType.getAdditionalDataTokens().get(name);
			dataList.add(new RecipeDataToken.Pair(token, token.streamCodec().decode(buf)));
		}
		return new RecipeDataMap(dataList);
	}

	private static Map<RecipeElementType<?>, List<RecipeElement>> loadRecipeMap(final RegistryFriendlyByteBuf buf) {
		final int mapSize = buf.readVarInt();
		final Map<RecipeElementType<?>, List<RecipeElement>> result = new HashMap<>();
		for (int i = 0; i < mapSize; ++i) {
			final RecipeElementType<?> key = RecipeElementType.STREAM_CODEC.decode(buf);
			final int listSize = buf.readVarInt();
			final List<RecipeElement> list = result.computeIfAbsent(key, k -> new ArrayList<>(listSize));
			for (int j = 0; j < listSize; ++j) {
				list.add(key.fromNetwork(buf));
			}
		}
		return result;
	}
}
