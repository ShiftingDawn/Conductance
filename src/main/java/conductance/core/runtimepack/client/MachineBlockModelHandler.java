package conductance.core.runtimepack.client;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import conductance.api.CAPI;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.Conductance;

@ConductancePluginListener(modid = Conductance.MODID)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class MachineBlockModelHandler {

	private static final EnumMap<Direction, String> SIDES = new EnumMap<>(Direction.class);
	private static final Map<String, MachineBlockModelHandler> MODELS = new HashMap<>();

	private final String machineKey;
	private final ResourceLocation modelLocation;

	public static void add(final String machineKey, final ResourceLocation modelLocation) {
		MachineBlockModelHandler.MODELS.put(machineKey, new MachineBlockModelHandler(machineKey, modelLocation));
	}

	public static void remove(final String machineKey) {
		MachineBlockModelHandler.MODELS.remove(machineKey);
	}

	@EventListener(priority = -100)
	private static void onAddRuntimeModels(final AddRuntimeModelEvent event) {
		MachineBlockModelHandler.MODELS.values().forEach(model -> {
			final String newPath = model.modelLocation.getPath().startsWith("block/") ? model.modelLocation.getPath().substring(6) : model.modelLocation.getPath();
			event.addBlockModel(model.modelLocation.withPath(newPath), builder -> builder
					.element(element -> {
						element.from(0, 0, 0).to(16, 16, 16);
						MachineBlockModelHandler.SIDES.forEach((dir, side) -> model.ifExists(side, tex -> {
							builder.texture(side, tex);
							element.face(dir, face -> face.texture(side).cullFace(dir));
						}));
					})
			);
		});
	}

	private void ifExists(final String side, final Consumer<ResourceLocation> consumer) {
		final ResourceLocation texLoc = this.modelLocation.withPath("block/machine/%s/%s".formatted(this.machineKey, side));
		if (CAPI.resourceFinder().isTextureValid(texLoc)) {
			consumer.accept(texLoc);
		}
	}

	static {
		MachineBlockModelHandler.SIDES.put(Direction.UP, "top");
		MachineBlockModelHandler.SIDES.put(Direction.DOWN, "bottom");
		MachineBlockModelHandler.SIDES.put(Direction.NORTH, "front");
		MachineBlockModelHandler.SIDES.put(Direction.SOUTH, "back");
		MachineBlockModelHandler.SIDES.put(Direction.EAST, "side");
		MachineBlockModelHandler.SIDES.put(Direction.WEST, "side");
	}
}
