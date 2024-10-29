package conductance.client.resourcepack;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import conductance.api.material.Material;
import conductance.api.material.MaterialTextureSet;
import conductance.api.material.MaterialTextureType;

@RequiredArgsConstructor
public final class MaterialBlockModelHandler {

	private static final Set<MaterialBlockModelHandler> MODELS = new HashSet<>();

	private final Block block;
	private final Material material;
	private final MaterialTextureSet set;
	private final MaterialTextureType type;

	public static void add(final Block block, final Material material, final MaterialTextureSet set, final MaterialTextureType type) {
		MaterialBlockModelHandler.MODELS.add(new MaterialBlockModelHandler(block, material, set, type));
	}

	static void reload() {
		MaterialBlockModelHandler.MODELS.forEach(model -> {
			final ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(model.block);
			final ResourceLocation custom = ResourceHelper.getCustomBlockTexture(model.material, model.type);
			if (custom == null) {
				RuntimeResourcePack.addBlockModel(blockId, new DelegatedModel(model.type.getBlockModel(model.set, null, null).getValue()));
			} else {
				RuntimeResourcePack.addBlockModel(blockId, () -> Util.make(new JsonObject(), json -> {
					json.addProperty("parent", "block/cube_all");
					json.add("textures", Util.make(new JsonObject(), json2 -> json2.addProperty("all", custom.toString())));
				}));
			}
			RuntimeResourcePack.addBlockState(blockId, BlockModelGenerators.createSimpleBlock(model.block, blockId.withPrefix("block/")));
			RuntimeResourcePack.addItemModel(BuiltInRegistries.ITEM.getKey(model.block.asItem()), new DelegatedModel(ModelLocationUtils.getModelLocation(model.block)));
		});
	}
}
