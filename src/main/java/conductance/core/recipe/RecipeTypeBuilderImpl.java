package conductance.core.recipe;

import net.minecraft.resources.ResourceLocation;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import conductance.api.recipe.RecipeElementType;
import conductance.api.recipe.event.RecipeTypeBuilder;
import conductance.api.util.IO;
import conductance.Conductance;

final class RecipeTypeBuilderImpl implements RecipeTypeBuilder {

	private final Object2IntMap<RecipeElementType<?>> inputLimits = new Object2IntArrayMap<>();
	private final Object2IntMap<RecipeElementType<?>> outputLimits = new Object2IntArrayMap<>();
	private ResourceLocation guiArrow = Conductance.id("conductance/progress_bars/generic_arrow");

	@Override
	public RecipeTypeBuilder setIO(final IO io, final RecipeElementType<?> type, final int limit) {
		switch (io) {
			case IN -> this.inputLimits.put(type, limit);
			case OUT -> this.outputLimits.put(type, limit);
		}
		return this;
	}

	@Override
	public RecipeTypeBuilder guiArrow(final ResourceLocation arrowTexture) {
		this.guiArrow = arrowTexture.withPrefix("conductance/progress_bars/");
		return this;
	}

	public MachineRecipeTypeImpl build() {
		return new MachineRecipeTypeImpl(this.inputLimits, this.outputLimits, this.guiArrow);
	}
}
