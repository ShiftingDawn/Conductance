package conductance.init.recipe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import conductance.api.NCMaterialTaggedSets;
import conductance.api.NCMaterials;
import conductance.api.NCRecipeTypes;
import conductance.api.NCTiers;
import conductance.api.plugin.RegisterRecipeEvent;
import conductance.api.util.MiscUtils;

final class FuelAndEnergyRecipes {

	public static void add(final RegisterRecipeEvent event) {
		MiscUtils.getFurnaceFuels().forEach((item, burnTime) -> {
			event.create(NCRecipeTypes.STEAM_BOILER, BuiltInRegistries.ITEM.getKey(item).getPath(),
					builder -> builder.in(item).processTime(burnTime * 12)
			);
		});
		event.create(NCRecipeTypes.STEAM_BOILER, "lava", builder ->
				builder.in(new FluidStack(Fluids.LAVA, 100)).processTime(600 * 12)
		);
		event.create(NCRecipeTypes.STEAM_TURBINE, "steam", builder ->
				builder.in(NCMaterialTaggedSets.GAS, NCMaterials.STEAM, 640).processTime(10).outEnergy(NCTiers.LV.getVoltage())
		);
	}

	private FuelAndEnergyRecipes() {
	}
}
