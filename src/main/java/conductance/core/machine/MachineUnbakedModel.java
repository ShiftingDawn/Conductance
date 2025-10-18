package conductance.core.machine;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.SingleVariant;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Tuple;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;
import net.neoforged.neoforge.model.data.ModelData;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.MachineModelProperties;
import conductance.api.util.ModelUtils;
import conductance.Conductance;
import conductance.client.model.ExtendedRotationVariant;

public record MachineUnbakedModel(ExtendedRotationVariant variant) implements CustomUnbakedBlockStateModel {

	public static final ResourceLocation TEXTURE_IO_PORT = Conductance.id("block/machine/io_port");
	public static final ResourceLocation TEXTURE_IO_PORT_ITEM_OFF = Conductance.id("block/machine/io_port_item_off");
	public static final ResourceLocation TEXTURE_IO_PORT_ITEM_ON = Conductance.id("block/machine/io_port_item_on");
	public static final ResourceLocation TEXTURE_IO_PORT_FLUID_OFF = Conductance.id("block/machine/io_port_fluid_off");
	public static final ResourceLocation TEXTURE_IO_PORT_FLUID_ON = Conductance.id("block/machine/io_port_fluid_on");
	public static final ResourceLocation TEXTURE_IO_PORT_BOTH_ITEM_OFF = Conductance.id("block/machine/io_port_both_item_off");
	public static final ResourceLocation TEXTURE_IO_PORT_BOTH_ITEM_ON = Conductance.id("block/machine/io_port_both_item_on");
	public static final ResourceLocation TEXTURE_IO_PORT_BOTH_FLUID_OFF = Conductance.id("block/machine/io_port_both_fluid_off");
	public static final ResourceLocation TEXTURE_IO_PORT_BOTH_FLUID_ON = Conductance.id("block/machine/io_port_both_fluid_on");

	public static final MapCodec<MachineUnbakedModel> MAP_CODEC = ExtendedRotationVariant.MAP_CODEC.xmap(MachineUnbakedModel::new, MachineUnbakedModel::variant);
	static final SimplePreparableReloadListener<Void> RELOAD_LISTENER;
	private static final Map<BlockState, BakedQuad[]> BLOCK_STATE_QUAD_CACHE = new IdentityHashMap<>();
	private static final Table<ResourceLocation, Direction, BakedQuad> FACE_QUAD_CACHE = HashBasedTable.create();

	private static void reset() {
		MachineUnbakedModel.BLOCK_STATE_QUAD_CACHE.clear();
		MachineUnbakedModel.FACE_QUAD_CACHE.clear();
	}

	@Override
	public BlockStateModel bake(final ModelBaker baker) {
		return new WrappedVariant(this.variant.bake(baker));
	}

	@Override
	public void resolveDependencies(final Resolver resolver) {
		this.variant.resolveDependencies(resolver);
	}

	@Override
	public MapCodec<? extends CustomUnbakedBlockStateModel> codec() {
		return MachineUnbakedModel.MAP_CODEC;
	}

	private static BakedQuad[] getBlockStateQuads(final BlockState blockState, final BlockAndTintGetter level, final BlockPos pos, final RandomSource random) {
		return MachineUnbakedModel.BLOCK_STATE_QUAD_CACHE.computeIfAbsent(blockState, state -> ModelUtils.getBaseQuadsFromBlockState(state, level, pos, random));
	}

	private static @Nullable BakedQuad getFaceQuad(final ResourceLocation texture, final Direction face) {
		if (MachineUnbakedModel.FACE_QUAD_CACHE.isEmpty()) {
			final BiConsumer<ResourceLocation, Boolean> filler = (tex, emissive) -> {
				for (final Direction direction : Direction.values()) {
					MachineUnbakedModel.FACE_QUAD_CACHE.put(tex, direction, ModelUtils.createFullCubeBakedQuad(emissive, direction, tex));
				}
			};
			filler.accept(MachineUnbakedModel.TEXTURE_IO_PORT, false);
			filler.accept(MachineUnbakedModel.TEXTURE_IO_PORT_ITEM_OFF, false);
			filler.accept(MachineUnbakedModel.TEXTURE_IO_PORT_ITEM_ON, true);
			filler.accept(MachineUnbakedModel.TEXTURE_IO_PORT_FLUID_OFF, false);
			filler.accept(MachineUnbakedModel.TEXTURE_IO_PORT_FLUID_ON, true);
			filler.accept(MachineUnbakedModel.TEXTURE_IO_PORT_BOTH_ITEM_OFF, false);
			filler.accept(MachineUnbakedModel.TEXTURE_IO_PORT_BOTH_ITEM_ON, true);
			filler.accept(MachineUnbakedModel.TEXTURE_IO_PORT_BOTH_FLUID_OFF, false);
			filler.accept(MachineUnbakedModel.TEXTURE_IO_PORT_BOTH_FLUID_ON, true);
		}
		return MachineUnbakedModel.FACE_QUAD_CACHE.get(texture, face);
	}

	private static class WrappedVariant extends SingleVariant {

		WrappedVariant(final BlockModelPart model) {
			super(model);
		}

		@Override
		public void collectParts(final BlockAndTintGetter level, final BlockPos pos, final BlockState state, final RandomSource random, final List<BlockModelPart> parts) {
			super.collectParts(level, pos, state, random, parts);
			if (parts.isEmpty()) {
				return;
			}
			final ModelData modelData = level.getModelData(pos);
			MachineUnbakedModel.replaceBasePart(parts, modelData, level, pos, state, random);
			MachineUnbakedModel.addAutoOutputQuads(parts, modelData);
		}
	}

	private static void replaceBasePart(final List<BlockModelPart> parts, final ModelData modelData, final BlockAndTintGetter level, final BlockPos pos, final BlockState state, final RandomSource random) {
		final BlockState appearance = modelData.get(MachineModelProperties.APPEARANCE);
		if (appearance != null) {
			final BakedQuad[] replacementQuads = MachineUnbakedModel.getBlockStateQuads(appearance, level, pos, random);
			if (replacementQuads.length > 0) {
				parts.set(0, new ModelUtils.ReplacedQuadBlockModelPart(parts.getFirst(), replacementQuads));
			}
		}
	}

	private static void addAutoOutputQuads(final List<BlockModelPart> parts, final ModelData modelData) {
		if (!modelData.has(MachineModelProperties.ITEM_AUTO_OUTPUT) && !modelData.has(MachineModelProperties.FLUID_AUTO_OUTPUT)) {
			return;
		}
		final @Nullable Tuple<Direction, Boolean> itemData = modelData.get(MachineModelProperties.ITEM_AUTO_OUTPUT);
		final @Nullable Tuple<Direction, Boolean> fluidData = modelData.get(MachineModelProperties.FLUID_AUTO_OUTPUT);
		final Direction itemSide = itemData != null ? itemData.getA() : null;
		final boolean itemEnabled = itemData != null && itemData.getB();
		final Direction fluidSide = fluidData != null ? fluidData.getA() : null;
		final boolean fluidEnabled = fluidData != null && fluidData.getB();
		final Map<Direction, List<BakedQuad>> quads = new EnumMap<>(Direction.class);
		assert itemSide != null || fluidSide != null;
		if (itemSide == fluidSide) {
			final List<BakedQuad> list = new ArrayList<>();
			final BakedQuad portQuad = MachineUnbakedModel.getFaceQuad(MachineUnbakedModel.TEXTURE_IO_PORT, itemSide);
			if (portQuad != null) {
				list.add(portQuad);
			}
			final BakedQuad itemQuad = MachineUnbakedModel.getFaceQuad(itemEnabled ? MachineUnbakedModel.TEXTURE_IO_PORT_BOTH_ITEM_ON : MachineUnbakedModel.TEXTURE_IO_PORT_BOTH_ITEM_OFF, itemSide);
			if (itemQuad != null) {
				list.add(itemQuad);
			}
			final BakedQuad fluidQuad = MachineUnbakedModel.getFaceQuad(fluidEnabled ? MachineUnbakedModel.TEXTURE_IO_PORT_BOTH_FLUID_ON : MachineUnbakedModel.TEXTURE_IO_PORT_BOTH_FLUID_OFF, itemSide);
			if (fluidQuad != null) {
				list.add(fluidQuad);
			}
			if (!list.isEmpty()) {
				quads.put(itemSide, list);
			}
		} else {
			if (itemSide != null) {
				final List<BakedQuad> list = new ArrayList<>();
				final BakedQuad portQuad = MachineUnbakedModel.getFaceQuad(MachineUnbakedModel.TEXTURE_IO_PORT, itemSide);
				if (portQuad != null) {
					list.add(portQuad);
				}
				final BakedQuad itemQuad = MachineUnbakedModel.getFaceQuad(itemEnabled ? MachineUnbakedModel.TEXTURE_IO_PORT_ITEM_ON : MachineUnbakedModel.TEXTURE_IO_PORT_ITEM_OFF, itemSide);
				if (itemQuad != null) {
					list.add(itemQuad);
				}
				if (!list.isEmpty()) {
					quads.put(itemSide, list);
				}
			}
			if (fluidSide != null) {
				final List<BakedQuad> list = new ArrayList<>();
				final BakedQuad portQuad = MachineUnbakedModel.getFaceQuad(MachineUnbakedModel.TEXTURE_IO_PORT, fluidSide);
				if (portQuad != null) {
					list.add(portQuad);
				}
				final BakedQuad fluidQuad = MachineUnbakedModel.getFaceQuad(fluidEnabled ? MachineUnbakedModel.TEXTURE_IO_PORT_FLUID_ON : MachineUnbakedModel.TEXTURE_IO_PORT_FLUID_OFF, fluidSide);
				if (fluidQuad != null) {
					list.add(fluidQuad);
				}
				if (!list.isEmpty()) {
					quads.put(fluidSide, list);
				}
			}
		}
		if (!quads.isEmpty()) {
			parts.add(new ModelUtils.SimpleOverlayQuadBlockModelPart(quads, parts.getFirst().particleIcon()));
		}
	}

	static {
		RELOAD_LISTENER = new SimplePreparableReloadListener<>() {
			@Override
			protected Void prepare(final ResourceManager resourceManager, final ProfilerFiller profiler) {
				return null;
			}

			@Override
			protected void apply(final Void object, final ResourceManager resourceManager, final ProfilerFiller profiler) {
				MachineUnbakedModel.reset();
			}
		};
	}
}
