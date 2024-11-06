package nl.appelgebakje22.xdata.ref;

import java.util.Collection;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import lombok.Getter;
import lombok.Setter;
import nl.appelgebakje22.xdata.api.Holder;
import nl.appelgebakje22.xdata.api.IManaged;
import org.jetbrains.annotations.Nullable;

public class Reference {

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

	Reference(final ReferenceKey key, final Holder valueHolder) {
		this.key = key;
		this.valueHolder = valueHolder;
	}

	public void tick() {
	}

	public void markDirty() {
		this.markPersistenceDirty();
		this.markSyncDirty();
	}

	public void markPersistenceDirty() {
		if (this.key.isPersisted() && !this.needsPersist) {
			this.needsPersist = true;
			if (this.persistenceStateCallback != null) {
				this.persistenceStateCallback.accept(true);
			}
		}
	}

	public void markSyncDirty() {
		if (this.key.isSynchronized() && !this.needsSync) {
			this.needsSync = true;
			if (this.syncStateCallback != null) {
				this.syncStateCallback.accept(true);
			}
		}
	}

	public void clearPersistenceMark() {
		if (this.needsPersist) {
			this.needsPersist = false;
			if (this.persistenceStateCallback != null) {
				this.persistenceStateCallback.accept(false);
			}
		}
	}

	public void clearSyncMark() {
		if (this.needsSync) {
			this.needsSync = false;
			if (this.syncStateCallback != null) {
				this.syncStateCallback.accept(false);
			}
		}
	}

	public static Reference of(final ReferenceKey referenceKey, final Holder valueHolder) {
		if (valueHolder instanceof final ReflectionHolder reflectionHolder && reflectionHolder.isPrimitive()) {
			return new PrimitiveReference(referenceKey, valueHolder);
		} else if (referenceKey.getRawField().getType().isArray() || Collection.class.isAssignableFrom(referenceKey.getRawField().getType())) {
			return new GenericArrayOuterReference(referenceKey, valueHolder);
		} else if (IManaged.class.isAssignableFrom(referenceKey.getRawField().getType())) {
			return new IManagedOuterReference(referenceKey, valueHolder);
		}
		return new Reference(referenceKey, valueHolder);
	}

	private static class PrimitiveReference extends Reference {

		private Object lastValue;

		public PrimitiveReference(final ReferenceKey key, final Holder valueHolder) {
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
