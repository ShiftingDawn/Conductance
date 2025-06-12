package conductance.init;

import conductance.api.NCMaterialTaggedSets;
import conductance.api.material.event.RegisterMaterialUnitOverrideEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.Conductance;
import static conductance.api.CAPI.UNIT;
import static conductance.api.NCMaterials.AMETHYST;
import static conductance.api.NCMaterials.BLAZE;
import static conductance.api.NCMaterials.BONE;
import static conductance.api.NCMaterials.BRICK;
import static conductance.api.NCMaterials.CALCITE;
import static conductance.api.NCMaterials.CERTUS_QUARTZ;
import static conductance.api.NCMaterials.CLAY;
import static conductance.api.NCMaterials.GLASS;
import static conductance.api.NCMaterials.ICE;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceMaterialUnitOverrides {

	@EventListener(priority = -100)
	private static void onRegisterMaterialUnitOverrides(final RegisterMaterialUnitOverrideEvent event) {
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, CLAY, UNIT * 4);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, BRICK, UNIT * 4);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, ICE, UNIT);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, CALCITE, UNIT * 4);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, GLASS, UNIT);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, CERTUS_QUARTZ, UNIT * 4);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, AMETHYST, UNIT * 4);
		event.add(NCMaterialTaggedSets.ROD, BLAZE, UNIT * 4);
		event.add(NCMaterialTaggedSets.ROD, BONE, UNIT * 5);
	}

	private ConductanceMaterialUnitOverrides() {
	}
}
