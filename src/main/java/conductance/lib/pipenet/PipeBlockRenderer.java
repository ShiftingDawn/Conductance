package conductance.lib.pipenet;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.common.util.TriState;
import com.lowdragmc.lowdraglib.client.model.ModelFactory;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.cover.ICoverable;
import conductance.init.block.PipeBlock;

public class PipeBlockRenderer implements IRenderer {

	@Getter
	private final Lazy<PipeModel> pipeModel;
	private final PipeBlock<?, ?, ?> block;

	public PipeBlockRenderer(final Lazy<PipeModel> pipeModel, final PipeBlock<?, ?, ?> block) {
		this.pipeModel = pipeModel;
		this.block = block;
		if (CAPI.isClient()) {
			this.registerEvent();
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderItem(
			final ItemStack stack, final ItemDisplayContext transformType, final boolean leftHand, final PoseStack matrixStack, final MultiBufferSource buffer, final int combinedLight,
			final int combinedOverlay, final BakedModel model
	) {
		this.pipeModel.get().renderItem(this.block, stack, transformType, leftHand, matrixStack, buffer, combinedLight, combinedOverlay, model);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public TriState useAO(final BlockState state, final ModelData modelData, final RenderType renderType) {
		return TriState.TRUE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean useBlockLight(final ItemStack stack) {
		return true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public List<BakedQuad> renderModel(
			@Nullable final BlockAndTintGetter level, @Nullable final BlockPos pos, @Nullable final BlockState state, @Nullable final Direction side, final RandomSource rand,
			final ModelData data, @Nullable final RenderType renderType
	) {
		if (level == null) {
			return this.pipeModel.get().bakeQuads(side, PipeNetHelper.ITEM_CONNECTIONS);
		} else if (level.getBlockEntity(pos) instanceof final INetworkNode<?, ?> networkNode) {
			final List<BakedQuad> quads = new LinkedList<>(this.pipeModel.get().bakeQuads(side, networkNode.getConnections()));
			final ModelState modelState = ModelFactory.getRotation(((ICoverable) networkNode).getFrontFacing());
			final Direction modelFacing = side == null ? null : ModelFactory.modelFacing(side, ((ICoverable) networkNode).getFrontFacing());
			return quads;
		}
		return Collections.emptyList();
	}

	@NotNull
	@Override
	@OnlyIn(Dist.CLIENT)
	public TextureAtlasSprite getParticleTexture() {
		return this.pipeModel.get().getParticleTexture();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void onPrepareTextureAtlas(final ResourceLocation atlas, final Consumer<ResourceLocation> register) {
		if (atlas.equals(InventoryMenu.BLOCK_ATLAS)) {
			this.pipeModel.get().registerTextureAtlas(register);
		}
	}
}
