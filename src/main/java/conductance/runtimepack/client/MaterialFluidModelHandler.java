package conductance.runtimepack.client;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddItemModelEvent;
import conductance.Conductance;
import conductance.core.material.MaterialRegistryImpl;

@ConductancePluginListener(modid = Conductance.MODID)
final class MaterialFluidModelHandler {

	@EventListener(priority = -100)
	private static void onAddItemModels(final AddItemModelEvent event) {
		MaterialRegistryImpl.INSTANCE.getGeneratedFluidRegistry().rowMap().forEach((taggedSet, column) -> column.forEach((material, fluid) -> {
			fluid.getBucket().ifPresent(bucket -> {
				final ResourceLocation bucketId = BuiltInRegistries.ITEM.getKey(bucket);
				event.add(bucketId, builder -> builder.parent(ResourceLocation.fromNamespaceAndPath(NeoForgeVersion.MOD_ID, "item/bucket_drip"))
						.loader(ResourceLocation.fromNamespaceAndPath(NeoForgeVersion.MOD_ID, "fluid_container"))
						.addProperty("flip_gas", fluid.getType().isLighterThanAir())
						.addProperty("fluid", fluid.getId()));
			});
		}));
	}

	private MaterialFluidModelHandler() {
	}
}
