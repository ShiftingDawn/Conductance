package conductance.loader;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import com.mojang.serialization.Codec;
import org.apache.commons.lang3.function.TriFunction;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.machine.recipe.RecipeBuilder;
import conductance.api.machine.recipe.RecipeElementCloner;
import conductance.api.plugin.RegisterCoverEvent;
import conductance.api.plugin.RegisterFieldSerializerEvent;
import conductance.api.plugin.RegisterMachineEvent;
import conductance.api.plugin.RegisterRecipeEvent;
import conductance.api.plugin.RegisterRecipeTypeEvent;
import conductance.api.plugin.RegisterTagEvent;
import conductance.api.plugin.RegisterTierEvent;
import conductance.api.plugin.RemoveRecipeEvent;
import conductance.api.util.tier.Tier;
import conductance.core.apiimpl.ApiBridge;
import conductance.core.cover.CoverTypeImpl;
import conductance.core.machine.MachineBuilderImpl;
import conductance.core.recipe.RecipeElementTypeSerializer;
import conductance.core.recipe.RecipeTypeBuilderImpl;
import conductance.core.sync.SyncFieldSerializerRegister;
import conductance.runtimepack.server.TagRegister;

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

	public static void dispatchRegisterRecipes(final RecipeOutput recipeOutput, final BiFunction<NCRecipeType, ResourceLocation, RecipeBuilder> factory) {
		PluginEventBus.post(RegisterRecipeEvent.class, modid -> new RegisterRecipeEventImpl(modid, recipeOutput, factory));
	}

	public static void dispatchRemoveRecipes(final Consumer<ResourceLocation> consumer) {
		PluginEventBus.post(RemoveRecipeEvent.class, modid -> new RemoveRecipeEventImpl(consumer));
	}
	//endregion

	//region Misc
	public static void dispatchRegisterSyncFieldSerializers(final SyncFieldSerializerRegister register) {
		PluginEventBus.postAll(RegisterFieldSerializerEvent.class, new RegisterFieldSerializerEventImpl(register));
	}

	public static void dispatchRegisterTiers(final TriFunction<String, String, Integer, Tier.Builder> factory) {
		//TODO clean this up
		PluginEventBus.postAll(RegisterTierEvent.class, new RegisterTierEventImpl(new RegisterTierEventImpl.TierRegister() {

			@Override
			public Tier register(final String registryName, final String displayName, final int tierColor, final Tier previousTier) {
				return factory.apply(registryName, displayName, tierColor).previous(previousTier).build();
			}

			@Override
			public Tier register(final String registryName, final String displayName, final int tierColor) {
				return factory.apply(registryName, displayName, tierColor).build();
			}
		}));
	}

	public static void dispatchRegisterTags(final TagRegister register) {
		PluginEventBus.postAll(RegisterTagEvent.class, new RegisterTagEventImpl(register));
	}
	//endregion

	private PluginEventDispatcher() {
	}
}
