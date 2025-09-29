package conductance.core.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import lombok.Getter;
import conductance.api.recipe.MachineRecipe;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.recipe.RecipeElementType;
import conductance.api.util.IO;

final class MachineRecipeTypeImpl implements MachineRecipeType {

	private final Object2IntMap<RecipeElementType<?>> inputLimits;
	private final Object2IntMap<RecipeElementType<?>> outputLimits;
	private final @Getter ResourceLocation guiArrow;

	MachineRecipeTypeImpl(final Object2IntMap<RecipeElementType<?>> inputLimits, final Object2IntMap<RecipeElementType<?>> outputLimits, final ResourceLocation guiArrow) {
		this.inputLimits = Object2IntMaps.unmodifiable(inputLimits);
		this.outputLimits = Object2IntMaps.unmodifiable(outputLimits);
		this.guiArrow = guiArrow;
	}

	@Override
	public RecipeSerializer<MachineRecipe> getRecipeSerializer() {
		return MachineRecipeSerializer.INSTANCE;
	}

	@Override
	public int getLimit(final IO io, final RecipeElementType<?> elementType) {
		return switch (io) {
			case IN -> this.inputLimits.getInt(elementType);
			case OUT -> this.outputLimits.getInt(elementType);
		};
	}
}
