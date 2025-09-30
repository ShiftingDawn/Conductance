package conductance.compat.jei;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import conductance.api.CAPI;
import conductance.api.machine.MachineType;
import conductance.api.recipe.MachineRecipeType;
import conductance.Conductance;

@JeiPlugin
public final class ConductanceJeiPlugin implements IModPlugin {

	public static final ResourceLocation UID = Conductance.id(Conductance.MODID);

	@Override
	public void registerCategories(final IRecipeCategoryRegistration registration) {
		final List<IRecipeCategory<?>> categories = new ArrayList<>();
		for (final MachineType<?> machineType : CAPI.regs().machines()) {
			categories.add(new GenericRecipeMachineCategory(machineType, GenericRecipeMachineCategory.RECIPE_TYPES.apply(machineType.getRecipeTypes()[0])));
		}
		registration.addRecipeCategories(categories.toArray(IRecipeCategory[]::new));
	}

	@Override
	public void registerRecipes(final IRecipeRegistration registration) {
		for (final MachineRecipeType recipeType : CAPI.regs().recipeTypes()) {
			registration.addRecipes(GenericRecipeMachineCategory.RECIPE_TYPES.apply(recipeType), recipeType.getRecipes().stream().map(RecipeHolder::value).toList());
		}
	}

	@Override
	public void registerRecipeCatalysts(final IRecipeCatalystRegistration registration) {
		for (final MachineType<?> machineType : CAPI.regs().machines()) {
			registration.addCraftingStation(GenericRecipeMachineCategory.RECIPE_TYPES.apply(machineType.getRecipeTypes()[0]), machineType.getBlock().get());
		}
	}

	@Override
	public ResourceLocation getPluginUid() {
		return ConductanceJeiPlugin.UID;
	}
}
