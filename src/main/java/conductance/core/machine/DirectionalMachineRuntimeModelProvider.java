package conductance.core.machine;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import com.mojang.datafixers.util.Pair;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import conductance.api.machine.MachineType;
import conductance.api.resource.BlockModelBuilder;
import conductance.api.resource.BlockStateBuilder;
import conductance.api.resource.RuntimeModelProvider;
import conductance.api.util.SerializationHelper;
import conductance.Conductance;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class DirectionalMachineRuntimeModelProvider implements RuntimeModelProvider {

	private final MachineType<?> machineType;

	public DirectionalMachineRuntimeModelProvider(final MachineType<?> machineType) {
		this.machineType = machineType;
	}

	@Override
	public void createBlockState(final ResourceLocation blockId, final BlockStateBuilder builder, final ResourceLocation defaultModelLocation, final Consumer<JsonObject> prebuilt) {
		builder.variants(b -> {
			b.variant(HORIZONTAL_FACING, Direction.NORTH).model(defaultModelLocation);
			b.variant(HORIZONTAL_FACING, Direction.SOUTH).model(defaultModelLocation).y(180);
			b.variant(HORIZONTAL_FACING, Direction.EAST).model(defaultModelLocation).y(90);
			b.variant(HORIZONTAL_FACING, Direction.WEST).model(defaultModelLocation).y(270);
		});
	}

	@Override
	public void createBlockModel(final ResourceLocation blockId, final BlockModelBuilder<?> builder, final Consumer<JsonObject> prebuilt) {
		final JsonObject json = DirectionalMachineRuntimeModelProvider.loadJson();
		final Pair<JsonObject, String> frontOverlayHolder =
				SerializationHelper.findContainer(json, e -> e instanceof final JsonPrimitive prim && prim.isString() && prim.getAsString().equals("@@MACHINE_OVERLAY_FRONT@@"));
		if (frontOverlayHolder != null) {
			frontOverlayHolder.getFirst().addProperty(frontOverlayHolder.getSecond(), Conductance.id("block/machine/%s/front".formatted(this.machineType.getRegistryKey())).toString());
		}
		prebuilt.accept(json);
	}

	private static JsonObject loadJson() {
		try (final BufferedReader reader = Minecraft.getInstance().getResourceManager().openAsReader(Conductance.id("models/block/cube_machine_overlay.json"))) {
			return GsonHelper.parse(reader, true);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
}
