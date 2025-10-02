package conductance.core.recipe;

import java.util.List;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeMap;
import com.mojang.serialization.Codec;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.recipe.RecipeElementType;
import conductance.api.recipe.RecipeHelper;
import conductance.api.recipe.event.RecipeBuilderCallback;
import conductance.api.recipe.event.RegisterRecipeElementTypeEvent;
import conductance.api.recipe.event.RegisterRecipeTypeEvent;
import conductance.Conductance;

public final class RecipeCore {

	public static void initialize() {
		Conductance.setApiValue(RecipeHelper.class, new RecipeHelper());
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
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Conductance.id("machine"), MachineRecipeSerializer.INSTANCE);
		Conductance.dispatch(RegisterRecipeTypeEvent.class, modid -> new RegisterRecipeTypeEventImpl((registryName, builder) -> {
			final ResourceLocation registryKey = ResourceLocation.fromNamespaceAndPath(modid, registryName);
			final MachineRecipeTypeImpl result = Util.make(new RecipeTypeBuilderImpl(), builder).build();
			Conductance.REGISTRIES.register(CAPI.regs().recipeTypes(), registryKey, result);
			Registry.register(BuiltInRegistries.RECIPE_TYPE, registryKey, result);
			return result;
		}));
	}

	public static void processRecipes(final RecipeMap recipeMap) {
		//Called from RecipeManagerMixin
		MachineRecipeTypeImpl.ALL_RECIPES.clear();
		for (final MachineRecipeType recipeType : CAPI.regs().recipeTypes()) {
			MachineRecipeTypeImpl.ALL_RECIPES.put(recipeType, List.copyOf(recipeMap.byType(recipeType)));
		}
	}

	public static @Nullable RecipeBuilderCallback getRecipeBuilderCallback(final MachineRecipeType recipeType) {
		return recipeType instanceof final MachineRecipeTypeImpl impl ? impl.getRecipeBuilderCallback() : null;
	}

	private RecipeCore() {
	}
}
