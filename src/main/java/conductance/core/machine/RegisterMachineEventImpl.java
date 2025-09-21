package conductance.core.machine;

import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineType;
import conductance.api.machine.event.MachineBlockEntityFactory;
import conductance.api.machine.event.MachineBuilder;
import conductance.api.machine.event.RegisterMachineEvent;

@RequiredArgsConstructor
final class RegisterMachineEventImpl implements RegisterMachineEvent {

	interface Delegate {
		<T extends MachineBlockEntity<T>> MachineType<T> apply(String registryName, MachineBlockEntityFactory<T> blockEntityFactory, Consumer<MachineBuilder<T>> builder);
	}

	private final Delegate delegate;

	@Override
	public <T extends MachineBlockEntity<T>> MachineType<T> register(final String registryName, final MachineBlockEntityFactory<T> blockEntityFactory, final Consumer<MachineBuilder<T>> builder) {
		return this.delegate.apply(registryName, blockEntityFactory, builder);
	}
}
