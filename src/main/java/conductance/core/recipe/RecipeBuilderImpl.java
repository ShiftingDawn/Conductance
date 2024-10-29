package conductance.core.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import conductance.api.machine.recipe.IRecipe;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.machine.recipe.RecipeBuilder;
import conductance.api.machine.recipe.RecipeElement;

public class RecipeBuilderImpl implements RecipeBuilder {

	private final Map<IRecipeElementType<?>, List<RecipeElement>> inputs = new HashMap<>();
	private final Map<IRecipeElementType<?>, List<RecipeElement>> inputsPerTick = new HashMap<>();
	private final Map<IRecipeElementType<?>, List<RecipeElement>> outputs = new HashMap<>();
	private final Map<IRecipeElementType<?>, List<RecipeElement>> outputsPerTick = new HashMap<>();
	private final NCRecipeType recipeType;
	private final ResourceLocation recipeId;
	private boolean perTick = false;
	private int chance = 100;
	private int maxChance = 100;

	public RecipeBuilderImpl(final NCRecipeType recipeType, final ResourceLocation recipeId) {
		this.recipeType = recipeType;
		this.recipeId = recipeId.withPrefix(recipeType.getRegistryKey().getPath() + "/");
	}

	@Override
	public RecipeBuilder perTick(final boolean newPerTick) {
		this.perTick = newPerTick;
		return this;
	}

	@Override
	public RecipeBuilder chance(final int newChance, final int newMaxChance) {
		this.chance = newChance;
		this.maxChance = newMaxChance;
		return this;
	}

	@Override
	public <T> RecipeBuilder add(final boolean input, final IRecipeElementType<T> type, final T obj) {
		final Map<IRecipeElementType<?>, List<RecipeElement>> map;
		if (this.perTick) {
			map = input ? this.inputsPerTick : this.outputsPerTick;
		} else {
			map = input ? this.inputs : this.outputs;
		}
		map.computeIfAbsent(type, k -> new ArrayList<>()).add(new RecipeElement(obj, this.chance, this.maxChance));
		return this;
	}

	@Override
	public IRecipe build() {
		return new RecipeImpl(
				this.recipeType,
				this.inputs,
				this.inputsPerTick,
				this.outputs,
				this.outputsPerTick
		);
	}

	@Override
	public void save(final RecipeOutput output) {
		output.accept(this.recipeId, this.build(), null);
	}
}
