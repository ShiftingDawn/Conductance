package conductance.runtimepack.server;

import net.minecraft.core.HolderLookup;
import conductance.Conductance;
import conductance.core.material.MaterialRegistryImpl;
import conductance.runtimepack.server.recipe.DynamicRecipeHandler;

public final class RuntimeDataPackBridge {

	public static void reload(final HolderLookup.Provider provider) {
		final long sysTime = System.currentTimeMillis();
		MaterialRegistryImpl.INSTANCE.reload();

		DynamicRecipeHandler.addRecipes(provider);
		LootTableGenerationHandler.generate(provider);

		Conductance.LOGGER.info("Conductance reloaded RuntimeDataPack in {}ms", System.currentTimeMillis() - sysTime);
	}

	private RuntimeDataPackBridge() {
	}
}
