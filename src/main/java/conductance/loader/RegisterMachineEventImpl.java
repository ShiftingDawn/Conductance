package conductance.loader;

import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineBlockEntityFactory;
import conductance.api.machine.MachineBuilder;
import conductance.api.machine.MachineType;
import conductance.api.plugin.RegisterMachineEvent;

@AllArgsConstructor
final class RegisterMachineEventImpl implements RegisterMachineEvent {

	public interface MachineRegister {

		<T extends MachineBlockEntity<T>> MachineBuilder<T> apply(String registryName, MachineBlockEntityFactory<T> constructor);
	}

	private final MachineRegister delegate;

	@Override
	public <T extends MachineBlockEntity<T>> MachineType<T> register(final String registryName, final MachineBlockEntityFactory<T> constructor, final Consumer<MachineBuilder<T>> builder) {
		//TODO refactor
		final MachineBuilder<T> b = this.delegate.apply(registryName, constructor);
		builder.accept(b);
		return b.build();
	}
}
