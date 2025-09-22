package conductance.core.recipe;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import com.mojang.serialization.Codec;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import conductance.api.recipe.RecipeElementType;

@RequiredArgsConstructor
final class RecipeElementTypeImpl<T> implements RecipeElementType<T> {

	private final @Getter Codec<T> dataCodec;
	private final @Getter StreamCodec<RegistryFriendlyByteBuf, T> dataStreamCodec;
}
