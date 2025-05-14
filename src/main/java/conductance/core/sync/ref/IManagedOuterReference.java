package conductance.core.sync.ref;

import conductance.api.CAPI;
import conductance.api.machine.sync.Holder;
import conductance.api.machine.sync.IManaged;
import conductance.api.machine.sync.ManagedDataMap;
import conductance.api.machine.sync.ReferenceKey;
import conductance.core.sync.ManagedDataMapImpl;

public class IManagedOuterReference extends ReferenceImpl {

	IManagedOuterReference(final ReferenceKey key, final Holder valueHolder) {
		super(key, valueHolder);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.getValueHolder().get() instanceof final IManaged managed) {
			if (managed.getDataMap() instanceof final ManagedDataMapImpl map) {
				map.tick();
				if (map.hasDirtyPersistentFields()) {
					this.markPersistenceDirty();
				}
				if (map.hasDirtySyncFields()) {
					this.markSyncDirty();
				}
			} else {
				throw new IllegalStateException("%s at %s returned an invalid %s! Use %s::syncHelper()::requestDataMap(%s)!".formatted(
						IManaged.class.getName(), this.getKey().getRawField(), ManagedDataMap.class.getName(), CAPI.class.getName(), IManaged.class.getName()
				));
			}
		} else {
			throw new NullPointerException("Encountered null %s at %s!".formatted(IManaged.class.getName(), this.getKey().getRawField()));
		}
	}
}
