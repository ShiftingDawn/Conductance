package conductance.core.tier;

import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.api.tier.TieredItemType;
import conductance.Conductance;

@ConductancePluginListener(modid = Conductance.MODID)
final class TierModelHandler {

	@EventListener(priority = -100)
	private static void onAddRuntimeModels(final AddRuntimeModelEvent event) {
		for (final TieredItemType itemType : TieredItemType.values()) {
			event.addItemModel(Conductance.id("tier/%s".formatted(itemType.toString())), b -> b
				.layer0(Conductance.id("item/tier/%s/base".formatted(itemType)))
				.layer1(Conductance.id("item/tier/%s/overlay".formatted(itemType)))
			);
		}
	}

	private TierModelHandler() {
	}
}
