package conductance.client;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
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
import com.lowdragmc.lowdraglib.client.bakedpipeline.FaceQuad;
import com.lowdragmc.lowdraglib.client.model.ModelFactory;
import com.lowdragmc.lowdraglib.client.renderer.IItemRendererProvider;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.render.BakedModelItemDefaults;
import conductance.api.machine.render.TextureOverrideRenderer;

public class EmissiveOverlayRenderer extends TextureOverrideRenderer {

	private final ResourceLocation overlay;

	public EmissiveOverlayRenderer(final ResourceLocation texture, final ResourceLocation overlay) {
		super(ResourceLocation.withDefaultNamespace("block/cube_all"), Map.of("all", texture));
		this.overlay = overlay;
	}

	@Override
	public void renderItem(
			final ItemStack stack, final ItemDisplayContext transformType, final boolean leftHand, final PoseStack poseStack, final MultiBufferSource buffer,
			final int combinedLight, final int combinedOverlay, final BakedModel model
	) {
		super.renderItem(stack, transformType, leftHand, poseStack, buffer, combinedLight, combinedOverlay, model);
		IItemRendererProvider.disabled.set(true);
		Minecraft.getInstance().getItemRenderer().render(stack, transformType, leftHand, poseStack, buffer, combinedLight, combinedOverlay, (BakedModelItemDefaults) (state, direction, random) -> {
			final List<BakedQuad> quads = new LinkedList<>();
			this.renderBlock(quads, direction);
			return quads;
		});
		IItemRendererProvider.disabled.set(false);
	}

	@Override
	public List<BakedQuad> renderModel(
			@Nullable final BlockAndTintGetter level, @Nullable final BlockPos pos, @Nullable final BlockState state,
			@Nullable final Direction side, final RandomSource rand, final ModelData data, @Nullable final RenderType renderType
	) {
		final List<BakedQuad> quads = new LinkedList<>(super.renderModel(level, pos, state, side, rand, data, renderType));
		this.renderBlock(quads, side);
		return quads;
	}

	@OnlyIn(Dist.CLIENT)
	private void renderBlock(final List<BakedQuad> quads, @Nullable final Direction side) {
		if (side != null) {
			quads.add(FaceQuad.bakeFace(side, ModelFactory.getBlockSprite(this.overlay), BlockModelRotation.X0_Y0, 1, 15, true, false));
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void onPrepareTextureAtlas(final ResourceLocation atlasName, final Consumer<ResourceLocation> register) {
		super.onPrepareTextureAtlas(atlasName, register);
		if (atlasName.equals(InventoryMenu.BLOCK_ATLAS)) {
			register.accept(this.overlay);
		}
	}
}
