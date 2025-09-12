package conductance.core.block;

import java.util.Arrays;
import net.minecraft.resources.ResourceLocation;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.Conductance;
import conductance.lib.pipenet.WireRegistry;
import conductance.lib.pipenet.WireType;
import static conductance.api.resource.model.ModelUtils.MODEL_ROTATION;
import static conductance.init.block.PipeBlock.CONNECTION_PROPS;
import static net.minecraft.core.Direction.DOWN;
import static net.minecraft.core.Direction.EAST;
import static net.minecraft.core.Direction.NORTH;
import static net.minecraft.core.Direction.UP;
import static net.minecraft.core.Direction.WEST;

@ConductancePluginListener(modid = Conductance.MODID)
final class WireBlockModelHandler {

	@EventListener
	private static void onAddRuntimeModels(final AddRuntimeModelEvent event) {
		for (final WireType wireType : WireType.values()) {
			final int start = (16 - wireType.getVoxels()) / 2;
			final int end = start + wireType.getVoxels();
			event.addBlockModel(WireBlockModelHandler.getBaseLocation(wireType), builder -> builder
					.element(element -> element
							.from(start, start, start).to(end, end, end)
							.faces((side, face) -> face.particle().tintIndex(0), false)
					)
			);
			event.addBlockModel(WireBlockModelHandler.getExtensionLocation(wireType), builder ->
					builder.element(element -> element
							.from(start, start, 0).to(end, end, start)
							.faces((side, face) -> face.particle().tintIndex(0), true, UP, DOWN, NORTH, EAST, WEST)
					)
			);
		}

		Arrays.stream(WireRegistry.getAllBlocks()).forEach(blockEntry -> {
			final WireType wireType = blockEntry.get().getWireType();
			final ResourceLocation blockBaseId = blockEntry.getId().withSuffix("_base");
			final ResourceLocation blockExtensionId = blockEntry.getId().withSuffix("_extension");
			event.addBlockModel(blockBaseId, builder -> builder
					.parent(WireBlockModelHandler.getBaseLocation(wireType).withPrefix("block/"))
					.particle(wireType.getMaterialTaggedSet().getTextureType().getTexture(blockEntry.get().getMaterial().getTextureSet(), null, null).value())
			);
			event.addBlockModel(blockExtensionId, builder -> builder
					.parent(WireBlockModelHandler.getExtensionLocation(wireType).withPrefix("block/"))
					.particle(wireType.getMaterialTaggedSet().getTextureType().getTexture(blockEntry.get().getMaterial().getTextureSet(), null, null).value())
			);
			event.addBlockState(blockEntry.getId(), builder -> builder.multipart(state -> {
						state.part(null, model -> model.model(blockBaseId.withPrefix("block/")));
						CONNECTION_PROPS.forEach((direction, property) -> state.part(
								when -> when.when(property, true),
								model -> model.model(blockExtensionId.withPrefix("block/"))
										.x(MODEL_ROTATION.get(direction).getA())
										.y(MODEL_ROTATION.get(direction).getB())
						));
					}
			));
			event.addItemModel(blockEntry.getId(), model -> model.parent(blockBaseId.withPrefix("block/")));
		});
	}

	private static ResourceLocation getBaseLocation(final WireType wireType) {
		return Conductance.id(wireType.getMaterialTaggedSet().getRegistryKey() + "_base");
	}

	private static ResourceLocation getExtensionLocation(final WireType wireType) {
		return Conductance.id(wireType.getMaterialTaggedSet().getRegistryKey() + "_extension");
	}

	private WireBlockModelHandler() {
	}
}
