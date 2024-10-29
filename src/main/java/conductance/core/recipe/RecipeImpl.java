package conductance.core.recipe;

import net.minecraft.world.item.crafting.RecipeType;
import lombok.Getter;
import conductance.api.machine.recipe.IRecipe;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.machine.recipe.RecipeElementMap;

public class RecipeImpl implements IRecipe {

	@Getter
	private final NCRecipeType type;
	@Getter
	private final RecipeElementMap inputs;
	@Getter
	private final RecipeElementMap inputsPerTick;
	@Getter
	private final RecipeElementMap outputs;
	@Getter
	private final RecipeElementMap outputsPerTick;

	public RecipeImpl(
			final RecipeType<?> recipeType,
			final RecipeElementMap inputs, final RecipeElementMap inputsPerTick,
			final RecipeElementMap outputs, final RecipeElementMap outputsPerTick
	) {
		if (!(recipeType instanceof NCRecipeType)) {
			throw new RuntimeException("RecipeType %s is not an instance of %s".formatted(recipeType.getClass().getName(), NCRecipeType.class.getName()));
		}
		this.type = (NCRecipeType) recipeType;
		this.inputs = ((RecipeElementMapImpl) inputs).immutableCopy();
		this.inputsPerTick = ((RecipeElementMapImpl) inputsPerTick).immutableCopy();
		this.outputs = ((RecipeElementMapImpl) outputs).immutableCopy();
		this.outputsPerTick = ((RecipeElementMapImpl) outputsPerTick).immutableCopy();
	}
}
