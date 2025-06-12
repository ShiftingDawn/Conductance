package conductance.core.material;

import net.minecraft.resources.ResourceLocation;
import conductance.api.CAPI;
import conductance.api.material.MaterialTextureType;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.Conductance;
import conductance.init.block.MaterialBlock;

@ConductancePluginListener(modid = Conductance.MODID)
final class MaterialBlockModelHandler {

	@EventListener(priority = -100)
	private static void onAddRuntimeModels(final AddRuntimeModelEvent event) {
		MaterialRegistryImpl.INSTANCE.getGeneratedBlockRegistry().rowMap().forEach((taggedSet, column) -> column.forEach((material, blockEntry) -> {
			if (!(blockEntry.get() instanceof final MaterialBlock block)) {
				return;
			}
			event.addBlockState(blockEntry.getId(), blockStateBuilder -> blockStateBuilder.simple(variant ->
					variant.model(blockEntry.getId().withPrefix("block/")))
			);
			final MaterialTextureType textureType = block.getSet().getTextureType();
			final ResourceLocation custom = CAPI.resourceFinder().getCustomMaterialTexture(material, textureType);
			if (custom == null) {
				event.addBlockModel(blockEntry.getId(), builder -> builder.parent(textureType.getBlockModel(material.getTextureSet(), null, null).value()));
			} else {
				event.addBlockModel(blockEntry.getId(), builder -> builder.parent("block/cube_all").texture("all", custom));
			}
			event.addItemModelDelegate(block);
		}));
	}

	private MaterialBlockModelHandler() {
	}
}
