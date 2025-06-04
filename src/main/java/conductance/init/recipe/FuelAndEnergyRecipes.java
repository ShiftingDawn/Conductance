package conductance.init.recipe;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import conductance.api.NCMaterialTaggedSets;
import conductance.api.NCMaterials;
import conductance.api.NCRecipeTypes;
import conductance.api.NCTiers;
import conductance.api.plugin.RegisterRecipeEvent;

final class FuelAndEnergyRecipes {

	public static void add(final RegisterRecipeEvent event) {
		FuelAndEnergyRecipes.getFurnaceFuels().forEach((item, burnTime) -> {
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

	@SuppressWarnings("deprecation")
	private static Map<Item, Integer> getFurnaceFuels() {
		return Util.make(new HashMap<>(), map -> {
			map.putAll(FurnaceBlockEntity.getFuel());
			BuiltInRegistries.ITEM.forEach(item -> {
				final int fuelTime = item.getDefaultInstance().getBurnTime(RecipeType.SMELTING);
				if (fuelTime > 0) {
					map.put(item, fuelTime);
				}
			});
		});
	}

	private FuelAndEnergyRecipes() {
	}
}
