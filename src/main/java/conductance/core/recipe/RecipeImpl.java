package conductance.core.recipe;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.world.item.crafting.RecipeType;
import lombok.Getter;
import conductance.api.machine.recipe.IRecipe;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.machine.recipe.RecipeElement;

public class RecipeImpl implements IRecipe {

	@Getter
	private final NCRecipeType type;
	@Getter
	private final Map<IRecipeElementType<?>, List<RecipeElement>> inputs;
	@Getter
	private final Map<IRecipeElementType<?>, List<RecipeElement>> inputsPerTick;
	@Getter
	private final Map<IRecipeElementType<?>, List<RecipeElement>> outputs;
	@Getter
	private final Map<IRecipeElementType<?>, List<RecipeElement>> outputsPerTick;

	public RecipeImpl(
			final RecipeType<?> recipeType,
			final Map<IRecipeElementType<?>, List<RecipeElement>> inputs, final Map<IRecipeElementType<?>, List<RecipeElement>> inputsPerTick,
			final Map<IRecipeElementType<?>, List<RecipeElement>> outputs, final Map<IRecipeElementType<?>, List<RecipeElement>> outputsPerTick
	) {
		if (!(recipeType instanceof NCRecipeType)) {
			throw new RuntimeException("RecipeType %s is not an instance of %s".formatted(recipeType.getClass().getName(), NCRecipeType.class.getName()));
		}
		this.type = (NCRecipeType) recipeType;
		this.inputs = RecipeImpl.makeImmutable(inputs);
		this.inputsPerTick = RecipeImpl.makeImmutable(inputsPerTick);
		this.outputs = RecipeImpl.makeImmutable(outputs);
		this.outputsPerTick = RecipeImpl.makeImmutable(outputsPerTick);
	}

	private static Map<IRecipeElementType<?>, List<RecipeElement>> makeImmutable(final Map<IRecipeElementType<?>, List<RecipeElement>> map) {
		return Collections.unmodifiableMap(Util.make(new HashMap<>(map.size()), newMap -> map.forEach((key, value) -> newMap.put(key, Collections.unmodifiableList(value)))));
	}
}
