package conductance.core.recipe;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import conductance.api.CAPI;
import conductance.api.NCRecipeElementTypes;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.machine.recipe.RecipeTypeBuilder;

public class RecipeTypeBuilderImpl implements RecipeTypeBuilder {

	private final Object2IntArrayMap<IRecipeElementType<?>> maxInputs = new Object2IntArrayMap<>();
	private final Object2IntArrayMap<IRecipeElementType<?>> maxOutputs = new Object2IntArrayMap<>();
	private final ResourceLocation registryKey;

	public RecipeTypeBuilderImpl(final ResourceLocation registryKey) {
		this.registryKey = registryKey;
	}

	@Override
	public RecipeTypeBuilder setIO(final int maxItemsIn, final int maxFluidsIn, final int maxItemsOut, final int maxFluidsOut) {
		return this.setIO(true, NCRecipeElementTypes.ITEM, maxItemsIn)
				.setIO(true, NCRecipeElementTypes.FLUID, maxFluidsIn)
				.setIO(false, NCRecipeElementTypes.ITEM, maxItemsOut)
				.setIO(false, NCRecipeElementTypes.FLUID, maxFluidsOut);
	}

	@Override
	public RecipeTypeBuilder setIO(final boolean input, final IRecipeElementType<?> type, final int max) {
		(input ? this.maxInputs : this.maxOutputs).put(type, max);
		return this;
	}

	@Override
	public NCRecipeType build() {
		final NCRecipeType result = new RecipeTypeImpl(
				this.registryKey,
				this.maxInputs,
				this.maxOutputs
		);
		CAPI.regs().recipeTypes().register(result.getRegistryKey(), result);
		return result;
	}
}
