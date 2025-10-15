package conductance.core.coil;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import conductance.api.coil.CoilBlockType;
import conductance.api.coil.event.RegisterCoilBlockTypeEvent;

@RequiredArgsConstructor
final class RegisterCoilBlockTypeEventImpl implements RegisterCoilBlockTypeEvent {

	interface Delegate {
		CoilBlockType apply(String name, int color, @Nullable CoilBlockType previousCoil);
	}

	private final Delegate delegate;

	@Override
	public CoilBlockType register(final String name, final int color, final CoilBlockType previousCoil) {
		if (!(previousCoil instanceof CoilBlockTypeImpl)) {
			throw new IllegalArgumentException("Invalid previousCoil supplied. All coils MUST be created using %s".formatted(RegisterCoilBlockTypeEvent.class.getSimpleName()));
		}
		return this.delegate.apply(name, color, previousCoil);
	}

	@Override
	public CoilBlockType register(final String name, final int color) {
		return this.delegate.apply(name, color, null);
	}
}
