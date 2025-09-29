package conductance.api.util;

import java.util.function.LongSupplier;
import net.minecraft.Util;

public final class LazyLong implements LongSupplier {

	private final LongSupplier supplier;
	private volatile long value;
	private volatile boolean fetched = false;

	private LazyLong(final LongSupplier supplier) {
		this.supplier = supplier;
	}

	@Override
	public long getAsLong() {
		if (!this.fetched) {
			synchronized (this) {
				if (!this.fetched) {
					this.value = this.supplier.getAsLong();
					this.fetched = true;
				}
			}
		}
		return this.value;
	}

	public static LazyLong of(final LongSupplier supplier) {
		return new LazyLong(supplier);
	}

	@SuppressWarnings("DataFlowIssue")
	public static LazyLong of(final long value) {
		return Util.make(new LazyLong(null), lazy -> {
			lazy.value = value;
			lazy.fetched = true;
		});
	}
}
