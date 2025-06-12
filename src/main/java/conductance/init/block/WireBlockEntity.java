package conductance.init.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCCapabilities;
import conductance.api.energy.IEnergyHandler;
import conductance.api.util.world.InteractType;
import conductance.lib.pipenet.WireData;
import conductance.lib.pipenet.EnergyNet;
import conductance.lib.pipenet.EnergyNetHandler;
import conductance.lib.pipenet.IWireNode;
import conductance.lib.pipenet.LevelEnergyNet;
import conductance.lib.pipenet.PerTickLongHandler;
import conductance.lib.pipenet.PipeNetHelper;

public class WireBlockEntity extends PipeBlockEntity<IWireNode, WireData, LevelEnergyNet> implements IWireNode {

	private final PerTickLongHandler counter = new PerTickLongHandler(0L);

	public WireBlockEntity(final BlockEntityType<?> type, final BlockPos pos, final BlockState state) {
		super(type, pos, state);
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
