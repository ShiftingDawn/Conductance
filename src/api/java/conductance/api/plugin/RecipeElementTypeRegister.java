package conductance.api.plugin;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import com.mojang.serialization.Codec;
import conductance.api.machine.recipe.IRecipeElementType;

public interface RecipeElementTypeRegister {

	<T> IRecipeElementType<T> register(String name, Codec<T> dataCodec, StreamCodec<RegistryFriendlyByteBuf, T> dataStreamCodec);
}
