package conductance.api.machine;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import com.lowdragmc.lowdraglib.gui.factory.BlockEntityUIFactory;
import com.lowdragmc.lowdraglib.gui.modular.IUIHolder;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.util.world.RotationState;
import conductance.api.util.world.WorldUtils;

@SuppressWarnings("deprecation")
public class MachineBlock<T extends MachineBlockEntity<T>> extends Block implements IMachineBlock<T> {

	public static final BooleanProperty LIT = BlockStateProperties.LIT;
	private final MachineType<T> machineType;
	@Getter
	private final RotationState rotationState;

	public MachineBlock(final Properties props, final MachineType<T> machineType) {
		super(props);
		this.machineType = machineType;
		this.rotationState = RotationState.get();
		if (this.rotationState != RotationState.NONE) {
			this.registerDefaultState(this.getStateDefinition().any()
					.setValue(this.rotationState.property, this.rotationState.defaultDirection)
					.setValue(MachineBlock.LIT, false)
			);
		}
	}

	@Override
	public MachineType<T> getMachineType() {
		return this.machineType;
	}

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(MachineBlock.LIT);
		final RotationState rotState = RotationState.get();
		if (rotState != RotationState.NONE) {
			builder.add(rotState.property);
		}
	}

	@Override
	public MutableComponent getName() {
		final String localized = this.getMachineType().getLocalizedName();
		return localized != null ? Component.literal(localized) : super.getName();
	}

	@Override
	public BlockState getStateForPlacement(final BlockPlaceContext context) {
		final RotationState rotState = this.getRotationState();
		final Player player = context.getPlayer();
		final BlockPos blockPos = context.getClickedPos();
		final BlockState state = this.defaultBlockState();
		if (player != null && rotState != RotationState.NONE) {
			final Vec3 pos = player.position();
			if (Math.abs(pos.x - (double) ((float) blockPos.getX() + 0.5F)) < 2.0D && Math.abs(pos.z - (double) ((float) blockPos.getZ() + 0.5F)) < 2.0D) {
				final double d0 = pos.y + (double) player.getEyeHeight();
				if (d0 - (double) blockPos.getY() > 2.0D && rotState.test(Direction.UP)) {
					return state.setValue(rotState.property, Direction.UP);
				}
				if ((double) blockPos.getY() - d0 > 0.0D && rotState.test(Direction.DOWN)) {
					return state.setValue(rotState.property, Direction.DOWN);
				}
			}
			if (rotState == RotationState.VERTICAL) {
				return state.setValue(rotState.property, Direction.UP);
			} else {
				return state.setValue(rotState.property, player.getDirection().getOpposite());
			}
		}
		return state;
	}

	@Override
	protected BlockState rotate(final BlockState state, final Rotation rotation) {
		if (this.rotationState == RotationState.NONE) {
			return state;
		}
		return state.setValue(this.rotationState.property, rotation.rotate(state.getValue(this.rotationState.property)));
	}

	@Override
	protected BlockState mirror(final BlockState state, final Mirror mirror) {
		if (this.rotationState == RotationState.NONE) {
			return state;
		}
		return state.rotate(mirror.getRotation(state.getValue(this.rotationState.property)));
	}

	@Override
	protected InteractionResult useWithoutItem(final BlockState state, final Level level, final BlockPos pos, final Player player, final BlockHitResult hitResult) {
		return this.<InteractionResult>getMachine(level, pos, mbt -> {
			if (mbt instanceof final IUIHolder.Block holder && !level.isClientSide && player instanceof final ServerPlayer serverPlayer) {
				BlockEntityUIFactory.INSTANCE.openUI(holder.self(), serverPlayer);
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		}, () -> InteractionResult.PASS);
	}

	@Override
	public void onNeighborChange(final BlockState state, final LevelReader level, final BlockPos pos, final BlockPos neighbor) {
		super.onNeighborChange(state, level, pos, neighbor);
		this.setMachine(level, pos, machine -> machine.onNeighborChanged(neighbor, level.getBlockState(neighbor), WorldUtils.getNeighborSide(pos, neighbor)));
	}

	@Override
	public boolean canConnectRedstone(final BlockState state, final BlockGetter level, final BlockPos pos, @Nullable final Direction direction) {
		return this.getMachine(level, pos, machine -> machine.canConnectRedstone(direction), super.canConnectRedstone(state, level, pos, direction));
	}

	@Override
	protected int getSignal(final BlockState state, final BlockGetter level, final BlockPos pos, final Direction direction) {
		return this.getMachine(level, pos, machine -> machine.getRedstone(direction), super.getSignal(state, level, pos, direction));
	}

	@Override
	protected int getDirectSignal(final BlockState state, final BlockGetter level, final BlockPos pos, final Direction direction) {
		return this.getMachine(level, pos, machine -> machine.getRedstoneDirect(direction), super.getDirectSignal(state, level, pos, direction));
	}

	@Override
	protected int getAnalogOutputSignal(final BlockState state, final Level level, final BlockPos pos) {
		return this.getMachine(level, pos, MachineBlockEntity::getRedstoneAnalog, super.getAnalogOutputSignal(state, level, pos));
	}

	@Override
	public void appendHoverText(final ItemStack stack, final Item.TooltipContext context, final List<Component> tooltip, final TooltipFlag tooltipFlag) {
		this.getMachineType().getTooltipBuilder().accept(stack, tooltip);
		final String mainKey = String.format("tooltip.%s.machine.%s", CAPI.MOD_ID, this.getMachineType().getRegistryKey());
		if (LocalizationUtils.exist(mainKey)) {
			tooltip.add(1, Component.translatable(mainKey));
		}
	}

	@Override
	public void animateTick(final BlockState state, final Level level, final BlockPos pos, final RandomSource random) {
		super.animateTick(state, level, pos, random);
		this.setMachine(level, pos, machine -> machine.onAnimateTick(random));
	}
}
