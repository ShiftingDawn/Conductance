package conductance.init.recipe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import conductance.api.CAPI;
import conductance.api.NCBlocks;
import conductance.api.NCItems;
import conductance.api.NCMachines;
import conductance.api.recipe.event.RegisterRecipeEvent;
import conductance.api.tier.Tier;
import conductance.api.tier.TieredComponentMap;
import conductance.api.tier.TieredItemType;
import static conductance.init.recipe.RecipeLoader.shaped;

final class TierRecipes {

	public static void add(final RegisterRecipeEvent event) {
		CAPI.regs().tiers().forEach(tier -> TierRecipes.addTierRecipes(event, tier, tier.getComponentMap()));
	}

	private static void addTierRecipes(final RegisterRecipeEvent event, final Tier tier, final TieredComponentMap map) {
		shaped(event, NCBlocks.MACHINE_CASING.get(tier).getId().getPath(), NCBlocks.MACHINE_CASING.get(tier).asStack(),
				"AAA", "AWA", "AAA", 'A', map.getMachineCasingPlateItem());
		shaped(event, BuiltInRegistries.BLOCK.getKey(NCMachines.MACHINE_HULL.get(tier).getBlock().get()).getPath(), new ItemStack(NCMachines.MACHINE_HULL.get(tier).getBlock().get()),
				"AAA", "BCB", 'A', map.getMachineHullPlateItem(), 'B', map.getMachineHullWireItem(), 'C', NCBlocks.MACHINE_CASING.get(tier).asStack());
		shaped(event, TieredItemType.ELECTRIC_MOTOR.makeUnlocalizedName(tier), NCItems.tiered(TieredItemType.ELECTRIC_MOTOR, tier, 1),
				" AB", "ACA", "BA ", 'A', map.getElectricMotorWireItem(), 'B', map.getElectricMotorRodItem(), 'C', map.getElectricMotorMagneticRodItem());
		shaped(event, TieredItemType.ELECTRIC_PISTON.makeUnlocalizedName(tier), NCItems.tiered(TieredItemType.ELECTRIC_PISTON, tier, 1),
				"AAA", "BCC", "BDE", 'A', map.getElectricPistonPlateItem(), 'B', map.getElectricPistonWireItem(), 'C', map.getElectricPistonRodItem(), 'D', NCItems.tiered(TieredItemType.ELECTRIC_MOTOR, tier, 1),
				'E', map.getElectricPistonSmallGearItem());
		shaped(event, TieredItemType.CONVEYOR_MODULE.makeUnlocalizedName(tier), NCItems.tiered(TieredItemType.CONVEYOR_MODULE, tier, 1),
				"AAA", "BCB", "AAA", 'A', map.getConveyorModulePlateItem(), 'B', NCItems.tiered(TieredItemType.ELECTRIC_MOTOR, tier, 1), 'C', map.getConveyorModuleWireItem());
		shaped(event, TieredItemType.ELECTRIC_PUMP.makeUnlocalizedName(tier), NCItems.tiered(TieredItemType.ELECTRIC_PUMP, tier, 1),
				"EDA", "DBD", "ACE", 'A', map.getElectricPumpRingItem(), 'B', map.getElectricPumpRotorItem(), 'C', NCItems.tiered(TieredItemType.ELECTRIC_MOTOR, tier, 1), 'D', map.getElectricPumpPlateItem(),
				'E', map.getElectricPumpWireItem());
		shaped(event, TieredItemType.ROBOT_ARM.makeUnlocalizedName(tier), NCItems.tiered(TieredItemType.ROBOT_ARM, tier, 1),
				"ABC", "DEB", "AFG", 'A', map.getRobotArmWireItem(), 'B', map.getRobotArmRodItem(), 'C', NCItems.tiered(TieredItemType.CIRCUIT, tier, 1), 'D', map.getRobotArmPlateItem(),
				'E', NCItems.tiered(TieredItemType.ELECTRIC_PISTON, tier, 1), 'F', NCItems.tiered(TieredItemType.ELECTRIC_MOTOR, tier, 1), 'G', map.getRobotArmGearItem());
	}

	private TierRecipes() {
	}
}
