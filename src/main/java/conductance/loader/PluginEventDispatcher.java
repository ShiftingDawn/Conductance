package conductance.loader;

import net.minecraft.Util;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import com.mojang.serialization.Codec;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.RecipeElementCloner;
import conductance.api.plugin.RegisterCoverEvent;
import conductance.api.plugin.RegisterFieldSerializerEvent;
import conductance.api.plugin.RegisterMachineEvent;
import conductance.api.plugin.RegisterRecipeTypeEvent;
import conductance.core.apiimpl.ApiBridge;
import conductance.core.cover.CoverTypeImpl;
import conductance.core.machine.MachineBuilderImpl;
import conductance.core.recipe.RecipeElementTypeSerializer;
import conductance.core.recipe.RecipeTypeBuilderImpl;
import conductance.core.sync.SyncFieldSerializerRegister;

public final class PluginEventDispatcher {

	//region Machine
	public static void dispatchRegisterCovers() {
		PluginEventBus.post(RegisterCoverEvent.class, modid -> new RegisterCoverEventImpl(modid, CoverTypeImpl::new));
	}

	public static void dispatchRegisterMachines() {
		PluginEventBus.postAll(RegisterMachineEvent.class, new RegisterMachineEventImpl(MachineBuilderImpl::new));
	}
	//endregion

	//region Recipe
	public static void dispatchRegisterRecipeElementTypes() {
		//TODO clean this up
		PluginEventBus.post(RegisterRecipeElementTypeEventImpl.class, modid -> new RegisterRecipeElementTypeEventImpl(modid, new RegisterRecipeElementTypeEventImpl.RecipeElementTypeRegister() {

			@Override
			public <T> IRecipeElementType<T> register(final ResourceLocation registryKey, final Codec<T> dataCodec, final StreamCodec<RegistryFriendlyByteBuf, T> dataStreamCodec, final RecipeElementCloner<T> cloner) {
				return Util.make(new RecipeElementTypeSerializer<>(registryKey, dataCodec, dataStreamCodec, cloner), result -> {
					ApiBridge.getRegs().recipeElementTypes().register(result);
				});
			}
		}));
	}

	public static void dispatchRegisterRecipeTypes() {
		PluginEventBus.post(RegisterRecipeTypeEvent.class, modid -> new RegisterRecipeTypeEventImpl(modid, RecipeTypeBuilderImpl::new));
	}
	//endregion

	//region Misc
	public static void dispatchRegisterSyncFieldSerializers(final SyncFieldSerializerRegister register) {
		PluginEventBus.postAll(RegisterFieldSerializerEvent.class, new RegisterFieldSerializerEventImpl(register));
	}
	//endregion

	private PluginEventDispatcher() {
	}
}
