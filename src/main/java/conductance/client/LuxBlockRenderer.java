package conductance.client;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.DyeColor;
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
import com.lowdragmc.lowdraglib.client.model.custommodel.ICTMPredicate;
import com.lowdragmc.lowdraglib.client.renderer.IItemRendererProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.render.BakedModelItemDefaults;
import conductance.api.machine.render.RebakedModelRenderer;
import conductance.Conductance;
import conductance.block.DecoLuxBlock;

public class LuxBlockRenderer extends RebakedModelRenderer implements ICTMPredicate {

	private final ResourceLocation texture;

	public LuxBlockRenderer(final DyeColor dyeColor) {
		super(ResourceLocation.withDefaultNamespace("block/cube_all"));
		this.texture = Conductance.id("block/decoration/lux/%s".formatted(dyeColor.getSerializedName()));
	}

	@Override
	public void renderItem(
			final ItemStack stack, final ItemDisplayContext transformType, final boolean leftHand, final PoseStack poseStack, final MultiBufferSource buffer,
			final int combinedLight, final int combinedOverlay, final BakedModel model
	) {
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
		final List<BakedQuad> quads = new LinkedList<>();
		this.renderBlock(quads, side);
		return quads;
	}

	@OnlyIn(Dist.CLIENT)
	private void renderBlock(final List<BakedQuad> quads, @Nullable final Direction side) {
		if (side != null) {
			quads.add(FaceQuad.bakeFace(side, ModelFactory.getBlockSprite(this.texture), BlockModelRotation.X0_Y0, 0, 15, true, false));
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void onPrepareTextureAtlas(final ResourceLocation atlasName, final Consumer<ResourceLocation> register) {
		super.onPrepareTextureAtlas(atlasName, register);
		if (atlasName.equals(InventoryMenu.BLOCK_ATLAS)) {
			register.accept(this.texture);
		}
	}

	@NotNull
	@Override
	public TextureAtlasSprite getParticleTexture() {
		return ModelFactory.getBlockSprite(this.texture);
	}

	@Override
	public boolean isConnected(final BlockAndTintGetter level, final BlockState state, final BlockPos pos, final BlockState sourceState, final BlockPos sourcePos, final Direction side) {
		return state.getBlock() instanceof DecoLuxBlock && sourceState.getBlock() instanceof DecoLuxBlock;
	}
}