package conductance.core.recipe;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import com.mojang.serialization.Codec;
import lombok.RequiredArgsConstructor;
import conductance.api.recipe.RecipeElementType;
import conductance.api.recipe.event.RegisterRecipeElementTypeEvent;

@RequiredArgsConstructor
final class RegisterRecipeElementTypeEventImpl implements RegisterRecipeElementTypeEvent {

	interface Delegate {
		<T> RecipeElementType<T> apply(String registryName, Codec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec);
	}

	private final Delegate delegate;

	@Override
	public <T> RecipeElementType<T> register(final String registryName, final Codec<T> codec, final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
		return this.delegate.apply(registryName, codec, streamCodec);
	}
}
