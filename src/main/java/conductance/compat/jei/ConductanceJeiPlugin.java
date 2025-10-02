package conductance.compat.jei;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.NCDataComponents;
import conductance.api.NCItems;
import conductance.api.machine.MachineType;
import conductance.api.machine.gui.GuiTheme;
import conductance.api.machine.gui.MachineScreen;
import conductance.api.recipe.MachineRecipeType;
import conductance.Conductance;

@JeiPlugin
public final class ConductanceJeiPlugin implements IModPlugin {

	public static final ResourceLocation UID = Conductance.id(Conductance.MODID);
	private static @Nullable IJeiRuntime jeiRuntime = null;

	@Override
	public void registerItemSubtypes(final ISubtypeRegistration registration) {
		registration.registerFromDataComponentTypes(NCItems.PROGRAM_CIRCUIT.value(), NCDataComponents.PROGRAM_CIRCUIT.get());
	}

	@Override
	public void registerCategories(final IRecipeCategoryRegistration registration) {
		final List<IRecipeCategory<?>> categories = new ArrayList<>();
		for (final MachineType<?> machineType : CAPI.regs().machines()) {
			final GuiTheme theme = machineType.getGuiSetup() != null ? machineType.getGuiSetup().getTheme() : GuiTheme.THEME_DEFAULT;
			for (final MachineRecipeType recipeType : machineType.getRecipeTypes()) {
				categories.add(new GenericRecipeMachineCategory(recipeType, GenericRecipeMachineCategory.RECIPE_TYPES.apply(machineType.getRecipeTypes()[0]), theme));
			}
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
			for (final MachineRecipeType recipeType : machineType.getRecipeTypes()) {
				registration.addCraftingStation(GenericRecipeMachineCategory.RECIPE_TYPES.apply(recipeType), machineType.getBlock().get());
			}
		}
	}

	@Override
	public void registerGuiHandlers(final IGuiHandlerRegistration registration) {
		registration.addGuiContainerHandler(MachineScreen.class, new MachineScreenGuiHandler());
	}

	@Override
	public void onRuntimeAvailable(final IJeiRuntime runtime) {
		ConductanceJeiPlugin.jeiRuntime = runtime;
	}

	@Override
	public ResourceLocation getPluginUid() {
		return ConductanceJeiPlugin.UID;
	}

	public static void showRecipes(final MachineRecipeType recipeType) {
		if (ConductanceJeiPlugin.jeiRuntime != null) {
			ConductanceJeiPlugin.jeiRuntime.getRecipesGui().showTypes(List.of(GenericRecipeMachineCategory.RECIPE_TYPES.apply(recipeType)));
		}
	}
}
