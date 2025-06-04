package conductance.core.material;

import net.minecraft.resources.ResourceLocation;
import conductance.api.CAPI;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.Conductance;

@ConductancePluginListener(modid = Conductance.MODID)
final class MaterialItemModelHandler {

	@EventListener(priority = -100)
	private static void onAddRuntimeModels(final AddRuntimeModelEvent event) {
		MaterialRegistryImpl.INSTANCE.getGeneratedItemRegistry().rowMap().forEach((taggedSet, column) -> column.forEach((material, itemEntry) -> {
			final ResourceLocation custom = CAPI.resourceFinder().getCustomMaterialTexture(material, taggedSet.getTextureType());
			event.addItemModel(itemEntry.getId(), builder -> {
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
