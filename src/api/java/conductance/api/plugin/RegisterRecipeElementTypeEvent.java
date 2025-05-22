package conductance.api.plugin;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import com.mojang.serialization.Codec;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.RecipeElementCloner;

public interface RegisterRecipeElementTypeEvent extends IConductancePluginEvent {

	<T> IRecipeElementType<T> register(String registryKey, Codec<T> dataCodec, StreamCodec<RegistryFriendlyByteBuf, T> dataStreamCodec, RecipeElementCloner<T> cloner);
}
