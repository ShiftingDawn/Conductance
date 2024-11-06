package nl.appelgebakje22.xdata.ref;

import lombok.RequiredArgsConstructor;
import nl.appelgebakje22.xdata.api.Holder;
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
