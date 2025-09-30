package conductance.api.recipe;

import java.util.List;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import com.mojang.serialization.Codec;
import conductance.api.CAPI;
import conductance.api.util.IO;

public interface MachineRecipeType extends RecipeType<MachineRecipe> {

	Codec<MachineRecipeType> CODEC = ResourceLocation.CODEC.xmap(CAPI.regs().recipeTypes()::getValue, MachineRecipeType::getId);

	int getLimit(IO io, RecipeElementType<?> elementType);

	ResourceLocation getGuiArrow();

	RecipeSerializer<MachineRecipe> getRecipeSerializer();

	List<RecipeHolder<MachineRecipe>> getRecipes();

	default ResourceLocation getId() {
		return Objects.requireNonNull(CAPI.regs().recipeTypes().getKey(this), "Unregistered recipe type");
	}
}
