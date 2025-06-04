package conductance.core.material;

import java.io.BufferedReader;
import java.io.IOException;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.BlockModelGenerators;
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
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.api.util.JsonUtils;
import conductance.Conductance;

@ConductancePluginListener(modid = Conductance.MODID)
final class MaterialOreModelHandler {

	@EventListener(priority = -100)
	private static void onAddRuntimeModels(final AddRuntimeModelEvent event) {
		MaterialRegistryImpl.INSTANCE.getGeneratedBlockRegistry().rowMap().entrySet().stream().filter(entry -> entry.getKey().getOreType() != null)
				.forEach(entry -> entry.getValue().forEach((material, blockEntry) -> {
					final ResourceLocation blockId = blockEntry.getId();
					final MaterialOreType oreType = entry.getKey().getOreType();
					if (blockEntry.getDefaultState().hasProperty(RotatedPillarBlock.AXIS)) {
						event.insertBlockState(blockId, BlockModelGenerators.createAxisAlignedPillarBlock(blockEntry.get(), blockId.withPrefix("block/")));
					} else {
						event.insertBlockState(blockId, BlockModelGenerators.createSimpleBlock(blockEntry.get(), blockId.withPrefix("block/")));
					}
					event.insertBlockModel(blockId, () -> Util.make(MaterialOreModelHandler.createOre(material.get(NCMaterialTraits.ORE).isEmissive()), json -> {
						final String oreTexture = NCTextureTypes.ORE.getTexture(material.getTextureSet(), null, null).getValue().toString();
						JsonUtils.getOrOverrideObject("textures", json).addProperty("particle", oreTexture);
						final JsonObject children = JsonUtils.getOrOverrideObject("children", json);
						JsonUtils.getOrOverrideObject("textures", JsonUtils.getOrOverrideObject("ore_overlay", children)).addProperty("particle", oreTexture);
						JsonUtils.getOrOverrideObject("bearer", children).addProperty("parent", oreType.getBearingBlockModel().toString());
					}));
					event.addItemModel(BuiltInRegistries.ITEM.getKey(blockEntry.asItem()), builder -> builder.parent(ModelLocationUtils.getModelLocation(blockEntry.get())));
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
