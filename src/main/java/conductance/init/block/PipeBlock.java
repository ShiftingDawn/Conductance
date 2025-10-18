package conductance.init.block;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.util.Lazy;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.lib.pipenet.INetworkNode;
import conductance.lib.pipenet.LevelPipeNetwork;

public abstract class PipeBlock<NODE extends INetworkNode<NODE, DATA>, DATA, LEVELNET extends LevelPipeNetwork<NODE, DATA>> extends SimpleBlock implements EntityBlock {

	public static final Map<Direction, BooleanProperty> CONNECTION_PROPS;
	@Getter
	private final ResourceLocation networkType;
	private final Lazy<VoxelShape> baseShape = Lazy.of(this::makeBaseShape);
	private final Lazy<Map<Direction, VoxelShape>> extensionShapes = Lazy.of(() -> Collections.unmodifiableMap(Util.make(new EnumMap<>(Direction.class), this::makeExtensionShapes)));

	public PipeBlock(final BlockBehaviour.Properties properties, final ResourceLocation networkType) {
		super(properties);
		this.networkType = networkType;
		this.registerDefaultState(Util.make(() -> {
			BlockState s = this.defaultBlockState();
			for (final Direction d : Direction.values()) {
				s = s.setValue(PipeBlock.CONNECTION_PROPS.get(d), false);
			}
			return s;
		}));
	}

	protected abstract VoxelShape makeBaseShape();

	protected abstract void makeExtensionShapes(Map<Direction, VoxelShape> map);

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(PipeBlock.CONNECTION_PROPS.values().toArray(Property[]::new));
	}

	public abstract BlockEntityType<? extends PipeBlockEntity<NODE, DATA, LEVELNET>> getBlockEntityType();

	@Override
	public BlockEntity newBlockEntity(final BlockPos pos, final BlockState state) {
		return this.getBlockEntityType().create(pos, state);
	}

	@SuppressWarnings("unchecked")
	@Nullable
	public NODE getPipeBlockEntity(final BlockGetter level, final BlockPos pos) {
		if (level.getBlockEntity(pos) instanceof final INetworkNode<?, ?> node && node.getNodeType().equals(this.getNetworkType())) {
			return (NODE) node;
		}
		return null;
	}

	@Override
	public boolean isCollisionShapeFullBlock(final BlockState state, final BlockGetter level, final BlockPos pos) {
		return false;
	}

	@Override
	protected void neighborChanged(final BlockState state, final Level level, final BlockPos pos, final Block neighborBlock, @Nullable final Orientation orientation, final boolean movedByPiston) {
		super.neighborChanged(state, level, pos, neighborBlock, orientation, movedByPiston);
		if (level.getBlockEntity(pos) instanceof final PipeBlockEntity<?, ?, ?> pipe) {
			pipe.onNeighborChanged();
		}
	}

	@Override
	public VoxelShape getShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext ctx) {
		final NODE pipeNode = this.getPipeBlockEntity(level, pos);
		if (pipeNode != null) {
			final VoxelShape shape = PipeBlock.makeShapeFromConnections(state, this.baseShape.get(), this.extensionShapes.get());
			//TODO covers and facades
			// shape = Shapes.or(shape, ((ICoverable) pipeNode).getCoverManager().getCoverCollisionShapes());
			if (ctx instanceof final EntityCollisionContext entityCtx && entityCtx.getEntity() instanceof final Player player) {
				final ItemStack held = player.getMainHandItem();
				if (
					held.is(CAPI.TAG_WIRE_CUTTERS) || held.is(CAPI.TAG_WRENCHES)
						// || (held.getItem() instanceof final ICoverItem<?> coverItem && ((ICoverable) pipeNode).getCoverManager().canAcceptCover(coverItem.getCoverType(), null))
						|| (held.getItem() instanceof final BlockItem blockItem && blockItem.getBlock() instanceof final PipeBlock<?, ?, ?> pipeBlock
						&& pipeBlock.networkType.equals(this.networkType))
				) {
					return Shapes.or(Shapes.block(), shape);
				}
			}
			return shape;
		}
		return this.baseShape.get();
	}

	// TODO covers
//	@Override
//	public BlockState getAppearance(final BlockState state, final BlockAndTintGetter level, final BlockPos pos, final Direction side, @Nullable final BlockState queryState, @Nullable final BlockPos queryPos) {
//		final NODE node = this.getPipeBlockEntity(level, pos);
//		if (node != null) {
//			final BlockState appearance = ((ICoverable) node).getCoverManager().getAppearance(state, level, pos, side, queryState, queryPos);
//			if (appearance != state) {
//				return appearance;
//			}
//		}
//		return super.getAppearance(state, level, pos, side, queryState, queryPos);
//	}

	@Override
	public List<ItemStack> getDrops(final BlockState state, final LootParams.Builder builder) {
		final LootParams lootParams = builder.withParameter(LootContextParams.BLOCK_STATE, state).create(LootContextParamSets.BLOCK);
		final BlockEntity blockEntity = lootParams.contextMap().getOrDefault(LootContextParams.BLOCK_ENTITY, null);
		if (blockEntity instanceof final INetworkNode<?, ?> networkNode) {
			for (final Direction direction : Direction.values()) {
				// TODO covers
				// ((ICoverable) networkNode).getCoverManager().removeCover(direction);
			}
		}
		return super.getDrops(state, builder);
	}

	public static VoxelShape makeShapeFromConnections(final BlockState state, final VoxelShape baseShape, final Map<Direction, VoxelShape> extensionShapes) {
		VoxelShape result = baseShape;
		for (final Direction direction : Direction.values()) {
			if (state.getValue(PipeBlock.CONNECTION_PROPS.get(direction))) {
				result = Shapes.or(result, extensionShapes.get(direction));
			}
		}
		return result;
	}

	static {
		CONNECTION_PROPS = Util.make(new EnumMap<>(Direction.class), map -> {
			for (final Direction d : Direction.values()) {
				map.put(d, BooleanProperty.create(d.getName()));
			}
		});
	}
}
