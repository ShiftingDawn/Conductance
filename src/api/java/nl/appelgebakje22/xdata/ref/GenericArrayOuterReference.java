package nl.appelgebakje22.xdata.ref;

import java.lang.reflect.Array;
import java.util.Collection;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.XDataRegister;
import nl.appelgebakje22.xdata.api.Copier;
import nl.appelgebakje22.xdata.api.Holder;

public class GenericArrayOuterReference extends Reference {

	private final IntSet dirtyElements = new IntOpenHashSet();
	private final boolean isArray;
	private Object lastValue;
	private int lastLength;

	GenericArrayOuterReference(final ReferenceKey key, final Holder valueHolder) {
		super(key, valueHolder);
		this.isArray = key.getRawField().getType().isArray();
		final Object currentValue = valueHolder.get();
		if (currentValue != null) {
			this.lastValue = GenericArrayOuterReference.cloneGenericArray(currentValue, this.isArray);
			this.lastLength = Array.getLength(this.lastLength);
		}
	}

	@Override
	public void tick() {
		final Object currentValue = this.getValueHolder().get();
		if ((currentValue == null && this.lastValue != null) || (currentValue != null && this.lastValue == null) || (this.lastValue != null && this.hasChanged(currentValue))) {
			if (currentValue != null) {
				this.lastValue = GenericArrayOuterReference.cloneGenericArray(currentValue, this.isArray);
				this.lastLength = Array.getLength(this.lastValue);
			} else {
				this.lastValue = null;
				this.lastLength = 0;
			}
			this.markDirty();
		}
	}

	@SuppressWarnings("rawtypes")
	protected boolean hasChanged(final Object newValue) {
		if (this.isArray) {
			if (Array.getLength(newValue) != this.lastLength) {
				this.markDirty();
				this.dirtyElements.clear();
				return true;
			}
			boolean hasChanged = false;
			for (int i = 0; i < this.lastLength; ++i) {
				if (!XData.isEqual(Array.get(this.lastValue, i), Array.get(newValue, i))) {
					this.markDirty();
					this.dirtyElements.add(i);
					hasChanged = true;
				}
			}
			return hasChanged;
		}
		final Collection c = (Collection) newValue;
		if (c.size() != this.lastLength) {
			this.markDirty();
			this.dirtyElements.clear();
			return true;
		}
		int i = 0;
		boolean hasChanged = false;
		for (final Object item : c) {
			if (!XData.isEqual(Array.get(this.lastValue, i++), item)) {
				this.markDirty();
				this.dirtyElements.add(i);
				hasChanged = true;
			}
		}
		return hasChanged;
	}

	@SuppressWarnings({"SuspiciousSystemArraycopy", "rawtypes", "unchecked"})
	private static Object cloneGenericArray(final Object array, final boolean isArray) {
		if (isArray) {
			final Class<?> componentType = array.getClass().getComponentType();
			if (componentType.isPrimitive()) {
				final Object newArray = Array.newInstance(componentType, Array.getLength(array));
				System.arraycopy(array, 0, newArray, 0, Array.getLength(newArray));
				return newArray;
			}
			final Object[] newArray = new Object[Array.getLength(array)];
			for (int i = 0; i < newArray.length; ++i) {
				final Object item = Array.get(array, i);
				if (item != null) {
					final Copier copier = XDataRegister.getCopier(item.getClass());
					newArray[i] = copier != null ? copier.copy(item) : item;
				}
			}
			return newArray;
		}
		final Collection c = (Collection) array;
		final Object[] result = new Object[c.size()];
		int i = 0;
		for (final Object item : c) {
			if (item != null) {
				final Copier copier = XDataRegister.getCopier(item.getClass());
				result[i++] = copier != null ? copier.copy(item) : item;
			}
		}
		return result;
	}
}
