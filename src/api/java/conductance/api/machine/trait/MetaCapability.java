package conductance.api.machine.trait;

import nl.appelgebakje22.xdata.ManagedDataMap;
import nl.appelgebakje22.xdata.api.IManaged;

public abstract class MetaCapability implements IManaged {

	private final ManagedDataMap managedDataMap = new ManagedDataMap(this);

	public void onLoad() {
	}

	public void onUnload() {
	}

	@Override
	public final ManagedDataMap getDataMap() {
		return this.managedDataMap;
	}
}
