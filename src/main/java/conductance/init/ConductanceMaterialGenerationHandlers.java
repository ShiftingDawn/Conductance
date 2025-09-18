package conductance.init;

import net.neoforged.neoforge.fluids.FluidType;
import conductance.api.CAPI;
import conductance.api.NCMaterialFlags;
import conductance.api.NCMaterialProps;
import conductance.api.NCMaterialTraits;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.material.MaterialTraitFluid;
import conductance.api.material.MaterialTraitKey;
import conductance.api.material.event.MaterialGenerationHandlerBuilder;
import conductance.api.material.event.RegisterMaterialGenerationHandlerEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddTranslationEvent;
import conductance.api.util.TextHelper;
import conductance.Conductance;
import static conductance.api.NCMaterialGenerationHandlers.BOLT;
import static conductance.api.NCMaterialGenerationHandlers.DUST;
import static conductance.api.NCMaterialGenerationHandlers.FINE_WIRE;
import static conductance.api.NCMaterialGenerationHandlers.FOIL;
import static conductance.api.NCMaterialGenerationHandlers.FRAME_BOX;
import static conductance.api.NCMaterialGenerationHandlers.GAS;
import static conductance.api.NCMaterialGenerationHandlers.GEAR;
import static conductance.api.NCMaterialGenerationHandlers.GEAR_SMALL;
import static conductance.api.NCMaterialGenerationHandlers.GEM;
import static conductance.api.NCMaterialGenerationHandlers.GEM_EXQUISITE;
import static conductance.api.NCMaterialGenerationHandlers.GEM_FLAWED;
import static conductance.api.NCMaterialGenerationHandlers.GEM_FLAWLESS;
import static conductance.api.NCMaterialGenerationHandlers.INGOT;
import static conductance.api.NCMaterialGenerationHandlers.LIQUID;
import static conductance.api.NCMaterialGenerationHandlers.NUGGET;
import static conductance.api.NCMaterialGenerationHandlers.PLASMA;
import static conductance.api.NCMaterialGenerationHandlers.PLATE;
import static conductance.api.NCMaterialGenerationHandlers.PLATE_DENSE;
import static conductance.api.NCMaterialGenerationHandlers.PLATE_DOUBLE;
import static conductance.api.NCMaterialGenerationHandlers.RING;
import static conductance.api.NCMaterialGenerationHandlers.ROD;
import static conductance.api.NCMaterialGenerationHandlers.ROTOR;
import static conductance.api.NCMaterialGenerationHandlers.SCREW;
import static conductance.api.NCMaterialGenerationHandlers.STORAGE_BLOCK;

@ConductancePluginListener(modid = Conductance.MODID)
public final class ConductanceMaterialGenerationHandlers {

	@EventListener(priority = -100)
	private static void init(final RegisterMaterialGenerationHandlerEvent event) {
		DUST = event.register("dust", b -> b
				.groupTag("c:dusts", (String) null) //translation handled by NeoForge
				.entryTag("c:dusts/%s", "%s Dusts")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.DUST)
		);
		STORAGE_BLOCK = event.register("storage_block", b -> b
				.groupTag("c:storage_blocks", (String) null) //translation handled by NeoForge
				.entryTag("c:storage_blocks/%s", "%s Storage Blocks")
				.setHasBlock(true, true, true)
				.predicate(material -> material.hasFlag(NCMaterialFlags.INGOT) || material.hasFlag(NCMaterialFlags.GEM))
		);

		INGOT = event.register("ingot", b -> b
				.groupTag("c:ingots", (String) null) //translation handled by NeoForge
				.entryTag("c:ingots/%s", "%s Ingots")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.INGOT)
		);
		NUGGET = event.register("nugget", b -> b
				.groupTag("c:nuggets", (String) null) //translation handled by NeoForge
				.entryTag("c:nuggets/%s", "%s Nuggets")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.INGOT)
		);

		GEM = event.register("gem", "%s", b -> b
				.groupTag("c:gems", (String) null) //translation handled by NeoForge
				.entryTag("c:gems/%s", "%s Gems")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.GEM)
		);
		GEM_FLAWED = event.register("flawed_gem", "flawed_%s", b -> b
				.groupTag("c:flawed_gems", "Flawed Gems")
				.entryTag("c:flawed_gems/%s", "Flawed %s Gems")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.GEM)
		);
		GEM_FLAWLESS = event.register("flawless_gem", "flawless_%s", b -> b
				.groupTag("c:flawless_gems", "Flawless Gems")
				.entryTag("c:flawless_gems/%s", "Flawless %s Gems")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.GEM)
		);
		GEM_EXQUISITE = event.register("exquisite_gem", "exquisite_%s", b -> b
				.groupTag("c:exquisite_gems", "Exquisite Gems")
				.entryTag("c:exquisite_gems/%s", "Exquisite %s Gems")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.GEM)
		);

		PLATE = event.register("plate", b -> b
				.groupTag("c:plates", "Plates")
				.entryTag("c:plates/%s", "%s Plates")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.PLATE)
		);
		PLATE_DOUBLE = event.register("double_plate", "double_%s_plate", b -> b
				.groupTag("c:double_plates", "Double Plates")
				.entryTag("c:double_plates/%s", "Double %s Plates")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.PLATE)
		);
		PLATE_DENSE = event.register("dense_plate", "dense_%s_plate", b -> b
				.groupTag("c:dense_plates", "Dense Plates")
				.entryTag("c:dense_plates/%s", "Dense %s Plates")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.PLATE)
		);
		ROD = event.register("rod", b -> b
				.groupTag("c:rods", (String) null) //translation handled by NeoForge
				.entryTag("c:rods/%s", "%s Rods")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.ROD)
		);
		GEAR = event.register("gear", b -> b
				.groupTag("c:gears", "Gears")
				.entryTag("c:gears/%s", "%s Gears")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.GEAR)
		);
		GEAR_SMALL = event.register("small_gear", b -> b
				.groupTag("c:small_gears", "Small Gears")
				.entryTag("c:small_gears/%s", "Small %s Gears")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.GEAR_SMALL)
		);
		FOIL = event.register("foil", b -> b
				.groupTag("c:foils", "Foils")
				.entryTag("c:foils/%s", "%s Foil")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.FOIL)
		);
		BOLT = event.register("bolt", b -> b
				.groupTag("c:bolts", "Bolts")
				.entryTag("c:bolts/%s", "%s Bolts")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.BOLT_AND_SCREW)
		);
		SCREW = event.register("screw", b -> b
				.groupTag("c:screws", "Screws")
				.entryTag("c:screws/%s", "%s Screws")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.BOLT_AND_SCREW)
		);
		RING = event.register("ring", b -> b
				.groupTag("c:rings", "Rings")
				.entryTag("c:rings/%s", "%s Rings")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.RING)
		);
		ROTOR = event.register("rotor", b -> b
				.groupTag("c:rotors", "Rotors")
				.entryTag("c:rotors/%s", "%s Rotors")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.ROD)
		);
		FINE_WIRE = event.register("fine_wire", b -> b
				.groupTag("c:fine_wires", "Fine Wires")
				.entryTag("c:fine_wires/%s", "Fine %s Wires")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.FINE_WIRE)
		);
		FRAME_BOX = event.register("frame_box", b -> b
				.groupTag("c:frame_boxes", "Frame Boxes")
				.entryTag("c:frame_boxes/%s", "%s Frame Boxes")
				.setHasBlock(true, true, false)
				.requiredFlag(NCMaterialFlags.FRAME_BOX)
		);

		LIQUID = event.register("liquid", ConductanceMaterialGenerationHandlers::liquidUnlocalizedNameFactory, b -> b
				.entryTag("c:%s", "%s")
				.setHasFluid(true, true, ConductanceMaterialGenerationHandlers.createFluidBuilderCallback(NCMaterialTraits.LIQUID))
				.requiredTrait(NCMaterialTraits.LIQUID)
				.setDescriptionIdSuffixFactory(ConductanceMaterialGenerationHandlers::liquidDescriptionIdSuffixFactory)
		);
		GAS = event.register("gas", ConductanceMaterialGenerationHandlers::gasUnlocalizedNameFactory, b -> b
				.entryTag("c:gases/%s", "%s Gases")
				.setHasFluid(true, true, ConductanceMaterialGenerationHandlers.createFluidBuilderCallback(NCMaterialTraits.GAS))
				.requiredTrait(NCMaterialTraits.GAS)
				.setDescriptionIdSuffixFactory(ConductanceMaterialGenerationHandlers::gasDescriptionIdSuffixFactory)
		);
		PLASMA = event.register("plasma", b -> b
				.entryTag("c:plasmas/%s", "%s Plasmas")
				.setHasFluid(true, true, ConductanceMaterialGenerationHandlers.createFluidBuilderCallback(NCMaterialTraits.PLASMA))
				.requiredTrait(NCMaterialTraits.PLASMA)
		);
	}

	@EventListener(priority = -100)
	private static void addMaterialGenerationHandlerTranslations(final AddTranslationEvent event) {
		for (final MaterialGenerationHandler handler : CAPI.regs().materialGenerationHandlers()) {
			event.add(handler.getDescriptionId(), TextHelper.lowerUnderscoreToEnglish(handler.getId().getPath()));
			if (handler.getDescriptionIdSuffixFactory() == null) {
				event.add(handler.getDescriptionId() + ".factory", "%s " + TextHelper.lowerUnderscoreToEnglish(handler.getId().getPath()));
			}
		}
		event.add(STORAGE_BLOCK.getDescriptionId() + ".factory", "Block of %s");
		event.add(PLATE_DOUBLE.getDescriptionId() + ".factory", "Double %s Plate");
		event.add(PLATE_DENSE.getDescriptionId() + ".factory", "Dense %s Plate");
		event.add(GEAR_SMALL.getDescriptionId() + ".factory", "Small %s Gear");
		event.add(FINE_WIRE.getDescriptionId() + ".factory", "Fine %s Wire");
		event.add(LIQUID.getDescriptionId() + ".factory", "%s");
		event.add(LIQUID.getDescriptionId() + ".molten", "Molten %s");
		event.add(LIQUID.getDescriptionId() + ".liquid", "Liquid %s");
		event.add(LIQUID.getDescriptionId() + ".bucket", "%s Bucket");
		event.add(GAS.getDescriptionId() + ".gas", "%s Gas");
		event.add(GAS.getDescriptionId() + ".factory", "%s");
		event.add(GAS.getDescriptionId() + ".bucket", "%s Bucket");
		event.add(PLASMA.getDescriptionId() + ".bucket", "%s Bucket");
	}

	private static <T extends MaterialTraitFluid<T>> MaterialGenerationHandlerBuilder.BuilderCallback<FluidType.Properties> createFluidBuilderCallback(final MaterialTraitKey<T> traitType) {
		return (material, props) -> {
			final MaterialTraitFluid<T> trait = material.getTrait(traitType);
			assert trait != null;
			return props.temperature(trait.getTemperature()).viscosity(trait.getViscosity()).density(trait.getDensity());
		};
	}

	private static String liquidUnlocalizedNameFactory(final Material material) {
		if (material.hasFlag(NCMaterialFlags.DUST) || material.hasFlag(NCMaterialFlags.INGOT) || material.hasFlag(NCMaterialFlags.GEM)) {
			return "molten_%s";
		}
		if (material.getProp(NCMaterialProps.DEFAULT_FLUID) == NCMaterialTraits.GAS) {
			return "liquid_%s";
		}
		return "%s";
	}

	private static String liquidDescriptionIdSuffixFactory(final Material material) {
		if (material.hasFlag(NCMaterialFlags.DUST) || material.hasFlag(NCMaterialFlags.INGOT) || material.hasFlag(NCMaterialFlags.GEM)) {
			return "molten";
		}
		if (material.getProp(NCMaterialProps.DEFAULT_FLUID) == NCMaterialTraits.GAS) {
			return "liquid";
		}
		return "factory";
	}

	private static String gasUnlocalizedNameFactory(final Material material) {
		if (material.getProp(NCMaterialProps.DEFAULT_FLUID) == NCMaterialTraits.GAS) {
			return "%s";
		}
		return "%s_gas";
	}

	private static String gasDescriptionIdSuffixFactory(final Material material) {
		if (material.getProp(NCMaterialProps.DEFAULT_FLUID) == NCMaterialTraits.GAS) {
			return "factory";
		}
		return "gas";
	}

	private ConductanceMaterialGenerationHandlers() {
	}
}
