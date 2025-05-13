package conductance.core.sync.ref;

import java.util.Collection;
import conductance.api.machine.sync.ReferenceKey;

public class CollectionReference extends ReferenceImpl {

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
