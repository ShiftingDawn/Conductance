package conductance.api.machine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.cover.ICoverable;

public class BaseBlockEntity extends BlockEntity implements IBlockEntity {

	private final int timerOffset = CAPI.RANDOM.nextInt(20);
	private final List<MachineRunnable> ticks = new ArrayList<>();
	private final List<MachineRunnable> pending = new ArrayList<>();

	public BaseBlockEntity(final BlockEntityType<?> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
	}

	@Override
	public boolean isInvalid() {
		return IBlockEntity.super.isInvalid();
	}

	@Override
	public void onLoad() {
		super.onLoad();
		if (this instanceof final ICoverable coverable) {
			coverable.getCoverManager().onLoaded();
		}
	}

	@Override
	public void onUnload() {
		this.ticks.forEach(MachineRunnable::invalidate);
		this.ticks.clear();
	}

	@Override
	@Nullable
	public final MachineRunnable addTick(final Runnable action) {
		if (!this.isClientSide()) {
			return Util.make(new MachineRunnable(action), result -> {
				this.pending.add(result);
				if (!this.getBlockState().getValue(MachineBlock.LIT) && this.getLevel() instanceof final ServerLevel serverLevel) {
					final BlockState newState = this.getBlockState().setValue(MachineBlock.LIT, true);
					serverLevel.setBlockAndUpdate(this.getBlockPos(), newState);
				}
			});
		}
		return null;
	}

	@Override
	public void handleServerTick() {
		if (!this.pending.isEmpty()) {
			this.ticks.addAll(this.pending);
			this.pending.clear();
		}
		final Iterator<MachineRunnable> iterator = this.ticks.iterator();
		while (iterator.hasNext()) {
			final MachineRunnable tick = iterator.next();
			tick.tick();
			if (this.isInvalid()) {
				break;
			}
			if (!tick.isValid()) {
				iterator.remove();
			}
		}
		if (this.isValid() && this.ticks.isEmpty() && this.pending.isEmpty()) {
			assert this.level != null;
			this.level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(BlockStateProperties.LIT, false));
		}
	}

	@Override
	public final long getTimerOffset() {
		return this.level != null ? this.level.getGameTime() + this.timerOffset : this.timerOffset;
	}


	@Override
	public IBlockEntity getBlockEntity() {
		return this;
	}

	@Override
	public void onNeighborChanged(final BlockPos neighborPos, final BlockState neighborState, final Direction neighborSide) {
	}

	@Override
	public final void setRemoved() {
		super.setRemoved();
		this.onUnload();
		if (this instanceof final ICoverable coverable) {
			coverable.getCoverManager().onUnloaded();
		}
	}
}
