package conductance.api.util;

import java.util.function.IntSupplier;
import net.minecraft.Util;

public final class LazyInt implements IntSupplier {

	private final IntSupplier supplier;
	private volatile int value;
	private volatile boolean fetched = false;

	private LazyInt(final IntSupplier supplier) {
		this.supplier = supplier;
	}

	@Override
	public int getAsInt() {
		if (!this.fetched) {
			synchronized (this) {
				if (!this.fetched) {
					this.value = this.supplier.getAsInt();
					this.fetched = true;
				}
			}
		}
		return this.value;
	}

	public static LazyInt of(final IntSupplier supplier) {
		return new LazyInt(supplier);
	}

	@SuppressWarnings("DataFlowIssue")
	public static LazyInt of(final int value) {
		return Util.make(new LazyInt(null), lazy -> {
			lazy.value = value;
			lazy.fetched = true;
		});
	}
}
