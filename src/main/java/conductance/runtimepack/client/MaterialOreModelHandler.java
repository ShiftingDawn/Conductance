package conductance.runtimepack.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import conductance.api.NCMaterialTraits;
import conductance.api.NCTextureTypes;
import conductance.api.material.Material;
import conductance.api.material.MaterialOreType;
import conductance.Conductance;

@RequiredArgsConstructor
public final class MaterialOreModelHandler {

	private static final Set<MaterialOreModelHandler> MODELS = new HashSet<>();

	private final Block block;
	private final Material material;
	private final MaterialOreType oreType;

	public static void add(final Block block, final Material material, final MaterialOreType oreType) {
		MaterialOreModelHandler.MODELS.add(new MaterialOreModelHandler(block, material, oreType));
	}

	static void reload() {
		MaterialOreModelHandler.MODELS.forEach(model -> {
			final ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(model.block);
			RuntimeResourcePack.addBlockModel(blockId, () -> Util.make(MaterialOreModelHandler.createOre(model.material.getTrait(NCMaterialTraits.ORE).isEmissive()), json -> {
				final String oreTexture = NCTextureTypes.ORE.getBlockTexture(model.material.getTextureSet(), null, null).getValue().toString();
				GsonHelper.getAsJsonObject(json, "textures").addProperty("particle", oreTexture);
				json.getAsJsonObject("children").getAsJsonObject("bearer").addProperty("parent", model.oreType.getBearingBlockModel().toString());
				json.getAsJsonObject("children").getAsJsonObject("ore_overlay").getAsJsonObject("textures").addProperty("particle", oreTexture);
			}));
			if (model.block.defaultBlockState().hasProperty(RotatedPillarBlock.AXIS)) {
				RuntimeResourcePack.addBlockState(blockId, BlockModelGenerators.createAxisAlignedPillarBlock(model.block, blockId.withPrefix("block/")));
			} else {
				RuntimeResourcePack.addBlockState(blockId, BlockModelGenerators.createSimpleBlock(model.block, blockId.withPrefix("block/")));
			}
			RuntimeResourcePack.addItemModel(BuiltInRegistries.ITEM.getKey(model.block.asItem()), new DelegatedModel(ModelLocationUtils.getModelLocation(model.block)));
		});
	}

	private static JsonObject createOre(final boolean emissive) {
		try (final BufferedReader reader = Minecraft.getInstance().getResourceManager().openAsReader(Conductance.id("models/block/ore%s.json".formatted(emissive ? "_emissive" : "")))) {
			return GsonHelper.parse(reader, true);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
}
