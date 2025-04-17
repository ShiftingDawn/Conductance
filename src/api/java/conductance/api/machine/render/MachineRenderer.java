package conductance.api.machine.render;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.common.util.TriState;
import com.lowdragmc.lowdraglib.client.model.ModelFactory;
import com.lowdragmc.lowdraglib.client.model.custommodel.ICTMPredicate;
import com.lowdragmc.lowdraglib.client.renderer.IItemRendererProvider;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.IMachineBlock;
import conductance.api.machine.IMachineBlockItem;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineType;

//TODO covers
public class MachineRenderer extends TextureOverrideRenderer implements /* ICoverRenderer,  */ ICTMPredicate {

	private static final ResourceLocation TEXTURE_IO_PORT = ResourceLocation.fromNamespaceAndPath(CAPI.MOD_ID, "block/machine/machine_hull/io_port");

	public MachineRenderer(final ResourceLocation modelLocation) {
		super(modelLocation);
	}

	@OnlyIn(Dist.CLIENT)
	public void renderBaseModel(
			final List<BakedQuad> quads, final MachineType<?> machineType, @Nullable final MachineBlockEntity<?> machine, final Direction frontFacing, @Nullable final Direction side,
			final RandomSource rand) {
		quads.addAll(this.getRotatedModel(frontFacing).getQuads(machineType.getDefaultBlockState(), side, rand));
	}

	@OnlyIn(Dist.CLIENT)
	public void renderMachine(
			final List<BakedQuad> quads, final MachineType<?> machineType, @Nullable final MachineBlockEntity<?> machine, final Direction front, @Nullable final Direction side,
			final RandomSource rand, @Nullable final Direction modelFacing, final ModelState modelState
	) {
		//Dynamic rendering like CTM, multi texture replacement here
		this.renderBaseModel(quads, machineType, machine, front, side, rand);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderItem(
			final ItemStack stack, final ItemDisplayContext transformType, final boolean leftHand, final PoseStack poseStack, final MultiBufferSource buffer, final int combinedLight,
			final int combinedOverlay, final BakedModel model
	) {
		if (stack.getItem() instanceof final IMachineBlockItem<?> machineItem) {
			IItemRendererProvider.disabled.set(true);
			Minecraft.getInstance().getItemRenderer().render(stack, transformType, leftHand, poseStack, buffer, combinedLight, combinedOverlay, (BakedModelItemDefaults) (state, direction, random) -> {
				final List<BakedQuad> quads = new LinkedList<>();
				MachineRenderer.this.renderMachine(quads, machineItem.getMachineType(), null, Direction.NORTH, direction, random, direction, BlockModelRotation.X0_Y0);
				return quads;
			});
			IItemRendererProvider.disabled.set(false);
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public List<BakedQuad> renderModel(
			@Nullable final BlockAndTintGetter level, @Nullable final BlockPos pos, @Nullable final BlockState state, @Nullable final Direction side, final RandomSource rand,
			final ModelData data, @Nullable final RenderType renderType
	) {
		if (state != null && state.getBlock() instanceof final IMachineBlock<?> machineBlock) {
			final Direction machineFrontFace = machineBlock.getFrontFacing(state);
			final ModelState modelState = ModelFactory.getRotation(machineFrontFace);
			final Direction modelFacing = side == null ? null : ModelFactory.modelFacing(side, machineFrontFace);
			final BlockEntity be = level != null && pos != null ? level.getBlockEntity(pos) : null;
			if (be != null) {
				final List<BakedQuad> quads = new LinkedList<>();
				if (be instanceof final MachineBlockEntity<?> machine) {
					final MachineType<?> machineType = machine.getMachineType();
					this.renderMachine(quads, machineType, machine, machineFrontFace, side, rand, modelFacing, modelState);
				}
				//TODO covers
//				if (be instanceof final ICoverable coverable) {
//					this.renderCovers(quads, side, rand, coverable, modelFacing, modelState);
//				}
				return quads;
			}
		}
		return Collections.emptyList();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean useBlockLight(final ItemStack stack) {
		return true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public TriState useAO() {
		return TriState.TRUE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void onPrepareTextureAtlas(final ResourceLocation atlasName, final Consumer<ResourceLocation> register) {
		super.onPrepareTextureAtlas(atlasName, register);
		if (atlasName.equals(InventoryMenu.BLOCK_ATLAS)) {
			register.accept(MachineRenderer.TEXTURE_IO_PORT);
		}
	}

	@Override
	public boolean isConnected(final BlockAndTintGetter level, final BlockState state, final BlockPos pos, final BlockState sourceState, final BlockPos sourcePos, final Direction side) {
		final BlockState state1 = state.getAppearance(level, pos, side, sourceState, sourcePos);
		final BlockState state2 = sourceState.getAppearance(level, sourcePos, side, state, pos);
		return state1 == state2;
	}
}
