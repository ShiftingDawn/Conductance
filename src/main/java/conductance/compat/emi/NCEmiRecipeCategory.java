package conductance.compat.emi;

import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import org.apache.commons.lang3.ArrayUtils;
import conductance.api.CAPI;
import conductance.api.machine.recipe.NCRecipeType;

final class NCEmiRecipeCategory extends EmiRecipeCategory {

	static final Function<NCRecipeType, NCEmiRecipeCategory> CATEGORIES = Util.memoize(NCEmiRecipeCategory::new);
	private final NCRecipeType recipeType;

	NCEmiRecipeCategory(final NCRecipeType recipeType) {
		super(recipeType.getRegistryKey(), EmiStack.of(recipeType.getRecipeTypeIcon().get()));
		this.recipeType = recipeType;
	}

	@Override
	public Component getName() {
		return this.recipeType.getName();
	}

	public static void register(final EmiRegistry registry) {
		CAPI.regs().recipeTypes().values().forEach(recipeType -> {
			final NCEmiRecipeCategory category = NCEmiRecipeCategory.CATEGORIES.apply(recipeType);
			registry.addCategory(category);
			registry.getRecipeManager().getAllRecipesFor(recipeType).stream()
					.map(recipe -> new NCEmiRecipe(category, recipe))
					.forEach(registry::addRecipe);
			CAPI.regs().machines().values().stream()
					.filter(machineType -> ArrayUtils.contains(machineType.getRecipeTypes(), recipeType))
					.forEach(machineType ->
							registry.addWorkstation(category, EmiStack.of(new ItemStack(machineType.getBlock().get())))
					);
		});
	}
}
