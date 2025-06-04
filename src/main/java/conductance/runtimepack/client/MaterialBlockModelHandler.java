package conductance.runtimepack.client;

import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonObject;
import conductance.api.material.MaterialTextureType;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.ReloadingRuntimeResourcePackEvent;
import conductance.Conductance;
import conductance.core.material.MaterialRegistryImpl;
import conductance.init.block.MaterialBlock;

@ConductancePluginListener(modid = Conductance.MODID)
final class MaterialBlockModelHandler {

	@EventListener(priority = -100)
	private static void onReloadingRuntimeResourcePack(final ReloadingRuntimeResourcePackEvent event) {
		MaterialRegistryImpl.INSTANCE.getGeneratedBlockRegistry().rowMap().forEach((taggedSet, column) -> column.forEach((material, blockEntry) -> {
			if (!(blockEntry.get() instanceof final MaterialBlock block)) {
				return;
			}
			event.addBlockState(blockEntry.getId(), BlockModelGenerators.createSimpleBlock(block, blockEntry.getId().withPrefix("block/")));
			event.addItemModel(BuiltInRegistries.ITEM.getKey(block.asItem()), new DelegatedModel(ModelLocationUtils.getModelLocation(block)));
			final MaterialTextureType textureType = block.getSet().getTextureType();
			final ResourceLocation custom = ResourceHelper.getCustomMaterialTexture(material, textureType);
			if (custom == null) {
				event.addBlockModel(blockEntry.getId(), new DelegatedModel(textureType.getBlockModel(material.getTextureSet(), null, null).getValue()));
			} else {
				event.addBlockModel(blockEntry.getId(), () -> Util.make(new JsonObject(), json -> {
					json.addProperty("parent", "block/cube_all");
					json.add("textures", Util.make(new JsonObject(), json2 -> json2.addProperty("all", custom.toString())));
				}));
			}
		}));
	}

	private MaterialBlockModelHandler() {
	}
}
