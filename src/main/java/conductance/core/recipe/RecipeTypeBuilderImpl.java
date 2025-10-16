package conductance.core.recipe;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.gui.ProgressProvider;
import conductance.api.recipe.RecipeDataToken;
import conductance.api.recipe.RecipeElementType;
import conductance.api.recipe.event.RecipeBuilderCallback;
import conductance.api.recipe.event.RecipeTypeBuilder;
import conductance.api.util.IO;
import conductance.Conductance;

final class RecipeTypeBuilderImpl implements RecipeTypeBuilder {

	private final Object2IntMap<RecipeElementType<?>> inputLimits = new Object2IntArrayMap<>();
	private final Object2IntMap<RecipeElementType<?>> outputLimits = new Object2IntArrayMap<>();
	private final Set<RecipeDataToken<?>> additionalDataTokens = new HashSet<>();
	private ResourceLocation guiArrow = Conductance.id("conductance/progress_bars/generic_arrow");
	private ProgressProvider.Direction guiArrowDirection = ProgressProvider.Direction.LEFT_TO_RIGHT;
	private boolean hidden = false;
	private @Nullable RecipeBuilderCallback recipeBuilderCallback = null;

	@Override
	public RecipeTypeBuilder setIO(final IO io, final RecipeElementType<?> type, final int limit) {
		switch (io) {
			case IN -> this.inputLimits.put(type, limit);
			case OUT -> this.outputLimits.put(type, limit);
		}
		return this;
	}

	@Override
	public RecipeTypeBuilder data(final RecipeDataToken<?> token) {
		this.additionalDataTokens.add(token);
		return this;
	}

	@Override
	public RecipeTypeBuilder guiArrow(final ResourceLocation arrowTexture, final ProgressProvider.Direction direction) {
		this.guiArrow = arrowTexture.withPrefix("conductance/progress_bars/");
		this.guiArrowDirection = direction;
		return this;
	}

	@Override
	public RecipeTypeBuilder hidden() {
		this.hidden = true;
		return this;
	}

	@Override
	public RecipeTypeBuilder recipeBuilderCallback(final RecipeBuilderCallback callback) {
		this.recipeBuilderCallback = callback;
		return this;
	}

	public MachineRecipeTypeImpl build() {
		final Map<String, RecipeDataToken<?>> additionalDataTokenMap = CAPI.make(new HashMap<>(),
			map -> this.additionalDataTokens.forEach(token -> map.put(token.name(), token)));
		return new MachineRecipeTypeImpl(this.inputLimits, this.outputLimits, additionalDataTokenMap, this.guiArrow, this.guiArrowDirection, this.hidden, this.recipeBuilderCallback);
	}
}
