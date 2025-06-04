package conductance.runtimepack.client;

import conductance.api.CAPI;
import conductance.api.NCItems;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.Conductance;

@ConductancePluginListener(modid = Conductance.MODID)
final class TierItemModelHandler {

	@EventListener(priority = -100)
	private static void onAddRuntimeModels(final AddRuntimeModelEvent event) {
		NCItems.TIERED.rowMap().forEach((itemType, column) -> column.forEach((tier, itemEntry) -> {
			event.addItemModel(itemEntry.getId(), builder -> {
				if (CAPI.resourceFinder().isItemTextureValid(itemEntry.getId())) {
					builder.layer0(CAPI.resourceFinder().getItemTexture(itemEntry.getId()));
				} else {
					builder.layer0(Conductance.id("item/tier/%s/base".formatted(itemType)));
					builder.layer1(Conductance.id("item/tier/%s/overlay".formatted(itemType)));
				}
			});
		}));
	}

	private TierItemModelHandler() {
	}
}
