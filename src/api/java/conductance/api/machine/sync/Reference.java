package conductance.api.machine.sync;

public interface Reference {

	void markDirty();

	void markPersistenceDirty();

	void markSyncDirty();

	void setPersistenceStateCallback(it.unimi.dsi.fastutil.booleans.BooleanConsumer persistenceStateCallback);

	void setSyncStateCallback(it.unimi.dsi.fastutil.booleans.BooleanConsumer syncStateCallback);

	ReferenceKey getKey();

	Holder getValueHolder();
}
