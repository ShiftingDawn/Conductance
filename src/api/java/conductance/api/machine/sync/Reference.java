package conductance.api.machine.sync;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;

public interface Reference {

	void markDirty();

	void markPersistenceDirty();

	void markSyncDirty();

	void setPersistenceStateCallback(BooleanConsumer persistenceStateCallback);

	void setSyncStateCallback(BooleanConsumer syncStateCallback);

	ReferenceKey getKey();

	Holder getValueHolder();
}
