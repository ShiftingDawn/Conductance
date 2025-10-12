package conductance.lib.pipenet;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCCapabilities;
import conductance.api.machine.energy.IEnergyHandler;
import conductance.Conductance;

public class EnergyNetHandler implements IEnergyHandler {

	private final ServerLevel level;
	private final IWireNode wire;

	public EnergyNetHandler(final ServerLevel serverLevel, final IWireNode wire) {
		this.level = serverLevel;
		this.wire = wire;
	}

	@Override
	public long receiveEnergy(@Nullable final Direction receivingSide, final long volts, final long amps) {
		long ampsUsed = 0L;
		final Set<BlockPos> burnedWires = new HashSet<>();
		final Set<NetworkPath<IWireNode, WireData>> paths = this.wire.getNetwork(this.level).getPaths(this.wire);
		if (paths == null || paths.isEmpty()) {
			return ampsUsed;
		}
		for (final NetworkPath<IWireNode, WireData> path : paths) {
			final EnergyPathData data = (EnergyPathData) path.getData();
			assert data != null;
			if (Objects.equals(this.wire.getBlockPos(), path.getDest()) && receivingSide == path.getSide()) {
				continue;
			}
			final IEnergyHandler destination = NCCapabilities.getEnergyHandler(this.level, path.getDest().relative(path.getSide()), path.getSide().getOpposite());
			final Direction destinationSide = path.getSide();
			if (destination == null || !destination.canReceiveEnergy(destinationSide) || destination.getEnergySpace() <= 0) {
				continue;
			}
			long energyLeftOver = volts;
			if (energyLeftOver <= 0) {
				continue;
			}
			for (final IWireNode pathNode : path.getPath()) {
				final WireData pathNodeData = pathNode.getData();
				if (pathNodeData.voltage() < volts) {
					burnedWires.add(pathNode.getBlockPos());
					energyLeftOver = Math.min(pathNodeData.voltage(), energyLeftOver);
				}
			}
			if (!burnedWires.isEmpty()) {
				break;
			}
			final long ampsAccepted = destination.receiveEnergy(destinationSide, energyLeftOver, amps - ampsUsed);
			if (ampsAccepted == 0) {
				continue;
			}
			ampsUsed += ampsAccepted;
			for (final IWireNode pathNode : path.getPath()) {
				final WireData pathNodeData = pathNode.getData();
				pathNode.handleEnergyTransferred(ampsAccepted, volts);
				if (pathNode.getAmpsTransferred() > pathNodeData.amperage()) {
					burnedWires.add(pathNode.getBlockPos());
				}
			}
			if (!burnedWires.isEmpty() || amps == ampsUsed) {
				break;
			}
		}
		for (final BlockPos pos : burnedWires) {
			this.level.setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
		}
		return ampsUsed;
	}

	@Override
	public long getInputAmperage() {
		return this.wire.getData().amperage();
	}

	@Override
	public long getInputVoltage() {
		return this.wire.getData().voltage();
	}

	@Override
	public long getEnergyCapacity() {
		return this.getInputVoltage() * this.getInputAmperage();
	}

	@Override
	@Deprecated
	public long modifyEnergy(final long differenceAmount) {
		Conductance.LOGGER.warn("Do not use modifyEnergy() for wires! Use receiveEnergy()");
		return this.receiveEnergy(null, differenceAmount / this.getInputAmperage(), differenceAmount / this.getInputVoltage()) * this.getInputVoltage();
	}

	@Override
	public boolean canReceiveEnergy(@Nullable final Direction receivingSide) {
		return true;
	}

	@Override
	public boolean canExtractEnergy(@Nullable final Direction extractingSide) {
		return true;
	}

	@Override
	public long getEnergyStored() {
		return 0;
	}
}
