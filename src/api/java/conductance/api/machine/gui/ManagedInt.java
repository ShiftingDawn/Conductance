package conductance.api.machine.gui;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class ManagedInt implements IntConsumer, IntSupplier {

	private final @Nullable IntConsumer setter;
	private final @Nullable IntSupplier getter;

	public ManagedInt(final int value) {
		final IntHolder holder = new IntHolder();
		holder.setValue(value);
		this.setter = holder::setValue;
		this.getter = holder::getValue;
	}

	public int add(final int toAdd) {
		return this.getAsInt() + toAdd;
	}

	public int subtract(final int toSubtract) {
		return this.getAsInt() - toSubtract;
	}

	@Override
	public void accept(final int value) {
		if (this.setter != null) {
			this.setter.accept(value);
		}
	}

	@Override
	public int getAsInt() {
		return this.getter != null ? this.getter.getAsInt() : 0;
	}

	@Override
	public String toString() {
		return String.valueOf(this.getAsInt());
	}
}
