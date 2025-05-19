package conductance.api.plugin;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import com.mojang.serialization.Codec;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.RecipeElementCloner;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegisterRecipeElementTypeEvent implements IConductancePluginEvent {

	public interface RecipeElementTypeRegister {

		<T> IRecipeElementType<T> register(ResourceLocation registryKey, Codec<T> dataCodec, StreamCodec<RegistryFriendlyByteBuf, T> dataStreamCodec, RecipeElementCloner<T> cloner);
	}

	private final String modid;
	private final RecipeElementTypeRegister delegate;

	public <T> IRecipeElementType<T> register(final String registryKey, final Codec<T> dataCodec, final StreamCodec<RegistryFriendlyByteBuf, T> dataStreamCodec, final RecipeElementCloner<T> cloner) {
		return this.delegate.register(ResourceLocation.fromNamespaceAndPath(this.modid, registryKey), dataCodec, dataStreamCodec, cloner);
	}
}
