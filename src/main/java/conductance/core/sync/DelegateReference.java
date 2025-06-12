package conductance.core.sync;

import conductance.api.sync.Holder;
import conductance.api.sync.ReferenceKey;

final class DelegateReference extends ReferenceImpl {

	DelegateReference(final ReferenceKey key, final Holder valueHolder) {
		super(key, valueHolder);
	}

	@Override
	public void tick() {
		throw new IllegalStateException("This should not be called, I think");
		//check array contents maybe
	}
}
