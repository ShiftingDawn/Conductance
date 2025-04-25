package conductance.core.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import conductance.api.machine.recipe.IRecipe;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.NCRecipeSerializer;
import conductance.api.machine.recipe.RecipeElement;

public class RecipeSerializerImpl implements NCRecipeSerializer {

	public static final MapCodec<IRecipe> CODEC;
	public static final StreamCodec<RegistryFriendlyByteBuf, IRecipe> STREAM_CODEC = StreamCodec.of(RecipeSerializerImpl::toNetwork, RecipeSerializerImpl::fromNetwork);

	@Override
	public MapCodec<IRecipe> codec() {
		return RecipeSerializerImpl.CODEC;
	}

	@Override
	public StreamCodec<RegistryFriendlyByteBuf, IRecipe> streamCodec() {
		return RecipeSerializerImpl.STREAM_CODEC;
	}

	public static void toNetwork(final RegistryFriendlyByteBuf buf, final IRecipe recipe) {
		buf.writeResourceLocation(BuiltInRegistries.RECIPE_TYPE.getKey(recipe.getType()));
		buf.writeResourceLocation(recipe.getId());
		RecipeSerializerImpl.writeRecipeMap(buf, recipe.getInputs());
		RecipeSerializerImpl.writeRecipeMap(buf, recipe.getOutputs());
		RecipeSerializerImpl.writeRecipeMap(buf, recipe.getInputsPerTick());
		RecipeSerializerImpl.writeRecipeMap(buf, recipe.getOutputsPerTick());
		buf.writeVarInt(recipe.getProcessTime());
		buf.writeVarInt(recipe.getProgram());
	}


	public static IRecipe fromNetwork(final RegistryFriendlyByteBuf buf) {
		return new RecipeImpl(
				BuiltInRegistries.RECIPE_TYPE.get(buf.readResourceLocation()),
				buf.readResourceLocation(),
				RecipeSerializerImpl.loadRecipeMap(buf),
				RecipeSerializerImpl.loadRecipeMap(buf),
				RecipeSerializerImpl.loadRecipeMap(buf),
				RecipeSerializerImpl.loadRecipeMap(buf),
				buf.readVarInt(),
				buf.readVarInt()
		);
	}

	private static void writeRecipeMap(final RegistryFriendlyByteBuf buf, final Map<IRecipeElementType<?>, List<RecipeElement>> map) {
		buf.writeVarInt(map.size());
		map.forEach((key, list) -> {
			RecipeCodecs.ELEMENT_TYPE_STREAM.encode(buf, key);
			buf.writeVarInt(list.size());
			list.forEach(element -> key.toNetwork(buf, element));
		});
	}

	private static Map<IRecipeElementType<?>, List<RecipeElement>> loadRecipeMap(final RegistryFriendlyByteBuf buf) {
		final int mapSize = buf.readVarInt();
		final Map<IRecipeElementType<?>, List<RecipeElement>> result = new HashMap<>();
		for (int i = 0; i < mapSize; ++i) {
			final IRecipeElementType<?> key = RecipeCodecs.ELEMENT_TYPE_STREAM.decode(buf);
			final int listSize = buf.readVarInt();
			final List<RecipeElement> list = result.computeIfAbsent(key, k -> new ArrayList<>(listSize));
			for (int j = 0; j < listSize; ++j) {
				list.add(key.fromNetwork(buf));
			}
		}
		return result;
	}

	static {
		CODEC = RecordCodecBuilder.mapCodec(ins -> ins.group(
				BuiltInRegistries.RECIPE_TYPE.byNameCodec().fieldOf("type").forGetter(IRecipe::getType),
				ResourceLocation.CODEC.fieldOf("id").forGetter(IRecipe::getId),
				RecipeCodecs.ELEMENT_MAP.fieldOf("inputs").forGetter(IRecipe::getInputs),
				RecipeCodecs.ELEMENT_MAP.fieldOf("outputs").forGetter(IRecipe::getOutputs),
				RecipeCodecs.ELEMENT_MAP.fieldOf("inputspertick").forGetter(IRecipe::getInputsPerTick),
				RecipeCodecs.ELEMENT_MAP.fieldOf("outputspertick").forGetter(IRecipe::getOutputsPerTick),
				Codec.INT.fieldOf("processtime").forGetter(IRecipe::getProcessTime),
				Codec.INT.fieldOf("program").forGetter(IRecipe::getProgram)
		).apply(ins, RecipeImpl::new));
	}
}
