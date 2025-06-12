package conductance.init;

import conductance.api.CAPI;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.plugin.RegisterTagEvent;
import conductance.Conductance;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceTags {

	@EventListener(priority = -100)
	private static void onRegisterTags(final RegisterTagEvent event) {
		event.item(CAPI.TAG_WRENCHES, ConductanceItems.CRAFTING_TOOL_WRENCH);
		event.item(CAPI.TAG_HAMMERS, ConductanceItems.CRAFTING_TOOL_HAMMER);
		event.item(CAPI.TAG_WIRE_CUTTERS, ConductanceItems.CRAFTING_TOOL_WIRE_CUTTERS);
	}

	private ConductanceTags() {
	}
}
