package conductance.init.recipe;

import conductance.api.CAPI;
import conductance.api.NCItems;
import conductance.api.recipe.event.RegisterRecipeEvent;
import conductance.api.tier.Tier;
import conductance.api.tier.TieredComponentMap;
import conductance.api.tier.TieredItemType;

final class TierRecipes {

	public static void add(final RegisterRecipeEvent event) {
		CAPI.regs().tiers().forEach(tier -> TierRecipes.addTierRecipes(event, tier, tier.getComponentMap()));
	}

	private static void addTierRecipes(final RegisterRecipeEvent event, final Tier tier, final TieredComponentMap map) {
		event.shaped(TieredItemType.ELECTRIC_MOTOR.getUnlocalizedName(tier), NCItems.tiered(TieredItemType.ELECTRIC_MOTOR, tier, 1), b -> b
			.pattern(" ab", "aca", "ba ").key('a', map.getElectricMotorWireItem()).key('b', map.getElectricMotorRodItem()).key('c', map.getElectricMotorMagneticRodItem()));
		event.shaped(TieredItemType.ELECTRIC_PISTON.getUnlocalizedName(tier), NCItems.tiered(TieredItemType.ELECTRIC_PISTON, tier, 1), b -> b
			.pattern("aaa", "bcc", "bde").key('a', map.getElectricPistonPlateItem()).key('b', map.getElectricPistonWireItem()).key('c', map.getElectricPistonRodItem())
			.key('d', NCItems.tiered(TieredItemType.ELECTRIC_MOTOR, tier, 1)).key('e', map.getElectricPistonSmallGearItem()));
		event.shaped(TieredItemType.CONVEYOR_MODULE.getUnlocalizedName(tier), NCItems.tiered(TieredItemType.CONVEYOR_MODULE, tier, 1), b -> b
			.pattern("aaa", "bcb", "aaa").key('a', map.getConveyorModulePlateItem()).key('b', NCItems.tiered(TieredItemType.ELECTRIC_MOTOR, tier, 1)).key('c', map.getConveyorModuleWireItem()));
		event.shaped(TieredItemType.ELECTRIC_PUMP.getUnlocalizedName(tier), NCItems.tiered(TieredItemType.ELECTRIC_PUMP, tier, 1), b -> b
			.pattern("eda", "dbd", "ace").key('a', map.getElectricPumpRingItem()).key('b', map.getElectricPumpRotorItem()).key('c', NCItems.tiered(TieredItemType.ELECTRIC_MOTOR, tier, 1))
			.key('d', map.getElectricPumpPlateItem()).key('e', map.getElectricPumpWireItem()));
		event.shaped(TieredItemType.ROBOT_ARM.getUnlocalizedName(tier), NCItems.tiered(TieredItemType.ROBOT_ARM, tier, 1), b -> b
			.pattern("abc", "deb", "afg").key('a', map.getRobotArmWireItem()).key('b', map.getRobotArmRodItem()).key('c', NCItems.tiered(TieredItemType.CIRCUIT, tier, 1)).key('d', map.getRobotArmPlateItem())
			.key('e', NCItems.tiered(TieredItemType.ELECTRIC_PISTON, tier, 1)).key('f', NCItems.tiered(TieredItemType.ELECTRIC_MOTOR, tier, 1)).key('g', map.getRobotArmGearItem()));
	}

	private TierRecipes() {
	}
}
