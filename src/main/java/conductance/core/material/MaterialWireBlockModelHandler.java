package conductance.core.material;

import java.util.Arrays;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import conductance.api.CAPI;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.api.util.ModelUtils;
import conductance.Conductance;
import conductance.init.block.WireBlock;
import conductance.lib.pipenet.WireRegistry;
import conductance.lib.pipenet.WireType;
import static net.minecraft.core.Direction.DOWN;
import static net.minecraft.core.Direction.EAST;
import static net.minecraft.core.Direction.NORTH;
import static net.minecraft.core.Direction.UP;
import static net.minecraft.core.Direction.WEST;

@ConductancePluginListener(modid = Conductance.MODID)
final class MaterialWireBlockModelHandler {

	@EventListener(priority = -99)
	private static void onAddRuntimeModels(final AddRuntimeModelEvent event) {
		for (final WireType wireType : WireType.values()) {
			final int start = (16 - wireType.getVoxels()) / 2;
			final int end = start + wireType.getVoxels();
			event.addBlockModel(MaterialWireBlockModelHandler.getBaseLocation(wireType), builder -> builder
				.element(element -> element
					.from(start, start, start).to(end, end, end)
					.faces((side, face) -> face.particle().tintIndex(0), false)
				)
			);
			event.addBlockModel(MaterialWireBlockModelHandler.getExtensionLocation(wireType), builder ->
				builder.element(element -> element
					.from(start, start, 0).to(end, end, start)
					.faces((side, face) -> face.particle().tintIndex(0), false, UP, DOWN, NORTH, EAST, WEST)
				)
			);
		}

		Arrays.stream(WireRegistry.getAllBlocks()).forEach(block -> {
			final WireType wireType = block.getWireType();
			final ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
			final ResourceLocation blockBaseId = blockId.withSuffix("_base");
			final ResourceLocation blockExtensionId = blockId.withSuffix("_extension");
			event.addBlockState(blockId, builder -> builder.multipart(state -> {
					state.part(null, model -> model.model(blockBaseId.withPrefix("block/")));
					WireBlock.CONNECTION_PROPS.forEach((direction, property) -> state.part(
						when -> when.when(property, true),
						model -> model.model(blockExtensionId.withPrefix("block/"))
							.x(ModelUtils.MODEL_ROTATION.get(direction).x())
							.y(ModelUtils.MODEL_ROTATION.get(direction).y())
							.uvLock(true)
					));
				}
			));
			event.addBlockModel(blockBaseId, builder -> builder
				.parent(MaterialWireBlockModelHandler.getBaseLocation(wireType).withPrefix("block/"))
				.particle(CAPI.resourceFinder().getMaterialTexture(block.getMaterial().getTextureSet(), wireType.getHandler().getTextureType(), null, null).value())
			);
			event.addBlockModel(blockExtensionId, builder -> builder
				.parent(MaterialWireBlockModelHandler.getExtensionLocation(wireType).withPrefix("block/"))
				.particle(CAPI.resourceFinder().getMaterialTexture(block.getMaterial().getTextureSet(), wireType.getHandler().getTextureType(), null, null).value())
			);
			event.addItemsModel(block.asItem(), builder -> builder.simple(blockBaseId.withPrefix("block/")));
		});
	}

	private static ResourceLocation getBaseLocation(final WireType wireType) {
		return Conductance.id(wireType.getHandler().getId().getPath() + "_base");
	}

	private static ResourceLocation getExtensionLocation(final WireType wireType) {
		return Conductance.id(wireType.getHandler().getId().getPath() + "_extension");
	}

	private MaterialWireBlockModelHandler() {
	}
}
