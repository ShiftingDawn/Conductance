package conductance.core.recipe;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import com.mojang.serialization.Codec;
import lombok.AllArgsConstructor;
import conductance.api.recipe.IRecipeElementType;
import conductance.api.recipe.RecipeElementCloner;
import conductance.api.recipe.event.RegisterRecipeElementTypeEvent;

@AllArgsConstructor
final class RegisterRecipeElementTypeEventImpl implements RegisterRecipeElementTypeEvent {

	public interface RecipeElementTypeRegister {

		<T> IRecipeElementType<T> register(String registryKey, Codec<T> dataCodec, StreamCodec<RegistryFriendlyByteBuf, T> dataStreamCodec, RecipeElementCloner<T> cloner);
	}

	private final RecipeElementTypeRegister delegate;

	@Override
	public <T> IRecipeElementType<T> register(final String registryName, final Codec<T> dataCodec, final StreamCodec<RegistryFriendlyByteBuf, T> dataStreamCodec, final RecipeElementCloner<T> cloner) {
		return this.delegate.register(registryName, dataCodec, dataStreamCodec, cloner);
	}
}
