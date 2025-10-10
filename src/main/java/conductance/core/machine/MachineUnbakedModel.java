package conductance.core.machine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.SimpleModelWrapper;
import net.minecraft.client.renderer.block.model.SingleVariant;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TriState;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.client.model.IQuadTransformer;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.IFluidAutoOutput;
import conductance.api.machine.IItemAutoOutput;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineCapability;
import conductance.api.machine.multi.IMultiBlockController;
import conductance.api.machine.multi.IMultiBlockPart;
import conductance.api.util.ModelUtils;
import conductance.Conductance;
import conductance.client.model.ExtendedRotationVariant;

public record MachineUnbakedModel(ExtendedRotationVariant variant) implements CustomUnbakedBlockStateModel {

	public static final ResourceLocation TEXTURE_IO_PORT = Conductance.id("block/machine/io_port");
	public static final ResourceLocation TEXTURE_IO_PORT_ITEM = Conductance.id("block/machine/io_port_item");
	public static final ResourceLocation TEXTURE_IO_PORT_FLUID = Conductance.id("block/machine/io_port_fluid");
	public static final ResourceLocation TEXTURE_IO_PORT_BOTH = Conductance.id("block/machine/io_port_both");

	public static final MapCodec<MachineUnbakedModel> MAP_CODEC = ExtendedRotationVariant.MAP_CODEC.xmap(MachineUnbakedModel::new, MachineUnbakedModel::variant);
	private static final Map<BlockState, BakedQuad[]> QUAD_CACHE = new IdentityHashMap<>();
	private static final Table<ResourceLocation, Direction, BakedQuad> TEXTURE_CACHE = HashBasedTable.create();

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
			if (level.getBlockEntity(pos) instanceof final IMultiBlockPart multiBlockPart) {
				if (parts.getFirst() instanceof final SimpleModelWrapper modelWrapper) {
					for (final BlockPos controllerPos : multiBlockPart.getControllers()) {
						if (level.getBlockEntity(controllerPos) instanceof final IMultiBlockController<?> controller && controller.isStructureFormed()) {
							final Supplier<BlockState> casingAppearance = controller.getMachineType().getCasingAppearance();
							if (casingAppearance == null) {
								continue;
							}
							final BakedQuad[] quads = MachineUnbakedModel.QUAD_CACHE.computeIfAbsent(casingAppearance.get(), renderState ->
								MachineUnbakedModel.getBlockStateQuads(renderState, level, pos, random)
							);
							if (quads.length > 0) {
								parts.set(0, new WrappedModelPart(modelWrapper, quads));
							}
							return;
						}
					}
				}
			}
			if (level.getBlockEntity(pos) instanceof final MachineBlockEntity<?> machine) {
				if (parts.getFirst() instanceof final SimpleModelWrapper modelWrapper) {
					if (MachineUnbakedModel.TEXTURE_CACHE.isEmpty()) {
						MachineUnbakedModel.fillCache(modelWrapper.quads(), MachineUnbakedModel.TEXTURE_IO_PORT);
						MachineUnbakedModel.fillCache(modelWrapper.quads(), MachineUnbakedModel.TEXTURE_IO_PORT_ITEM);
						MachineUnbakedModel.fillCache(modelWrapper.quads(), MachineUnbakedModel.TEXTURE_IO_PORT_FLUID);
						MachineUnbakedModel.fillCache(modelWrapper.quads(), MachineUnbakedModel.TEXTURE_IO_PORT_BOTH);
					}
					Direction itemSide = null;
					Direction fluidSide = null;
					for (final MachineCapability capability : machine.getCapabilities().values()) {
						if (capability instanceof final IItemAutoOutput itemAutoOutput) {
							itemSide = itemAutoOutput.getItemAutoOutputSide();
						}
						if (capability instanceof final IFluidAutoOutput fluidAutoOutput) {
							fluidSide = fluidAutoOutput.getFluidAutoOutputSide();
						}
					}
					if (itemSide != null || fluidSide != null) {
						final Map<Direction, List<BakedQuad>> quads = new EnumMap<>(Direction.class);
						if (itemSide == fluidSide || itemSide != null) {
							final BakedQuad portQuad = MachineUnbakedModel.TEXTURE_CACHE.get(MachineUnbakedModel.TEXTURE_IO_PORT, itemSide);
							final BakedQuad overlayQuad = MachineUnbakedModel.TEXTURE_CACHE.get(itemSide == fluidSide ? MachineUnbakedModel.TEXTURE_IO_PORT_BOTH : MachineUnbakedModel.TEXTURE_IO_PORT_ITEM, itemSide);
							if (portQuad != null && overlayQuad != null) {
								quads.put(itemSide, List.of(portQuad, overlayQuad));
							}
						}
						if (fluidSide != null) {
							final BakedQuad portQuad = MachineUnbakedModel.TEXTURE_CACHE.get(MachineUnbakedModel.TEXTURE_IO_PORT, fluidSide);
							final BakedQuad overlayQuad = MachineUnbakedModel.TEXTURE_CACHE.get(MachineUnbakedModel.TEXTURE_IO_PORT_FLUID, fluidSide);
							if (portQuad != null && overlayQuad != null) {
								quads.put(fluidSide, List.of(portQuad, overlayQuad));
							}
						}
						if (!quads.isEmpty()) {
							parts.add(new SingleQuadModelPart(quads, modelWrapper.particleIcon()));
						}
					}
				}
			}
		}
	}

	private static void fillCache(final QuadCollection quadCollection, final ResourceLocation texture) {
		for (final Direction face : Direction.values()) {
			quadCollection.getQuads(face).stream().findFirst().ifPresent(
				quad -> MachineUnbakedModel.TEXTURE_CACHE.put(texture, face, MachineUnbakedModel.retextureQuad(quad, ModelUtils.getBlockSprite(texture)))
			);
		}
	}

	private static BakedQuad retextureQuad(final BakedQuad quad, final TextureAtlasSprite sprite) {
		final TextureAtlasSprite oldSprite = quad.sprite();
		final int[] vertices = quad.vertices().clone();
		for (int i = 0; i < 4; i++) {
			final int offset = i * IQuadTransformer.STRIDE + IQuadTransformer.UV0;
			float u = Float.intBitsToFloat(vertices[offset]);
			float v = Float.intBitsToFloat(vertices[offset + 1]);
			u = Mth.map(u, oldSprite.getU0(), oldSprite.getU1(), sprite.getU0(), sprite.getU1());
			v = Mth.map(v, oldSprite.getV0(), oldSprite.getV1(), sprite.getV0(), sprite.getV1());
			vertices[offset] = Float.floatToRawIntBits(u);
			vertices[offset + 1] = Float.floatToRawIntBits(v);
		}
		return new BakedQuad(vertices, quad.tintIndex(), quad.direction(), sprite, quad.shade(), quad.lightEmission(), quad.hasAmbientOcclusion());
	}

	private static BakedQuad[] getBlockStateQuads(final BlockState state, final BlockAndTintGetter level, final BlockPos pos, final RandomSource random) {
		final List<BlockModelPart> newParts = Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(state).collectParts(level, pos, state, random);
		if (newParts.isEmpty()) {
			return new BakedQuad[0];
		}
		final BakedQuad[] result = new BakedQuad[6];
		for (final Direction face : Direction.values()) {
			final List<BakedQuad> quads = newParts.getFirst().getQuads(face);
			result[face.get3DDataValue()] = !quads.isEmpty() ? quads.getFirst() : null;
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	private record WrappedModelPart(BlockModelPart original, BakedQuad[] quads) implements BlockModelPart {

		@Override
		public List<BakedQuad> getQuads(@Nullable final Direction direction) {
			final ArrayList<BakedQuad> result = new ArrayList<>(this.original.getQuads(direction));
			if (direction != null) {
				final BakedQuad quad = this.quads[direction.get3DDataValue()];
				if (quad != null) {
					if (result.isEmpty()) {
						result.add(quad);
					} else {
						result.set(0, quad);
					}
				}
			}
			return result;
		}

		@Override
		public ChunkSectionLayer getRenderType(final BlockState state) {
			return this.original.getRenderType(state);
		}

		@Override
		public TriState ambientOcclusion() {
			return this.original.ambientOcclusion();
		}

		@Override
		public boolean useAmbientOcclusion() {
			return this.original.useAmbientOcclusion();
		}

		@Override
		public TextureAtlasSprite particleIcon() {
			return this.original.particleIcon();
		}
	}

	private record SingleQuadModelPart(Map<Direction, List<BakedQuad>> quads, TextureAtlasSprite particleIcon) implements BlockModelPart {

		@Override
		public List<BakedQuad> getQuads(@Nullable final Direction direction) {
			final List<BakedQuad> result = direction != null ? this.quads.get(direction) : null;
			return result != null ? result : Collections.emptyList();
		}

		@Override
		public boolean useAmbientOcclusion() {
			return true;
		}

		@Override
		public ChunkSectionLayer getRenderType(final BlockState state) {
			return ChunkSectionLayer.CUTOUT_MIPPED;
		}
	}
}
