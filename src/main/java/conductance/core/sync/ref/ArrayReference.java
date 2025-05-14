package conductance.core.sync.ref;

import conductance.api.machine.sync.ReferenceKey;

public class ArrayReference extends ReferenceImpl {

	private ArrayReference(final ReferenceKey key, final Object array, final int index, final Class<?> arrayType) {
		super(key, ArrayHolder.of(array, index, arrayType));
	}

	@Override
	public void tick() {
		throw new IllegalStateException("This should not be called, I think");
		//check array contents maybe
	}

	public static ArrayReference of(final ReferenceKey key, final Object array, final int index, final Class<?> arrayType) {
		return new ArrayReference(key, array, index, arrayType);
	}
}
