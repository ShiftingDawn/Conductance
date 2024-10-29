package conductance.api.machine;

import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import conductance.api.util.MiscUtils;

@SuppressWarnings("deprecation")
public class MetaBlockEntityBlock<T extends MetaBlockEntity<T>> extends Block implements EntityBlock {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty LIT = BlockStateProperties.LIT;
	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

	@Getter
	private final MetaBlockEntityType<T> metaBlockEntityType;

	public MetaBlockEntityBlock(final Properties props, final MetaBlockEntityType<T> metaBlockEntityType) {
		super(props);
		this.metaBlockEntityType = metaBlockEntityType;
		this.registerDefaultState(this.getStateDefinition().any()
				.setValue(MetaBlockEntityBlock.FACING, Direction.NORTH)
				.setValue(MetaBlockEntityBlock.LIT, false)
				.setValue(MetaBlockEntityBlock.ACTIVE, false)
		);
	}

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(MetaBlockEntityBlock.FACING, MetaBlockEntityBlock.LIT, MetaBlockEntityBlock.ACTIVE);
	}

	@Override
	public BlockState getStateForPlacement(final BlockPlaceContext context) {
		return this.defaultBlockState().setValue(MetaBlockEntityBlock.FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected BlockState rotate(final BlockState state, final Rotation rotation) {
		return state.setValue(MetaBlockEntityBlock.FACING, rotation.rotate(state.getValue(MetaBlockEntityBlock.FACING)));
	}

	@Override
	protected BlockState mirror(final BlockState state, final Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(MetaBlockEntityBlock.FACING)));
	}

	@Override
	public BlockEntity newBlockEntity(final BlockPos blockPos, final BlockState blockState) {
		return this.getMetaBlockEntityType().getBlockEntityType().get().create(blockPos, blockState);
	}

	@Nullable
	@Override
	public <BE extends BlockEntity> BlockEntityTicker<BE> getTicker(final Level level, final BlockState state, final BlockEntityType<BE> blockEntityType) {
		if (blockEntityType == this.metaBlockEntityType.getBlockEntityType().get()) {
			if (level.isClientSide()) {
				return (level1, blockPos, blockState, be) -> {
					if (be instanceof final MetaBlockEntity<?> baseBlockEntity) {
						baseBlockEntity.onClientTick();
					}
				};
			} else {
				if (state.getValue(MetaBlockEntityBlock.ACTIVE)) {
					return (level1, blockPos, blockState, be) -> {
						if (be instanceof final MetaBlockEntity<?> baseBlockEntity) {
							baseBlockEntity.handleServerTick();
						}
					};
				}
			}
		}
		return null;
	}

	public final void setMeta(final BlockGetter level, final BlockPos pos, final Consumer<MetaBlockEntity<?>> setter) {
		if (level.getBlockEntity(pos) instanceof final MetaBlockEntity<?> mbe) {
			setter.accept(mbe);
		}
	}

	@Contract("_, _, _, !null -> !null; _, _, _, null -> null")
	@Nullable
	public final <R> R getMeta(final BlockGetter level, final BlockPos pos, final Function<MetaBlockEntity<?>, R> getter, @Nullable final R fallback) {
		if (level.getBlockEntity(pos) instanceof final MetaBlockEntity<?> mbe) {
			final R result = getter.apply(mbe);
			return result != null ? result : fallback;
		}
		return fallback;
	}

	@Override
	public void onNeighborChange(final BlockState state, final LevelReader level, final BlockPos pos, final BlockPos neighbor) {
		super.onNeighborChange(state, level, pos, neighbor);
		this.setMeta(level, pos, mbe -> mbe.onNeighborChanged(neighbor, level.getBlockState(neighbor), MiscUtils.getNeighborSide(pos, neighbor)));
	}

	@Override
	public boolean canConnectRedstone(final BlockState state, final BlockGetter level, final BlockPos pos, @Nullable final Direction direction) {
		return this.getMeta(level, pos, mbe -> mbe.canConnectRedstone(direction), super.canConnectRedstone(state, level, pos, direction));
	}

	@Override
	protected int getSignal(final BlockState state, final BlockGetter level, final BlockPos pos, final Direction direction) {
		return this.getMeta(level, pos, mbe -> mbe.getRedstone(direction), super.getSignal(state, level, pos, direction));
	}

	@Override
	protected int getDirectSignal(final BlockState state, final BlockGetter level, final BlockPos pos, final Direction direction) {
		return this.getMeta(level, pos, mbe -> mbe.getRedstoneDirect(direction), super.getDirectSignal(state, level, pos, direction));
	}

	@Override
	protected int getAnalogOutputSignal(final BlockState state, final Level level, final BlockPos pos) {
		return this.getMeta(level, pos, MetaBlockEntity::getRedstoneAnalog, super.getAnalogOutputSignal(state, level, pos));
	}
}
