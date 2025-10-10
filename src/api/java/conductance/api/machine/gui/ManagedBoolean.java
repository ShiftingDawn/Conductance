package conductance.api.machine.gui;

import java.util.function.BooleanSupplier;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class ManagedBoolean implements BooleanConsumer, BooleanSupplier {

	private final @Nullable BooleanConsumer setter;
	private final @Nullable BooleanSupplier getter;

	public ManagedBoolean(final boolean value) {
		final BooleanHolder holder = new BooleanHolder();
		holder.setValue(value);
		this.setter = holder::setValue;
		this.getter = holder::getValue;
	}

	@Override
	public void accept(final boolean value) {
		if (this.setter != null) {
			this.setter.accept(value);
		}
	}

	@Override
	public boolean getAsBoolean() {
		return this.getter != null && this.getter.getAsBoolean();
	}

	@Override
	public String toString() {
		return String.valueOf(this.getAsBoolean());
	}
}
