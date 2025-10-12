package conductance.init.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCCapabilities;
import conductance.api.block.InteractType;
import conductance.api.machine.energy.IEnergyHandler;
import conductance.init.ConductanceBlockEntities;
import conductance.lib.pipenet.EnergyNet;
import conductance.lib.pipenet.EnergyNetHandler;
import conductance.lib.pipenet.IWireNode;
import conductance.lib.pipenet.LevelEnergyNet;
import conductance.lib.pipenet.PerTickLongHandler;
import conductance.lib.pipenet.PipeNetHelper;
import conductance.lib.pipenet.WireData;

public final class WireBlockEntity extends PipeBlockEntity<IWireNode, WireData, LevelEnergyNet> implements IWireNode {

	private final PerTickLongHandler counter = new PerTickLongHandler(0L);

	public WireBlockEntity(final BlockPos pos, final BlockState state) {
		super(ConductanceBlockEntities.WIRE.get(), pos, state);
	}

	@Override
	public LevelEnergyNet getNetwork(final ServerLevel serverLevel) {
		return LevelEnergyNet.getOrCreate(serverLevel);
	}

	@Override
	protected InteractType getInteractType() {
		return InteractType.WIRE_CUTTERS;
	}

	@Override
	public WireData getData() {
		return ((WireBlock) this.getPipeBlock()).getRealProps();
	}

	@Override
	public ResourceLocation getNodeType() {
		return EnergyNet.TYPE;
	}

	@Override
	public boolean canConnectTo(final Level level, final BlockPos pos, final Direction side) {
		return NCCapabilities.getEnergyHandler(level, pos.relative(side), side.getOpposite()) != null;
	}

	@Override
	public void handleEnergyTransferred(final long amps, final long volts) {
		this.counter.increment(this.getLevel(), amps);
	}

	@Override
	public long getAmpsTransferred() {
		return this.counter.get(this.getLevel());
	}

	@Nullable
	public IEnergyHandler getEnergyHandler(@Nullable final Direction side) {
		if (side == null || PipeNetHelper.isBlocked(this.getConnections(), side)) {
			return null;
		}
		if (this.getLevel() instanceof final ServerLevel serverLevel) {
			return new EnergyNetHandler(serverLevel, this);
		}
		return IEnergyHandler.DEFAULT;
	}
}
