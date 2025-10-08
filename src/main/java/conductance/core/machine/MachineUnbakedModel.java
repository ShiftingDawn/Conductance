package conductance.core.machine;

import java.util.ArrayList;
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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TriState;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.multi.IMultiBlockController;
import conductance.api.machine.multi.IMultiBlockPart;
import conductance.client.model.ExtendedRotationVariant;

public record MachineUnbakedModel(ExtendedRotationVariant variant) implements CustomUnbakedBlockStateModel {
	public static final MapCodec<MachineUnbakedModel> MAP_CODEC = ExtendedRotationVariant.MAP_CODEC.xmap(MachineUnbakedModel::new, MachineUnbakedModel::variant);
	private static final Map<BlockState, BakedQuad[]> QUAD_CACHE = new IdentityHashMap<>();

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
			if (!parts.isEmpty() && level.getBlockEntity(pos) instanceof final IMultiBlockPart multiBlockPart) {
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
		}
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
}
