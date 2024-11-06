package nl.appelgebakje22.xdata.ref;

import java.util.Collection;

public class CollectionReference extends Reference {

	private CollectionReference(final ReferenceKey key, final Collection<?> collection, final int index) {
		super(key, new CollectionHolder(collection, index));
	}

	@Override
	public void tick() {
		//TODO check array contents
	}

	public static CollectionReference of(final ReferenceKey key, final Collection<?> collection, final int index) {
		return new CollectionReference(key, collection, index);
	}
}
