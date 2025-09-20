package conductance.init;

import java.util.function.IntSupplier;
import conductance.api.CAPI;
import conductance.api.NCMaterialFlags;
import conductance.api.NCMaterialTraits;
import conductance.api.NCMaterials;
import conductance.api.material.Material;
import conductance.api.material.MaterialTraitFluid;
import conductance.api.material.event.ModifyMaterialEvent;
import conductance.api.material.event.RegisterMaterialEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddTranslationEvent;
import conductance.api.util.TextHelper;
import conductance.Conductance;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceMaterials {

	@EventListener(priority = -101)
	private static void initialize(final RegisterMaterialEvent event) {
		NCMaterials.AIR = event.register("air", b -> {
		});
	}

	@EventListener(priority = -100)
	private static void addMaterialTranslations(final AddTranslationEvent event) {
		for (final Material material : CAPI.regs().materials()) {
			event.add(material, TextHelper.lowerUnderscoreToEnglish(material.getName()));
		}
	}

	@EventListener(priority = -100)
	private static void modifyMaterials(final ModifyMaterialEvent event) {
		if (event.hasTrait(NCMaterialTraits.LIQUID)) {
			final MaterialTraitFluid.Liquid trait = event.getTrait(NCMaterialTraits.LIQUID);
			final boolean hasSolidForm = event.hasFlag(NCMaterialFlags.DUST);
			event.addTrait(NCMaterialTraits.LIQUID, new MaterialTraitFluid.Liquid(
				ConductanceMaterials.orElse(trait.getViscosity(), () -> MaterialTraitFluid.WATER_VISCOSITY),
				ConductanceMaterials.orElse(trait.getTemperature(), () -> hasSolidForm ? MaterialTraitFluid.MOLTEN_TEMP : MaterialTraitFluid.ROOM_TEMP),
				ConductanceMaterials.orElse(trait.getDensity(), () -> hasSolidForm ? MaterialTraitFluid.MOLTEN_DENSITY : MaterialTraitFluid.ROOM_DENSITY)
			));
		}
		if (event.hasTrait(NCMaterialTraits.GAS)) {
			final MaterialTraitFluid.Gas trait = event.getTrait(NCMaterialTraits.GAS);
			event.addTrait(NCMaterialTraits.GAS, new MaterialTraitFluid.Gas(
				ConductanceMaterials.orElse(trait.getViscosity(), () -> MaterialTraitFluid.WATER_VISCOSITY),
				ConductanceMaterials.orElse(trait.getTemperature(), () -> MaterialTraitFluid.ROOM_TEMP),
				ConductanceMaterials.orElse(trait.getDensity(), () -> MaterialTraitFluid.GAS_DENSITY)
			));
		}
		if (event.hasTrait(NCMaterialTraits.PLASMA)) {
			final MaterialTraitFluid.Plasma trait = event.getTrait(NCMaterialTraits.PLASMA);
			event.addTrait(NCMaterialTraits.PLASMA, new MaterialTraitFluid.Plasma(
				ConductanceMaterials.orElse(trait.getViscosity(), () -> MaterialTraitFluid.WATER_VISCOSITY),
				ConductanceMaterials.orElse(trait.getTemperature(), () -> MaterialTraitFluid.PLASMA_TEMP),
				ConductanceMaterials.orElse(trait.getDensity(), () -> MaterialTraitFluid.PLASMA_DENSITY)
			));
		}
	}

	private static int orElse(final int value, final IntSupplier fallback) {
		return value != -1 ? value : fallback.getAsInt();
	}

	private ConductanceMaterials() {
	}
}
