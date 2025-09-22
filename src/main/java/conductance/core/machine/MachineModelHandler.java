package conductance.core.machine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.MachineBlock;
import conductance.api.machine.MachineType;
import conductance.api.resource.ModelBuilder;
import conductance.api.resource.ModelElementBuilder;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.api.util.ModelUtils;
import conductance.Conductance;

final class MachineModelHandler {

	private static final ResourceLocation BASE_CASING_TEXTURE = Conductance.id("block/casing/machine_base");
	private static final List<MachineType<?>> DEFAULT_MODELS = Collections.synchronizedList(new ArrayList<>());
	private static final Map<MachineType<?>, ResourceLocation> SIMPLE_MODELS = new ConcurrentHashMap<>();

	static void addDefault(final MachineType<?> machineType) {
		MachineModelHandler.DEFAULT_MODELS.add(machineType);
	}

	static void addSimple(final MachineType<?> machineType, final ResourceLocation casingTexture) {
		MachineModelHandler.SIMPLE_MODELS.put(machineType, casingTexture);
	}

	static void generate(final AddRuntimeModelEvent event) {
		event.addBlockModel(Conductance.id("machine/base"), b -> b.parent(Conductance.id("block/cube_all")).particle(MachineModelHandler.BASE_CASING_TEXTURE));
		MachineModelHandler.DEFAULT_MODELS.forEach(machineType -> {
			final MachineBlock<?> block = machineType.getBlock().get();
			event.addBlockState(block, blockState -> blockState.simple(variant -> variant.model(block)));
			MachineModelHandler.createStandardModel(event, machineType, block, MachineModelHandler.BASE_CASING_TEXTURE, casing -> casing.parent(Conductance.id("block/machine/base")));
			event.addItemModelDelegate(block);
		});
		MachineModelHandler.SIMPLE_MODELS.forEach((machineType, casingTexture) -> {
			final MachineBlock<?> block = machineType.getBlock().get();
			event.addBlockState(block, blockState -> blockState.simple(variant -> variant.model(block)));
			MachineModelHandler.createStandardModel(event, machineType, block, casingTexture, casing -> casing.parent(Conductance.id("block/cube_all")).particle(casingTexture));
			event.addItemModelDelegate(block);
		});
	}

	private static void createStandardModel(
		final AddRuntimeModelEvent event, final MachineType<?> machineType, final MachineBlock<?> block, final ResourceLocation particleTexture, final Consumer<ModelBuilder> casingCallback
	) {
		event.addBlockModel(block, model -> model.particle(particleTexture).composite(composite -> composite
			.child("casing", casingCallback)
			.child("overlay", child -> child.renderType("cutout_mipped").element(element -> {
				element.from(0, 0, 0).to(16, 16, 16);
				MachineModelHandler.addSides(child, element, machineType.getId(), null, false);
			}, true), true)
			.child("overlay2", child -> child.renderType("cutout_mipped").element(element -> {
				element.from(0, 0, 0).to(16, 16, 16);
				MachineModelHandler.addSides(child, element, machineType.getId(), "_emissive", true);
			}, true), true)
			.itemRenderOrder("casing", "overlay", "overlay2")
		));
	}

	private static void addSides(final ModelBuilder builder, final ModelElementBuilder element, final ResourceLocation machineKey, @Nullable final String suffix, final boolean emissive) {
		ModelUtils.LOGICAL_SIDES.forEach((face, side) -> {
			final ResourceLocation texture = machineKey.withPath(current -> "block/machine/%s/%s%s".formatted(current, side, Objects.requireNonNullElse(suffix, "")));
			if (CAPI.resourceFinder().isTextureValid(texture)) {
				builder.texture(side, texture);
				element.face(face, f -> {
					f.texture(side).cullFace(face);
					if (emissive) {
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
