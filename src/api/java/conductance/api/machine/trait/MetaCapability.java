package conductance.api.machine.trait;

import com.lowdragmc.lowdraglib.syncdata.IManaged;
import com.lowdragmc.lowdraglib.syncdata.field.FieldManagedStorage;

public abstract class MetaCapability implements IManaged {

	private final FieldManagedStorage syncStorage = new FieldManagedStorage(this);

	public void onLoad() {
	}

	public void onUnload() {
	}

	@Override
	public FieldManagedStorage getSyncStorage() {
		return this.syncStorage;
	}
}
