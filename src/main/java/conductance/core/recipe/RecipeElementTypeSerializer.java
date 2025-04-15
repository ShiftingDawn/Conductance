package conductance.core.recipe;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import com.mojang.serialization.Codec;
import lombok.Getter;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.RecipeElementCloner;
import conductance.api.registry.RegistryObject;

@Getter
public final class RecipeElementTypeSerializer<T> extends RegistryObject<ResourceLocation> implements IRecipeElementType<T> {

	private final Codec<T> dataCodec;
	private final StreamCodec<RegistryFriendlyByteBuf, T> dataStreamCodec;
	private final RecipeElementCloner<T> cloner;

	public RecipeElementTypeSerializer(final ResourceLocation registryKey, final Codec<T> codec, final StreamCodec<RegistryFriendlyByteBuf, T> dataStreamCodec, final RecipeElementCloner<T> cloner) {
		super(registryKey);
		this.dataCodec = codec;
		this.dataStreamCodec = dataStreamCodec;
		this.cloner = cloner;
	}
}
