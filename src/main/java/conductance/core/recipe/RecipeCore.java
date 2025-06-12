package conductance.core.recipe;

import java.util.function.BiFunction;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegisterEvent;
import conductance.api.recipe.IRecipe;
import conductance.api.recipe.IRecipeElementType;
import conductance.api.recipe.NCRecipeType;
import conductance.api.recipe.RecipeBuilder;
import conductance.api.recipe.RecipeElementCloner;
import conductance.api.recipe.RecipeHelper;
import conductance.api.recipe.event.RegisterRecipeTypeEvent;
import conductance.Conductance;
import conductance.core.register.RegisterCore;

public final class RecipeCore {

	public static void initialize(final IEventBus modEventBus) {
		Conductance.setApiValue(RecipeHelper.class, RecipeHelperImpl.INSTANCE);
		modEventBus.addListener(RegisterEvent.class, event -> {
			if (event.getRegistryKey() == Registries.RECIPE_TYPE) {
				RegisterCore.REGS.recipeTypes().values().forEach(recipeType -> {
					event.register(Registries.RECIPE_TYPE, recipeType.getRegistryKey(), () -> recipeType);
				});
			}
			if (event.getRegistryKey() == Registries.RECIPE_SERIALIZER) {
				RegisterCore.REGS.recipeTypes().forEach(recipeType -> {
					event.register(Registries.RECIPE_SERIALIZER, recipeType.getRegistryKey(), RecipeSerializerImpl::new);
				});
			}
		});
		RecipeCore.initElementTypes();
		RecipeCore.initTypes();
	}

	private static void initElementTypes() {
		Conductance.dispatch(RegisterRecipeElementTypeEventImpl.class, modid -> new RegisterRecipeElementTypeEventImpl(new RegisterRecipeElementTypeEventImpl.RecipeElementTypeRegister() {

			@Override
			public <T> IRecipeElementType<T> register(final String registryName, final Codec<T> dataCodec, final StreamCodec<RegistryFriendlyByteBuf, T> dataStreamCodec, final RecipeElementCloner<T> cloner) {
				final ResourceLocation registryKey = ResourceLocation.fromNamespaceAndPath(modid, registryName);
				return Util.make(new RecipeElementTypeSerializer<>(registryKey, dataCodec, dataStreamCodec, cloner), result -> {
					RegisterCore.REGS.recipeElementTypes().register(result);
				});
			}
		}));
	}

	private static void initTypes() {
		Conductance.dispatch(RegisterRecipeTypeEvent.class, modid -> new RegisterRecipeTypeEventImpl((registryName, builder) -> {
			final RecipeTypeBuilderImpl b = new RecipeTypeBuilderImpl(ResourceLocation.fromNamespaceAndPath(modid, registryName));
			builder.accept(b);
			return b.build();
		}));
	}

	public static BiFunction<NCRecipeType, ResourceLocation, RecipeBuilder> getRecipeBuilderFactory() {
		return RecipeBuilderImpl::new;
	}

	public static MapCodec<IRecipe> getRecipeCodec() {
		return RecipeSerializerImpl.CODEC;
	}

	private RecipeCore() {
	}
}
