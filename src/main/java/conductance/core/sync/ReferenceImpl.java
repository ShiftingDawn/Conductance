package conductance.core.sync;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.sync.Holder;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.ReferenceKey;

class ReferenceImpl implements Reference {

	@Getter
	private final ReferenceKey key;
	@Getter
	private final Holder valueHolder;
	@Getter
	private boolean needsPersist = false;
	@Getter
	private boolean needsSync = false;
	@Setter
	@Nullable
	private BooleanConsumer persistenceStateCallback = null;
	@Setter
	@Nullable
	private BooleanConsumer syncStateCallback = null;

	ReferenceImpl(final ReferenceKey key, final Holder valueHolder) {
		this.key = key;
		this.valueHolder = valueHolder;
	}

	@Override
	public void tick() {
	}

	@Override
	public void markDirty() {
		this.markPersistenceDirty();
		this.markSyncDirty();
	}

	@Override
	public void markPersistenceDirty() {
		if (this.key.isPersisted() && !this.needsPersist) {
			this.needsPersist = true;
			if (this.persistenceStateCallback != null) {
				this.persistenceStateCallback.accept(true);
			}
		}
	}

	@Override
	public void markSyncDirty() {
		if (this.key.isSynchronized() && !this.needsSync) {
			this.needsSync = true;
			if (this.syncStateCallback != null) {
				this.syncStateCallback.accept(true);
			}
		}
	}

	@Override
	public void clearPersistenceMark() {
		if (this.needsPersist) {
			this.needsPersist = false;
			if (this.persistenceStateCallback != null) {
				this.persistenceStateCallback.accept(false);
			}
		}
	}

	@Override
	public void clearSyncMark() {
		if (this.needsSync) {
			this.needsSync = false;
			if (this.syncStateCallback != null) {
				this.syncStateCallback.accept(false);
			}
		}
	}

	static final class PrimitiveReference extends ReferenceImpl {

		private Object lastValue;

		PrimitiveReference(final ReferenceKey key, final Holder valueHolder) {
			super(key, valueHolder);
			this.lastValue = valueHolder.get();
		}

		@Override
		public void tick() {
			final Object newValue = this.getValueHolder().get();
			if (newValue != this.lastValue) {
				this.lastValue = newValue;
				this.markDirty();
			}
		}
	}
}
