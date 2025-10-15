package conductance.init;

import conductance.api.NCMaterials;
import conductance.api.coil.event.RegisterCoilBlockTypeEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.Conductance;
import static conductance.api.NCCoilBlockTypes.CUPRONICKEL;
import static conductance.api.NCCoilBlockTypes.KANTHAL;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceCoilBlockTypes {

	@EventListener(priority = -100)
	private static void init(final RegisterCoilBlockTypeEvent event) {
		CUPRONICKEL = event.register("cupronickel", NCMaterials.CUPRONICKEL.getColor().getCurrentColor());
		KANTHAL = event.register("kanthal", NCMaterials.KANTHAL.getColor().getCurrentColor());
	}

	private ConductanceCoilBlockTypes() {
	}
}
