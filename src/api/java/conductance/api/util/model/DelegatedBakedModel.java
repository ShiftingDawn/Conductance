package conductance.api.util.model;

import java.util.List;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.common.util.TriState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class DelegatedBakedModel<T extends BakedModel> implements IDynamicBakedModel {

	@Getter
	private final T delegate;

	@Override
	@Deprecated
	public List<BakedQuad> getQuads(@Nullable final BlockState blockState, @Nullable final Direction direction, final RandomSource randomSource) {
		return this.delegate.getQuads(blockState, direction, randomSource);
	}

	@Override
	public boolean useAmbientOcclusion() {
		return this.delegate.useAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return this.delegate.isGui3d();
	}

	@Override
	public boolean usesBlockLight() {
		return this.delegate.usesBlockLight();
	}

	@Override
	public boolean isCustomRenderer() {
		return this.delegate.isCustomRenderer();
	}

	@Deprecated
	@Override
	public TextureAtlasSprite getParticleIcon() {
		return this.delegate.getParticleIcon();
	}

	@Deprecated
	@Override
	public ItemTransforms getTransforms() {
		return this.delegate.getTransforms();
	}

	@Override
	public ItemOverrides getOverrides() {
		return this.delegate.getOverrides();
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable final BlockState state, @Nullable final Direction side, final RandomSource rand, final ModelData data, @Nullable final RenderType renderType) {
		return this.delegate.getQuads(state, side, rand, data, renderType);
	}

	@Override
	public TriState useAmbientOcclusion(final BlockState state, final ModelData data, final RenderType renderType) {
		return this.delegate.useAmbientOcclusion(state, data, renderType);
	}

	@Override
	public BakedModel applyTransform(final ItemDisplayContext transformType, final PoseStack poseStack, final boolean applyLeftHandTransform) {
		return this.delegate.applyTransform(transformType, poseStack, applyLeftHandTransform);
	}

	@Override
	public ModelData getModelData(final BlockAndTintGetter level, final BlockPos pos, final BlockState state, final ModelData modelData) {
		return this.delegate.getModelData(level, pos, state, modelData);
	}

	@Override
	public TextureAtlasSprite getParticleIcon(final ModelData data) {
		return this.delegate.getParticleIcon(data);
	}

	@Override
	public ChunkRenderTypeSet getRenderTypes(final BlockState state, final RandomSource rand, final ModelData data) {
		return this.delegate.getRenderTypes(state, rand, data);
	}

	@Override
	public List<RenderType> getRenderTypes(final ItemStack itemStack, final boolean fabulous) {
		return this.delegate.getRenderTypes(itemStack, fabulous);
	}

	@Override
	public List<BakedModel> getRenderPasses(final ItemStack itemStack, final boolean fabulous) {
		return this.delegate.getRenderPasses(itemStack, fabulous);
	}
}
