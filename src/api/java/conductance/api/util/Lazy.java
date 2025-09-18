package conductance.api.util;

import java.util.function.Supplier;
import net.minecraft.Util;

public final class Lazy<T> implements Supplier<T> {

	private final Supplier<T> supplier;
	private volatile T value;
	private volatile boolean fetched = false;

	private Lazy(final Supplier<T> supplier) {
		this.supplier = supplier;
	}

	@Override
	public T get() {
		if (!this.fetched) {
			synchronized (this) {
				if (!this.fetched) {
					this.value = this.supplier.get();
					this.fetched = true;
				}
			}
		}
		return this.value;
	}

	public static <T> Lazy<T> of(final Supplier<T> supplier) {
		return new Lazy<>(supplier);
	}

	@SuppressWarnings("DataFlowIssue")
	public static <T> Lazy<T> of(final T value) {
		return Util.make(new Lazy<>(null), lazy -> {
			lazy.value = value;
			lazy.fetched = true;
		});
	}
}
