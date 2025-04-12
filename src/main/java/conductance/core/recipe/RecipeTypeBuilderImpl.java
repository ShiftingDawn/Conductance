package conductance.core.recipe;

import net.minecraft.resources.ResourceLocation;
import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.NCRecipeElementTypes;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.machine.recipe.RecipeTypeBuilder;
import conductance.Conductance;

public class RecipeTypeBuilderImpl implements RecipeTypeBuilder {

	private final ResourceLocation registryKey;
	private final Object2IntArrayMap<IRecipeElementType<?>> maxInputs = new Object2IntArrayMap<>();
	private final Object2IntArrayMap<IRecipeElementType<?>> maxOutputs = new Object2IntArrayMap<>();
	private ResourceLocation progressBar;
	@Nullable
	private ResourceLocation recipeViewProgressBar = null;
	private ProgressTexture.FillDirection progressBarDirection = ProgressTexture.FillDirection.LEFT_TO_RIGHT;

	public RecipeTypeBuilderImpl(final ResourceLocation registryKey) {
		this.registryKey = registryKey;
		this.progressBar = registryKey;
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
	public RecipeTypeBuilder setProgressBar(final String name, final ProgressTexture.FillDirection direction) {
		this.progressBar = this.registryKey.withPath(name);
		this.progressBarDirection = direction;
		return this;
	}

	@Override
	public RecipeTypeBuilder setProgressBarDirection(final ProgressTexture.FillDirection direction) {
		this.progressBarDirection = direction;
		return this;
	}

	@Override
	public RecipeTypeBuilder setRecipeViewProgressBar(final String name) {
		this.recipeViewProgressBar = this.registryKey.withPath(name);
		return this;
	}

	@Override
	public NCRecipeType build() {
		ResourceLocation realProgressBar = this.progressBar.withPrefix("textures/gui/progress_bars/").withSuffix(".png");
		if (!CAPI.resourceFinder().isResourceValid(realProgressBar)) {
			Conductance.LOGGER.warn("Could not find progressbar texture {}, falling back to generic_arrow", realProgressBar);
			realProgressBar = this.progressBar.withPath("textures/gui/progress_bars/generic_arrow.png");
		}
		final ResourceLocation realRecipeViewProgressBar = this.recipeViewProgressBar == null ? realProgressBar : this.recipeViewProgressBar.withPrefix("textures/gui/progress_bars/").withSuffix(".png");

		final NCRecipeType result = new RecipeTypeImpl(
				this.registryKey,
				this.maxInputs,
				this.maxOutputs,
				realProgressBar,
				realRecipeViewProgressBar,
				this.progressBarDirection
		);
		CAPI.regs().recipeTypes().register(result.getRegistryKey(), result);
		return result;
	}
}
