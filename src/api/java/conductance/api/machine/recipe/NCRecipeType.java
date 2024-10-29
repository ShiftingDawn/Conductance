package conductance.api.machine.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import conductance.api.registry.IRegistryObject;

public interface NCRecipeType extends IRegistryObject<ResourceLocation>, RecipeType<IRecipe> {

	NCRecipeSerializer getSerializer();

	int getMaxInputs(IRecipeElementType<?> elementType);

	int getMaxOutputs(IRecipeElementType<?> elementType);
}
