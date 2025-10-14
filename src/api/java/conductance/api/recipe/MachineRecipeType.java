package conductance.api.recipe;

import java.util.List;
import java.util.Objects;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import com.mojang.serialization.Codec;
import conductance.api.CAPI;
import conductance.api.machine.gui.ProgressProvider;
import conductance.api.util.IO;

public interface MachineRecipeType extends RecipeType<MachineRecipe> {

	Codec<MachineRecipeType> CODEC = ResourceLocation.CODEC.xmap(CAPI.regs().recipeTypes()::getValue, MachineRecipeType::getId);

	int getLimit(IO io, RecipeElementType<?> elementType);

	ResourceLocation getGuiArrow();

	ProgressProvider.Direction getGuiArrowDirection();

	RecipeSerializer<MachineRecipe> getRecipeSerializer();

	RecipeBookCategory getRecipeBookCategory();

	List<RecipeHolder<MachineRecipe>> getRecipes();

	String getDescriptionId();

	Component getName();

	default ResourceLocation getId() {
		return Objects.requireNonNull(CAPI.regs().recipeTypes().getKey(this), "Unregistered recipe type");
	}
}
