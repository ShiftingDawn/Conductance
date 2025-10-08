package conductance.core.machine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.block.BlockRotationHelper;
import conductance.api.machine.MachineBlock;
import conductance.api.machine.MachineType;
import conductance.api.resource.ModelBuilder;
import conductance.api.resource.ModelElementBuilder;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.api.tier.Tier;
import conductance.api.util.ModelUtils;
import conductance.Conductance;
import static conductance.api.machine.MachineBlock.WORKING;

final class MachineModelHandler {

	private static final ResourceLocation BASE_CASING_TEXTURE = Conductance.id("block/casing/machine_base");
	private static final List<MachineType<?>> DEFAULT_MODELS = Collections.synchronizedList(new ArrayList<>());
	private static final Map<MachineType<?>, ResourceLocation> SIMPLE_MODELS = new ConcurrentHashMap<>();
	private static final Map<MachineType<?>, Tuple<String, Tier>> TIERED_MODELS = new ConcurrentHashMap<>();

	static void addDefault(final MachineType<?> machineType) {
		MachineModelHandler.DEFAULT_MODELS.add(machineType);
	}

	static void addSimple(final MachineType<?> machineType, final ResourceLocation casingTexture) {
		MachineModelHandler.SIMPLE_MODELS.put(machineType, casingTexture);
	}

	static void addTiered(final MachineType<?> machineType, final String baseName, final Tier tier) {
		MachineModelHandler.TIERED_MODELS.put(machineType, new Tuple<>(baseName, tier));
	}

	static void generate(final AddRuntimeModelEvent event) {
		event.addBlockModel(Conductance.id("machine/base"), b -> b.parent(Conductance.id("block/cube_all")).particle(MachineModelHandler.BASE_CASING_TEXTURE));
		MachineModelHandler.DEFAULT_MODELS.forEach(machineType -> {
			final MachineBlock<?> block = machineType.getBlock().get();
			final boolean canBeLit = block.defaultBlockState().hasProperty(WORKING);
			MachineModelHandler.createBlockState(event, block, machineType, canBeLit);
			for (int i = 0; i < 2; ++i) {
				MachineModelHandler.createStandardModel(event, machineType, block, i == 1, null, MachineModelHandler.BASE_CASING_TEXTURE, casing -> casing.parent(Conductance.id("block/machine/base")));
				if (!canBeLit) {
					break;
				}
			}
			event.addItemModelDelegate(block);
		});
		MachineModelHandler.SIMPLE_MODELS.forEach((machineType, casingTexture) -> {
			final MachineBlock<?> block = machineType.getBlock().get();
			final boolean canBeLit = block.defaultBlockState().hasProperty(WORKING);
			MachineModelHandler.createBlockState(event, block, machineType, canBeLit);
			for (int i = 0; i < 2; ++i) {
				MachineModelHandler.createStandardModel(event, machineType, block, i == 1, null, casingTexture, casing -> casing.parent(Conductance.id("block/cube_all")).particle(casingTexture));
				if (!canBeLit) {
					break;
				}
			}
			event.addItemModelDelegate(block);
		});
		MachineModelHandler.TIERED_MODELS.forEach((machineType, modelData) -> {
			final MachineBlock<?> block = machineType.getBlock().get();
			final boolean canBeLit = block.defaultBlockState().hasProperty(WORKING);
			final ResourceLocation casingTexture = modelData.getB().getId().withPrefix("block/casing/machine_");
			MachineModelHandler.createBlockState(event, block, machineType, canBeLit);
			for (int i = 0; i < 2; ++i) {
				MachineModelHandler.createStandardModel(event, machineType, block, i == 1, machineType.getId().withPath(modelData.getA()), casingTexture,
					casing -> casing.parent(Conductance.id("block/cube_all")).particle(casingTexture));
				if (!canBeLit) {
					break;
				}
			}
			event.addItemModelDelegate(block);
		});
	}

	private static void createBlockState(final AddRuntimeModelEvent event, final MachineBlock<?> block, final MachineType<?> machineType, final boolean canBeLit) {
		event.addBlockState(block, blockState -> {
			BlockRotationHelper.handleBlockStateGeneration(blockState, machineType.getRotationType(), variant -> {
				if (canBeLit) {
					variant.when(WORKING, false);
				}
				return variant.model(block);
			}, model -> model.addProperty("type", Conductance.id("machine")));
			if (canBeLit) {
				BlockRotationHelper.handleBlockStateGeneration(
					blockState, machineType.getRotationType(),
					variant -> variant.when(WORKING, true).model(BuiltInRegistries.BLOCK.getKey(block).withPath(current -> "block/" + current + "_working")),
					model -> model.addProperty("type", Conductance.id("machine"))
				);
			}
		});
	}

	private static void createStandardModel(
		final AddRuntimeModelEvent event, final MachineType<?> machineType, final MachineBlock<?> block, final boolean working, @Nullable final ResourceLocation machineTextureLocation,
		final ResourceLocation particleTexture,
		final Consumer<ModelBuilder> casingCallback
	) {
		ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
		if (working) {
			id = id.withSuffix("_working");
		}
		event.addBlockModel(id, model -> model.particle(particleTexture).renderType("cutout_mipped").composite(composite -> composite
			.child("casing", casingCallback)
			.child("overlay", child -> child.element(element -> {
				element.from(0, 0, 0).to(16, 16, 16);
				MachineModelHandler.addSides(child, element, Objects.requireNonNullElseGet(machineTextureLocation, machineType::getId), null, false);
				if (working) {
					MachineModelHandler.addSides(child, element, Objects.requireNonNullElseGet(machineTextureLocation, machineType::getId), "_working", false);
				}
			}, true), true)
			.child("overlay2", child -> child.element(element -> {
				element.from(0, 0, 0).to(16, 16, 16);
				MachineModelHandler.addSides(child, element, Objects.requireNonNullElseGet(machineTextureLocation, machineType::getId), "_emissive", true);
				if (working) {
					MachineModelHandler.addSides(child, element, Objects.requireNonNullElseGet(machineTextureLocation, machineType::getId), "_working_emissive", true);
				}
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
