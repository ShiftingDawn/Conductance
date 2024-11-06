package nl.appelgebakje22.xdata.ref;

public class ArrayReference extends Reference {

	private ArrayReference(final ReferenceKey key, final Object array, final int index, final Class<?> arrayType) {
		super(key, ArrayHolder.of(array, index, arrayType));
	}

	@Override
	public void tick() {
		//TODO check array contents
	}

	public static ArrayReference of(final ReferenceKey key, final Object array, final int index, final Class<?> arrayType) {
		return new ArrayReference(key, array, index, arrayType);
	}
}
