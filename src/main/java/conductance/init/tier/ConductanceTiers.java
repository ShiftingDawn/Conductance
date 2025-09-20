package conductance.init.tier;

import net.minecraft.ChatFormatting;
import conductance.api.CAPI;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddTranslationEvent;
import conductance.api.tier.TieredItemType;
import conductance.api.tier.event.RegisterTierEvent;
import conductance.Conductance;
import static conductance.api.NCTiers.LV;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceTiers {

	@EventListener
	private static void init(final RegisterTierEvent event) {
		LV = event.register("lv", 0x004fff, LvComponentMap::new);
	}

	@EventListener(priority = -100)
	private static void addTierTranslations(final AddTranslationEvent event) {
		event.add(CAPI.tiers().empty(), "Empty");
		event.add(CAPI.tiers().empty(), ChatFormatting.BOLD + "MAX");
		event.add(LV, ChatFormatting.DARK_BLUE + "LV");

		event.add(TieredItemType.CIRCUIT.getDescriptionId(), "%s Circuit");
		event.add(TieredItemType.ADVANCED_CIRCUIT.getDescriptionId(), "Advanced %s Circuit");
		event.add(TieredItemType.ELECTRIC_MOTOR.getDescriptionId(), "%s Circuit");
		event.add(TieredItemType.ELECTRIC_PISTON.getDescriptionId(), "%s Electric Motor");
		event.add(TieredItemType.CONVEYOR_MODULE.getDescriptionId(), "%s Conveyor Module");
		event.add(TieredItemType.ELECTRIC_PUMP.getDescriptionId(), "%s Electric Pump");
		event.add(TieredItemType.ROBOT_ARM.getDescriptionId(), "%s Robot Arm");
	}

	private ConductanceTiers() {
	}
}
