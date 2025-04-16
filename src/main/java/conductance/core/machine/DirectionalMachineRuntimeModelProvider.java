package conductance.core.machine;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonObject;
import conductance.api.machine.MachineBlock;
import conductance.api.machine.MachineType;
import conductance.api.resource.BlockModelBuilder;
import conductance.api.resource.BlockStateBuilder;
import conductance.api.resource.RuntimeModelProvider;
import conductance.api.util.RotationState;
import conductance.api.util.SerializationHelper;
import conductance.Conductance;

public class DirectionalMachineRuntimeModelProvider implements RuntimeModelProvider {

	private final MachineType<?> machineType;

	public DirectionalMachineRuntimeModelProvider(final MachineType<?> machineType) {
		this.machineType = machineType;
	}

	@Override
	public void createBlockState(final ResourceLocation blockId, final Function<ResourceLocation, BlockStateBuilder> builderFactory, final ResourceLocation defaultModelLocation,
	                             final BiConsumer<ResourceLocation, JsonObject> prebuilt) {
		final BlockStateBuilder builder = builderFactory.apply(blockId);
		final RotationState rotationState = this.machineType.getBlock().get().getRotationState();
		if (rotationState == RotationState.NONE) {
			builder.simple(b -> b.model(defaultModelLocation));
		} else {
			builder.variants(b -> {
				if (rotationState == RotationState.ALL || rotationState == RotationState.HORIZONTAL) {
					b.variant(rotationState.property, Direction.NORTH).when(MachineBlock.LIT, false).model(defaultModelLocation);
					b.variant(rotationState.property, Direction.SOUTH).when(MachineBlock.LIT, false).model(defaultModelLocation).y(180);
					b.variant(rotationState.property, Direction.EAST).when(MachineBlock.LIT, false).model(defaultModelLocation).y(90);
					b.variant(rotationState.property, Direction.WEST).when(MachineBlock.LIT, false).model(defaultModelLocation).y(270);
					b.variant(rotationState.property, Direction.NORTH).when(MachineBlock.LIT, true).model(defaultModelLocation.withSuffix("_working"));
					b.variant(rotationState.property, Direction.SOUTH).when(MachineBlock.LIT, true).model(defaultModelLocation.withSuffix("_working")).y(180);
					b.variant(rotationState.property, Direction.EAST).when(MachineBlock.LIT, true).model(defaultModelLocation.withSuffix("_working")).y(90);
					b.variant(rotationState.property, Direction.WEST).when(MachineBlock.LIT, true).model(defaultModelLocation.withSuffix("_working")).y(270);
				}
				if (rotationState == RotationState.ALL || rotationState == RotationState.VERTICAL) {
					b.variant(rotationState.property, Direction.UP).when(MachineBlock.LIT, false).model(defaultModelLocation).x(90);
					b.variant(rotationState.property, Direction.DOWN).when(MachineBlock.LIT, false).model(defaultModelLocation).x(270);
					b.variant(rotationState.property, Direction.UP).when(MachineBlock.LIT, true).model(defaultModelLocation.withSuffix("_working")).x(90);
					b.variant(rotationState.property, Direction.DOWN).when(MachineBlock.LIT, true).model(defaultModelLocation.withSuffix("_working")).x(270);
				}
			});
		}
	}

	@Override
	public void createBlockModel(final ResourceLocation blockId, final Function<ResourceLocation, BlockModelBuilder<?>> builderFactory, final BiConsumer<ResourceLocation, JsonObject> prebuilt) {
		//Overlay model default
		final ResourceLocation locDefault = blockId.withSuffix("_overlay");
		builderFactory.apply(locDefault)
				.parent("block/block")
				.texture("overlay", Conductance.id("block/machine/%s/front".formatted(this.machineType.getRegistryKey())))
				.element().from(0, 0, 0).to(16, 16, 16)
				/*  */.face(Direction.NORTH).texture("overlay").cullFace(Direction.NORTH).build()
				.build();
		//Overlay model working
		final ResourceLocation locWorking = blockId.withSuffix("_overlay_working");
		builderFactory.apply(locWorking)
				.parent("block/block")
				.texture("overlay", Conductance.id("block/machine/%s/front_working".formatted(this.machineType.getRegistryKey())))
				.element().from(0, 0, 0).to(16, 16, 16)
				/*  */.face(Direction.NORTH).texture("overlay").cullFace(Direction.NORTH).build()
				.build();
		//Block base default
		final JsonObject jsonDefault = this.loadBaseModelJson();
		final JsonObject childrenDefault = SerializationHelper.getOrOverrideObject("children", jsonDefault);
		SerializationHelper.getOrOverrideObject("machine_overlay", childrenDefault).addProperty("parent", locDefault.withPrefix("block/").toString());
		prebuilt.accept(blockId, jsonDefault);
		//Block base working
		final JsonObject jsonWorking = this.loadBaseModelJson();
		final JsonObject childrenWorking = SerializationHelper.getOrOverrideObject("children", jsonWorking);
		SerializationHelper.getOrOverrideObject("machine_overlay", childrenWorking).addProperty("parent", locWorking.withPrefix("block/").toString());
		prebuilt.accept(blockId.withSuffix("_working"), jsonWorking);
	}

	protected String getModelPath() {
		return "models/block/machine_block_tiered.json";
	}

	private JsonObject loadBaseModelJson() {
		try (final BufferedReader reader = Minecraft.getInstance().getResourceManager().openAsReader(Conductance.id(this.getModelPath()))) {
			return GsonHelper.parse(reader, true);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
}
