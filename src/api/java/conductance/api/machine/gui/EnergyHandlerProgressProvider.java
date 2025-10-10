package conductance.api.machine.gui;

import lombok.RequiredArgsConstructor;
import conductance.api.machine.energy.IEnergyHandler;

@RequiredArgsConstructor
public final class EnergyHandlerProgressProvider implements ProgressProvider {

	private final IEnergyHandler handler;

	@Override
	public long getMaxProgress() {
		return this.handler.getEnergyCapacity();
	}

	@Override
	public long getCurrentProgress() {
		return this.handler.getEnergyStored();
	}
}
