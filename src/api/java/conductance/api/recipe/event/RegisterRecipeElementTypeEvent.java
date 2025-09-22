package conductance.api.recipe.event;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import com.mojang.serialization.Codec;
import conductance.api.plugin.IConductancePluginEvent;
import conductance.api.recipe.RecipeElementType;

public interface RegisterRecipeElementTypeEvent extends IConductancePluginEvent {

	<T> RecipeElementType<T> register(String registryName, Codec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec);
}
