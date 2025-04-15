package conductance.runtimepack.server.recipe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import conductance.api.NCMaterialTaggedSets;
import conductance.api.NCMaterials;
import conductance.api.NCRecipeTypes;
import conductance.api.NCTiers;
import conductance.api.plugin.RecipeBuilderFactory;
import conductance.api.util.MiscUtils;
import conductance.Conductance;

final class FuelAndEnergyRecipes {

	public static void add(final RecipeOutput output, final RecipeBuilderFactory builderFactory) {
		MiscUtils.getFurnaceFuels().forEach((item, burnTime) -> {
			builderFactory.build(NCRecipeTypes.STEAM_BOILER, BuiltInRegistries.ITEM.getKey(item))
					.in(item)
					.processTime(burnTime * 12)
					.save(output);
		});
		builderFactory.build(NCRecipeTypes.STEAM_BOILER, Conductance.id("lava"))
				.in(new FluidStack(Fluids.LAVA, 100))
				.processTime(600 * 12)
				.save(output);

		builderFactory.build(NCRecipeTypes.STEAM_TURBINE, Conductance.id("steam"))
				.in(NCMaterialTaggedSets.GAS, NCMaterials.STEAM, 640)
				.processTime(10)
				.outEnergy(NCTiers.LV.getVoltage())
				.save(output);
	}

	private FuelAndEnergyRecipes() {
	}
}
