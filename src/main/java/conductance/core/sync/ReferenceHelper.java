package conductance.core.sync;

import java.util.Collection;
import conductance.api.machine.sync.Holder;
import conductance.api.machine.sync.IManaged;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.ReferenceKey;

public final class ReferenceHelper {

	public static Reference of(final ReferenceKey referenceKey, final Holder valueHolder) {
		if (valueHolder instanceof final ReflectionHolder reflectionHolder && reflectionHolder.isPrimitive()) {
			return new ReferenceImpl.PrimitiveReference(referenceKey, valueHolder);
		} else if (referenceKey.getRawField().getType().isArray() || Collection.class.isAssignableFrom(referenceKey.getRawField().getType())) {
			return new ArrayReferenceOuter(referenceKey, valueHolder);
		} else if (IManaged.class.isAssignableFrom(referenceKey.getRawField().getType())) {
			return new ManagedReferenceOuter(referenceKey, valueHolder);
		}
		return new ReferenceImpl(referenceKey, valueHolder);
	}

	public static Reference forArray(final ReferenceKey key, final Object array, final int index, final Class<?> arrayType) {
		return new DelegateReference(key, ArrayHolder.of(array, index, arrayType));
	}

	public static Reference forCollection(final ReferenceKey key, final Collection<?> collection, final int index) {
		return new DelegateReference(key, new CollectionHolder(collection, index));
	}

	private ReferenceHelper() {
	}
}
