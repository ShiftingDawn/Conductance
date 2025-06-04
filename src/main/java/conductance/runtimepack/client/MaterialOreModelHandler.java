package conductance.runtimepack.client;

import java.io.BufferedReader;
import java.io.IOException;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.RotatedPillarBlock;
import com.google.gson.JsonObject;
import conductance.api.NCMaterialTraits;
import conductance.api.NCTextureTypes;
import conductance.api.material.MaterialOreType;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.ReloadingRuntimeResourcePackEvent;
import conductance.api.util.SerializationHelper;
import conductance.Conductance;
import conductance.core.material.MaterialRegistryImpl;

@ConductancePluginListener(modid = Conductance.MODID)
final class MaterialOreModelHandler {

	@EventListener(priority = -100)
	private static void onReloadingRuntimeResourcePack(final ReloadingRuntimeResourcePackEvent event) {
		MaterialRegistryImpl.INSTANCE.getGeneratedBlockRegistry().rowMap().entrySet().stream().filter(entry -> entry.getKey().getOreType() != null)
				.forEach(entry -> entry.getValue().forEach((material, blockEntry) -> {
					final ResourceLocation blockId = blockEntry.getId();
					final MaterialOreType oreType = entry.getKey().getOreType();
					event.addBlockModel(blockId, () -> Util.make(MaterialOreModelHandler.createOre(material.get(NCMaterialTraits.ORE).isEmissive()), json -> {
						final String oreTexture = NCTextureTypes.ORE.getTexture(material.getTextureSet(), null, null).getValue().toString();
						SerializationHelper.getOrOverrideObject("textures", json).addProperty("particle", oreTexture);
						final JsonObject children = SerializationHelper.getOrOverrideObject("children", json);
						SerializationHelper.getOrOverrideObject("textures", SerializationHelper.getOrOverrideObject("ore_overlay", children)).addProperty("particle", oreTexture);
						SerializationHelper.getOrOverrideObject("bearer", children).addProperty("parent", oreType.getBearingBlockModel().toString());
					}));
					if (blockEntry.getDefaultState().hasProperty(RotatedPillarBlock.AXIS)) {
						event.addBlockState(blockId, BlockModelGenerators.createAxisAlignedPillarBlock(blockEntry.get(), blockId.withPrefix("block/")));
					} else {
						event.addBlockState(blockId, BlockModelGenerators.createSimpleBlock(blockEntry.get(), blockId.withPrefix("block/")));
					}
					event.addItemModel(BuiltInRegistries.ITEM.getKey(blockEntry.asItem()), new DelegatedModel(ModelLocationUtils.getModelLocation(blockEntry.get())));
				}));
	}

	private static JsonObject createOre(final boolean emissive) {
		try (final BufferedReader reader = Minecraft.getInstance().getResourceManager().openAsReader(Conductance.id("models/block/ore%s.json".formatted(emissive ? "_emissive" : "")))) {
			return GsonHelper.parse(reader, true);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
}
