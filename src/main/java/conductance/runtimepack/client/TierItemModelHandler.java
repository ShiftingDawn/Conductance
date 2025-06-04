package conductance.runtimepack.client;

import net.minecraft.resources.ResourceLocation;
import conductance.api.NCItems;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddItemModelEvent;
import conductance.Conductance;

@ConductancePluginListener(modid = Conductance.MODID)
final class TierItemModelHandler {

	@EventListener(priority = -100)
	private static void onAddItemModels(final AddItemModelEvent event) {
		NCItems.TIERED.rowMap().forEach((itemType, column) -> column.forEach((tier, itemEntry) -> {
			final ResourceLocation custom = ResourceHelper.getCustomItemTexture(itemEntry.getId());
			event.add(itemEntry.getId(), builder -> {
				if (custom == null) {
					builder.layer0(Conductance.id("item/tier/%s/base".formatted(itemType)));
					builder.layer1(Conductance.id("item/tier/%s/overlay".formatted(itemType)));
				} else {
					builder.layer0(custom);
				}
			});
		}));
	}

	private TierItemModelHandler() {
	}
}
