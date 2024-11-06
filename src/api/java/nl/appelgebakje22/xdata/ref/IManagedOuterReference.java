package nl.appelgebakje22.xdata.ref;

import nl.appelgebakje22.xdata.ManagedDataMap;
import nl.appelgebakje22.xdata.api.Holder;
import nl.appelgebakje22.xdata.api.IManaged;

public class IManagedOuterReference extends Reference {

	IManagedOuterReference(final ReferenceKey key, final Holder valueHolder) {
		super(key, valueHolder);
	}

	@Override
	public void tick() {
		super.tick();
		final ManagedDataMap map = ((IManaged) this.getValueHolder().get()).getDataMap();
		map.tick();
		if (map.hasDirtyPersistentFields()) {
			this.markPersistenceDirty();
		}
		if (map.hasDirtySyncFields()) {
			this.markSyncDirty();
		}
	}
}
