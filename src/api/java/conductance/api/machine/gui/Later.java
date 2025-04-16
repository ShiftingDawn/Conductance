package conductance.api.machine.gui;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class Later<T> {

	@Nullable
	private T value = null;

	@NotNull
	public T getOrSet(final NonNullSupplier<T> setter) {
		if (this.value == null) {
			this.value = setter.get();
		}
		return this.value;
	}
}
