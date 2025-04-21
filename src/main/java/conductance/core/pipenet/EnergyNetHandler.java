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
import conductance.api.material.traits.MaterialTraitCable;
import conductance.Conductance;

public class EnergyNetHandler implements IEnergyHandler {

	private final ServerLevel level;
	private final INetworkNode<CableData> cable;

	public EnergyNetHandler(final ServerLevel serverLevel, final INetworkNode<CableData> cable) {
		this.level = serverLevel;
		this.cable = cable;
	}

	@Override
	public long receiveEnergy(@Nullable final Direction receivingSide, final long volts, final long amps) {
		long ampsUsed = 0L;
		final Set<BlockPos> burnedCables = new HashSet<>();
		final Set<NetworkPath<CableData>> paths = this.cable.getNetwork(this.level).getPaths(this.cable);
		if (paths == null || paths.isEmpty()) {
			return ampsUsed;
		}
		for (final NetworkPath<CableData> path : paths) {
			final EnergyPathData data = (EnergyPathData) path.getData();
			assert data != null;
			if (data.totalLoss() >= volts) {
				continue;
			}
			if (Objects.equals(this.cable.getBlockPos(), path.getDest()) && receivingSide == path.getSide()) {
				continue;
			}
			final IEnergyHandler destination = CapabilityHelper.getEnergyHandler(this.level, path.getDest().relative(path.getSide()), path.getSide().getOpposite());
			final Direction destinationSide = path.getSide();
			if (destination == null || !destination.canReceiveEnergy(destinationSide) || destination.getEnergySpace() <= 0) {
				continue;
			}
			final long energyLeftOver = volts - data.totalLoss();
			if (energyLeftOver <= 0) {
				continue;
			}
//			for (final Pair<BlockPos, CableData> pair : path.path()) {
//				final MaterialTypeCable cable = pair.getSecond().properties();
//				if (cable.tier.getVoltage() < voltage) {
//					final int heat = (int) (Math.log(CAPI.TIERS.getTierByVoltage(voltage).getIndex() - CAPI.TIERS.getTierByVoltage(cable.getVoltage()).getIndex()) * 45 + 36.5);
//					if (this.energyNet.applyHeat(pair.getFirst(), heat)) {
//						burnedCables.add(pair.getFirst());
//					}
//					energyLeftOver = Math.min(cable.getVoltage(), energyLeftOver);
//				}
//			}
			if (!burnedCables.isEmpty()) {
				break;
			}
			final long ampsAccepted = destination.receiveEnergy(destinationSide, energyLeftOver, amps - ampsUsed);
			if (ampsAccepted == 0) {
				continue;
			}
			ampsUsed += ampsAccepted;
			long voltageTraveled = volts;
			for (final INetworkNode<CableData> pathNode : path.getPath()) {
				final MaterialTraitCable pathCableData = pathNode.getData().properties();
				voltageTraveled -= pathCableData.getCableLoss();
				if (voltageTraveled <= 0) {
					break;
				}
//				if (this.energyNet.incrementAmperage(pathNode.getFirst(), ampsAccepted, pathCableData.amperage)) {
//					burnedCables.add(pathNode.getFirst());
//				}
			}
			if (!burnedCables.isEmpty() || amps == ampsUsed) {
				break;
			}
		}
//		for (final BlockPos pos : burnedCables) {
//			this.burnCable(this.energyNet.getLevel(), pos);
//		}
		return ampsUsed;
	}

	private void burnCable(final ServerLevel serverLevel, final BlockPos pos) {
		serverLevel.setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
	}

	@Override
	public long getInputAmperage() {
		return this.cable.getData().properties().getAmperage();
	}

	@Override
	public long getInputVoltage() {
		return this.cable.getData().properties().getTier().getVoltage();
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
