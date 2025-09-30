package conductance.core.recipe;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import lombok.Getter;
import conductance.api.machine.gui.ProgressProvider;
import conductance.api.recipe.MachineRecipe;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.recipe.RecipeElementType;
import conductance.api.util.IO;

final class MachineRecipeTypeImpl implements MachineRecipeType {

	static final Map<MachineRecipeType, List<RecipeHolder<MachineRecipe>>> ALL_RECIPES = new ConcurrentHashMap<>();
	private final Object2IntMap<RecipeElementType<?>> inputLimits;
	private final Object2IntMap<RecipeElementType<?>> outputLimits;
	private final @Getter ResourceLocation guiArrow;
	private final @Getter ProgressProvider.Direction guiArrowDirection;

	MachineRecipeTypeImpl(
		final Object2IntMap<RecipeElementType<?>> inputLimits, final Object2IntMap<RecipeElementType<?>> outputLimits,
		final ResourceLocation guiArrow, final ProgressProvider.Direction guiArrowDirection
	) {
		this.inputLimits = Object2IntMaps.unmodifiable(inputLimits);
		this.outputLimits = Object2IntMaps.unmodifiable(outputLimits);
		this.guiArrow = guiArrow;
		this.guiArrowDirection = guiArrowDirection;
	}

	@Override
	public RecipeSerializer<MachineRecipe> getRecipeSerializer() {
		return MachineRecipeSerializer.INSTANCE;
	}

	@Override
	public List<RecipeHolder<MachineRecipe>> getRecipes() {
		return MachineRecipeTypeImpl.ALL_RECIPES.getOrDefault(this, List.of());
	}

	@Override
	public int getLimit(final IO io, final RecipeElementType<?> elementType) {
		return switch (io) {
			case IN -> this.inputLimits.getInt(elementType);
			case OUT -> this.outputLimits.getInt(elementType);
		};
	}
}
