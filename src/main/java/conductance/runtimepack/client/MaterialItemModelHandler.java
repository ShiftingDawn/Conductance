package conductance.runtimepack.client;

import net.minecraft.resources.ResourceLocation;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddItemModelEvent;
import conductance.Conductance;
import conductance.core.material.MaterialRegistryImpl;

@ConductancePluginListener(modid = Conductance.MODID)
final class MaterialItemModelHandler {

	@EventListener(priority = -100)
	private static void onAddItemModels(final AddItemModelEvent event) {
		MaterialRegistryImpl.INSTANCE.getGeneratedItemRegistry().rowMap().forEach((taggedSet, column) -> column.forEach((material, itemEntry) -> {
			final ResourceLocation custom = ResourceHelper.getCustomMaterialTexture(material, taggedSet.getTextureType());
			event.add(itemEntry.getId(), builder -> {
				if (custom == null) {
					builder.parent(taggedSet.getTextureType().getItemModel(material.getTextureSet(), null, null).getValue());
				} else {
					builder.layer0(custom);
				}
			});
		}));
	}

	private MaterialItemModelHandler() {
	}
}
