package conductance.runtimepack.server;

import net.minecraft.core.HolderLookup;
import conductance.Conductance;
import conductance.runtimepack.server.recipe.DynamicRecipeHandler;
import conductance.core.register.MaterialRegistry;

public final class RuntimeDataPackBridge {

	public static void reload(final HolderLookup.Provider provider) {
		final long sysTime = System.currentTimeMillis();
		MaterialRegistry.INSTANCE.reload();

		DynamicRecipeHandler.addRecipes(provider);
		DynamicRecipeHandler.removeRecipes();
		Conductance.LOGGER.info("Conductance reloaded RuntimeDataPack in {}ms", System.currentTimeMillis() - sysTime);
	}

	private RuntimeDataPackBridge() {
	}
}
