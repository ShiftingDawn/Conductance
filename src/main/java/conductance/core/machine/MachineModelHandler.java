package conductance.core.machine;

import java.util.Objects;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.MachineBlock;
import conductance.api.machine.MachineModelType;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.ModelBuilder;
import conductance.api.resource.ModelElementBuilder;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.api.util.model.ModelUtils;
import conductance.api.util.world.RotationState;
import conductance.Conductance;

@ConductancePluginListener(modid = Conductance.MODID)
final class MachineModelHandler {

	@EventListener(priority = -100)
	private static void onAddRuntimeModels(final AddRuntimeModelEvent event) {
		CAPI.regs().machines().values().stream().filter(t -> t instanceof MachineTypeImpl).forEach(t -> {
			final MachineTypeImpl<?> type = (MachineTypeImpl<?>) t;
			final ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(type.getBlock().get());
			final RotationState rotationState = type.getBlock().get().getRotationState();
			if (type.getModelType() == MachineModelType.DEFAULT_WORKABLE) {
				MachineModelHandler.standard(blockId, rotationState, (ResourceLocation) type.getModelData(), type.getRegistryKey(), event, true);
			} else if (type.getModelType() == MachineModelType.TIERED || type.getModelType() == MachineModelType.TIERED_WORKABLE) {
				final MachineModelType.TypeAndTier data = (MachineModelType.TypeAndTier) type.getModelData();
				final ResourceLocation parentModel = Conductance.id("block/machine_casing_tiered_%s".formatted(data.tier().getRegistryKey()));
				MachineModelHandler.standard(blockId, rotationState, parentModel, data.type(), event, type.getModelType() == MachineModelType.TIERED_WORKABLE);
			}
			event.addItemModelDelegate(type.getBlock().get());
		});
	}

	private static void standard(final ResourceLocation blockId, final RotationState rotationState, final ResourceLocation hullModel, final String machineType, final AddRuntimeModelEvent event, final boolean workable) {
		event.addBlockModel(blockId, model -> model.composite(composite -> composite
				.child("hull", child -> child.parent(hullModel).renderType("solid"))
				.child("overlay", child -> child.renderType("cutout_mipped").element(element -> {
					element.from(0, 0, 0).to(16, 16, 16);
					MachineModelHandler.addSides(Conductance.id(machineType), child, element, null);
				}, true))
				.itemRenderOrder("hull", "overlay")
		));
		if (workable) {
			event.addBlockModel(blockId.withSuffix("_working"), model -> model.composite(composite -> composite
					.child("hull", child -> child.parent(hullModel).renderType("solid"))
					.child("overlay", child -> child.renderType("cutout_mipped").element(element -> {
						element.from(0, 0, 0).to(16, 16, 16);
						MachineModelHandler.addSides(Conductance.id(machineType), child, element, "_working");
					}, true))
					.itemRenderOrder("hull", "overlay")
			));
		}
		event.addBlockState(blockId, b -> b.variants(builder -> {
			final ResourceLocation defaultModel = blockId.withPrefix("block/");
			final ResourceLocation litModel = defaultModel.withSuffix("_working");
			for (final Direction direction : Direction.values()) {
				if (rotationState.test(direction)) {
					final Tuple<Integer, Integer> rotation = ModelUtils.MODEL_ROTATION.get(direction);
					if (workable) {
						builder.variant(rotationState.property, direction).when(MachineBlock.LIT, false)
								.model(defaultModel).x(rotation.getA()).y(rotation.getB());
						builder.variant(rotationState.property, direction).when(MachineBlock.LIT, true)
								.model(litModel).x(rotation.getA()).y(rotation.getB());
					} else {
						builder.variant(rotationState.property, direction)
								.model(defaultModel).x(rotation.getA()).y(rotation.getB());
					}
				}
			}
		}));
	}

	private static void addSides(final ResourceLocation machineKey, final ModelBuilder builder, final ModelElementBuilder element, @Nullable final String suffix) {
		ModelUtils.LOGICAL_SIDES.forEach((face, side) -> {
			ResourceLocation texLoc = machineKey.withPath(current -> "block/machine/%s/%s%s_emissive".formatted(current, side, Objects.requireNonNullElse(suffix, "")));
			boolean emissive = true;
			if (!CAPI.resourceFinder().isTextureValid(texLoc)) {
				texLoc = machineKey.withPath(current -> "block/machine/%s/%s%s".formatted(current, side, Objects.requireNonNullElse(suffix, "")));
				emissive = false;
			}
			if (CAPI.resourceFinder().isTextureValid(texLoc)) {
				builder.texture(side, texLoc);
				final boolean finalEmissive = emissive;
				element.face(face, f -> {
					f.texture(side).cullFace(face);
					if (finalEmissive) {
						f.tintIndex(-100);
					}
				});
				if (emissive) {
					element.shade(false);
				}
			}
		});
	}

	private MachineModelHandler() {
	}
}
