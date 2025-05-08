package conductance.core.pipenet;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import conductance.api.capability.CapabilityHelper;
import conductance.api.capability.energy.IEnergyHandler;
import conductance.Conductance;

public class EnergyNetHandler implements IEnergyHandler {

	private final ServerLevel level;
	private final ICableNode cable;

	public EnergyNetHandler(final ServerLevel serverLevel, final ICableNode cable) {
		this.level = serverLevel;
		this.cable = cable;
	}

	@Override
	public long receiveEnergy(@Nullable final Direction receivingSide, final long volts, final long amps) {
		long ampsUsed = 0L;
		final Set<BlockPos> burnedCables = new HashSet<>();
		final Set<NetworkPath<ICableNode, CableData>> paths = this.cable.getNetwork(this.level).getPaths(this.cable);
		if (paths == null || paths.isEmpty()) {
			return ampsUsed;
		}
		for (final NetworkPath<ICableNode, CableData> path : paths) {
			final EnergyPathData data = (EnergyPathData) path.getData();
			assert data != null;
			if (Objects.equals(this.cable.getBlockPos(), path.getDest()) && receivingSide == path.getSide()) {
				continue;
			}
			final IEnergyHandler destination = CapabilityHelper.getEnergyHandler(this.level, path.getDest().relative(path.getSide()), path.getSide().getOpposite());
			final Direction destinationSide = path.getSide();
			if (destination == null || !destination.canReceiveEnergy(destinationSide) || destination.getEnergySpace() <= 0) {
				continue;
			}
			long energyLeftOver = volts;
			if (energyLeftOver <= 0) {
				continue;
			}
			for (final ICableNode pathNode : path.getPath()) {
				final CableData pathNodeData = pathNode.getData();
				if (pathNodeData.voltage() < volts) {
					burnedCables.add(pathNode.getBlockPos());
					energyLeftOver = Math.min(pathNodeData.voltage(), energyLeftOver);
				}
			}
			if (!burnedCables.isEmpty()) {
				break;
			}
			final long ampsAccepted = destination.receiveEnergy(destinationSide, energyLeftOver, amps - ampsUsed);
			if (ampsAccepted == 0) {
				continue;
			}
			ampsUsed += ampsAccepted;
			for (final ICableNode pathNode : path.getPath()) {
				final CableData pathNodeData = pathNode.getData();
				pathNode.handleEnergyTransferred(ampsAccepted, volts);
				if (pathNode.getAmpsTransferred() > pathNodeData.amperage()) {
					burnedCables.add(pathNode.getBlockPos());
				}
			}
			if (!burnedCables.isEmpty() || amps == ampsUsed) {
				break;
			}
		}
		for (final BlockPos pos : burnedCables) {
			this.level.setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
		}
		return ampsUsed;
	}

	@Override
	public long getInputAmperage() {
		return this.cable.getData().amperage();
	}

	@Override
	public long getInputVoltage() {
		return this.cable.getData().voltage();
	}

	@Override
	public long getEnergyCapacity() {
		return this.getInputVoltage() * this.getInputAmperage();
	}

	@Override
	@Deprecated
	public long modifyEnergy(final long differenceAmount) {
		Conductance.LOGGER.warn("Do not use modifyEnergy() for cables! Use receiveEnergy()");
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
