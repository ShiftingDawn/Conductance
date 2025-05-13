package conductance.core.sync.ref;

import lombok.RequiredArgsConstructor;
import conductance.api.machine.sync.Holder;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class SimpleHolder implements Holder {

	@Nullable
	private Object data;

	@Override
	public @Nullable Object get() {
		return this.data;
	}

	@Override
	public void set(@Nullable final Object object) {
		this.data = object;
	}
}
