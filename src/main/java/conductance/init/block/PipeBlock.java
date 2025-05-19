package conductance.init.block;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import com.lowdragmc.lowdraglib.client.renderer.IBlockRendererProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.capability.cover.ICoverItem;
import conductance.api.capability.cover.ICoverable;
import conductance.api.machine.IBlockEntityBlock;
import conductance.api.util.MiscUtils;
import conductance.core.pipenet.INetworkNode;
import conductance.core.pipenet.LevelPipeNetwork;
import conductance.core.pipenet.PipeBlockRenderer;
import conductance.core.pipenet.PipeModel;
import conductance.init.ConductanceCreativeTabs;

public abstract class PipeBlock<NODE extends INetworkNode<NODE, DATA>, DATA, LEVELNET extends LevelPipeNetwork<NODE, DATA>> extends ConductanceBlock implements IBlockEntityBlock, IBlockRendererProvider {

	@Getter
	private final ResourceLocation networkType;

	public PipeBlock(final Properties properties, final ResourceLocation networkType) {
		super(properties);
		this.networkType = networkType;
	}

	@Override
	public abstract BlockEntityType<? extends PipeBlockEntity<NODE, DATA, LEVELNET>> getBlockEntityType();

	@Override
	public RegistryEntry<CreativeModeTab, CreativeModeTab> getCreativeTab() {
		return ConductanceCreativeTabs.PIPELIKE;
	}

	@SuppressWarnings("unchecked")
	@Nullable
	public NODE getPipeBlockEntity(final BlockGetter level, final BlockPos pos) {
		if (level.getBlockEntity(pos) instanceof final INetworkNode<?, ?> node && node.getNodeType().equals(this.getNetworkType())) {
			return (NODE) node;
		}
		return null;
	}

	@Nullable
	@Override
	public abstract PipeBlockRenderer getRenderer(BlockState state);

	protected abstract PipeModel getPipeModel();

	@Override
	public boolean isCollisionShapeFullBlock(final BlockState state, final BlockGetter level, final BlockPos pos) {
		return false;
	}

	@Override
	public void onNeighborChange(final BlockState state, final LevelReader level, final BlockPos pos, final BlockPos neighbor) {
		super.onNeighborChange(state, level, pos, neighbor);
		if (level.getBlockEntity(pos) instanceof final PipeBlockEntity<?, ?, ?> pipe) {
			pipe.onNeighborChanged(neighbor, level.getBlockState(neighbor), MiscUtils.getNeighborSide(pos, neighbor));
		}
	}

	@Override
	protected void neighborChanged(final BlockState state, final Level level, final BlockPos pos, final Block neighborBlock, final BlockPos neighborPos, final boolean movedByPiston) {
		super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
	}

	@Override
	public VoxelShape getShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext ctx) {
		final NODE pipeNode = this.getPipeBlockEntity(level, pos);
		if (pipeNode != null) {
			VoxelShape shape = this.getPipeModel().getShapes(pipeNode.getConnections());
			shape = Shapes.or(shape, ((ICoverable) pipeNode).getCoverManager().getCoverCollisionShapes());
			if (ctx instanceof final EntityCollisionContext entityCtx && entityCtx.getEntity() instanceof final Player player) {
				final ItemStack held = player.getMainHandItem();
				if (
						held.is(CAPI.Tags.TAG_WIRE_CUTTERS) || held.is(CAPI.Tags.TAG_WRENCH)
								|| (held.getItem() instanceof final ICoverItem<?> coverItem && ((ICoverable) pipeNode).getCoverManager().canAcceptCover(coverItem.getCoverType(), null))
								|| (held.getItem() instanceof final BlockItem blockItem && blockItem.getBlock() instanceof final PipeBlock<?, ?, ?> pipeBlock
								&& pipeBlock.networkType.equals(this.networkType))
				) {
					return Shapes.or(Shapes.block(), shape);
				}
			}
			return shape;
		}
		return this.getPipeModel().getShapes(0);
	}

	@Override
	public BlockState getAppearance(final BlockState state, final BlockAndTintGetter level, final BlockPos pos, final Direction side, @Nullable final BlockState queryState, @Nullable final BlockPos queryPos) {
		final NODE node = this.getPipeBlockEntity(level, pos);
		if (node != null) {
			final BlockState appearance = ((ICoverable) node).getCoverManager().getAppearance(state, level, pos, side, queryState, queryPos);
			if (appearance != state) {
				return appearance;
			}
		}
		return super.getAppearance(state, level, pos, side, queryState, queryPos);
	}

	@Override
	public List<ItemStack> getDrops(final BlockState state, final LootParams.Builder builder) {
		final LootParams lootParams = builder.withParameter(LootContextParams.BLOCK_STATE, state).create(LootContextParamSets.BLOCK);
		final BlockEntity blockEntity = lootParams.getParamOrNull(LootContextParams.BLOCK_ENTITY);
		if (blockEntity instanceof final INetworkNode<?, ?> networkNode) {
			for (final Direction direction : Direction.values()) {
				((ICoverable) networkNode).getCoverManager().removeCover(direction);
			}
		}
		return super.getDrops(state, builder);
	}
}
