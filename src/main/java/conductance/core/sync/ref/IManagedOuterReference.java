package conductance.core.sync.ref;

import conductance.api.machine.sync.Holder;
import conductance.api.machine.sync.IManaged;
import conductance.api.machine.sync.ReferenceKey;
import conductance.core.sync.ManagedDataMapImpl;

public class IManagedOuterReference extends ReferenceImpl {

	IManagedOuterReference(final ReferenceKey key, final Holder valueHolder) {
		super(key, valueHolder);
	}

	@Override
	public void tick() {
		super.tick();
		final ManagedDataMapImpl map = (ManagedDataMapImpl) ((IManaged) this.getValueHolder().get()).getDataMap();
		map.tick();
		if (map.hasDirtyPersistentFields()) {
			this.markPersistenceDirty();
		}
		if (map.hasDirtySyncFields()) {
			this.markSyncDirty();
		}
	}
}
