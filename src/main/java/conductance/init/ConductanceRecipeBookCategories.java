package conductance.init;

import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import conductance.Conductance;

public final class ConductanceRecipeBookCategories {

	private static final DeferredRegister<RecipeBookCategory> REGISTRY = DeferredRegister.create(BuiltInRegistries.RECIPE_BOOK_CATEGORY, Conductance.MODID);
	public static final Supplier<RecipeBookCategory> ALL = ConductanceRecipeBookCategories.REGISTRY.register(Conductance.MODID, RecipeBookCategory::new);

	public static void initialize(final IEventBus modEventBus) {
		ConductanceRecipeBookCategories.REGISTRY.register(modEventBus);
	}

	private ConductanceRecipeBookCategories() {
	}
}
