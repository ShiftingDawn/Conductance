package conductance.loader;

import net.minecraft.Util;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import com.mojang.serialization.Codec;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.RecipeElementCloner;
import conductance.api.plugin.RegisterRecipeTypeEvent;
import conductance.core.recipe.RecipeElementTypeSerializer;
import conductance.core.recipe.RecipeTypeBuilderImpl;
import conductance.core.register.RegisterCore;

public final class PluginEventDispatcher {

	//region Recipe
	public static void dispatchRegisterRecipeElementTypes() {
		//TODO clean this up
		PluginEventBus.post(RegisterRecipeElementTypeEventImpl.class, modid -> new RegisterRecipeElementTypeEventImpl(modid, new RegisterRecipeElementTypeEventImpl.RecipeElementTypeRegister() {

			@Override
			public <T> IRecipeElementType<T> register(final ResourceLocation registryKey, final Codec<T> dataCodec, final StreamCodec<RegistryFriendlyByteBuf, T> dataStreamCodec, final RecipeElementCloner<T> cloner) {
				return Util.make(new RecipeElementTypeSerializer<>(registryKey, dataCodec, dataStreamCodec, cloner), result -> {
					RegisterCore.getRegs().recipeElementTypes().register(result);
				});
			}
		}));
	}

	public static void dispatchRegisterRecipeTypes() {
		PluginEventBus.post(RegisterRecipeTypeEvent.class, modid -> new RegisterRecipeTypeEventImpl(modid, RecipeTypeBuilderImpl::new));
	}
	//endregion

	private PluginEventDispatcher() {
	}
}
