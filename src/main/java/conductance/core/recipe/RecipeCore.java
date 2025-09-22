package conductance.core.recipe;

import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import com.mojang.serialization.Codec;
import conductance.api.CAPI;
import conductance.api.recipe.RecipeElementType;
import conductance.api.recipe.event.RegisterRecipeElementTypeEvent;
import conductance.api.recipe.event.RegisterRecipeTypeEvent;
import conductance.Conductance;

public final class RecipeCore {

	public static void initialize() {
		RecipeCore.initElementTypes();
		RecipeCore.initRecipeTypes();
	}

	private static void initElementTypes() {
		Conductance.dispatch(RegisterRecipeElementTypeEvent.class, modid -> new RegisterRecipeElementTypeEventImpl(new RegisterRecipeElementTypeEventImpl.Delegate() {
			@Override
			public <T> RecipeElementType<T> apply(final String registryName, final Codec<T> codec, final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
				final ResourceLocation registryKey = ResourceLocation.fromNamespaceAndPath(modid, registryName);
				final RecipeElementTypeImpl<T> result = new RecipeElementTypeImpl<>(codec, streamCodec);
				Conductance.REGISTRIES.register(CAPI.regs().recipeElementTypes(), registryKey, result);
				return result;
			}
		}));
	}

	private static void initRecipeTypes() {

		Conductance.dispatch(RegisterRecipeTypeEvent.class, modid -> new RegisterRecipeTypeEventImpl((registryName, builder) -> {
			final ResourceLocation registryKey = ResourceLocation.fromNamespaceAndPath(modid, registryName);
			final MachineRecipeTypeImpl result = Util.make(new RecipeTypeBuilderImpl(), builder).build();
			Conductance.REGISTRIES.register(CAPI.regs().recipeTypes(), registryKey, result);
			Registry.register(BuiltInRegistries.RECIPE_TYPE, registryKey, result);
			return result;
		}));
	}

	private RecipeCore() {
	}
}
