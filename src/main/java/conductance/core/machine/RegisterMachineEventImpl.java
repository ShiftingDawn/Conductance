package conductance.core.machine;

import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineType;
import conductance.api.machine.event.MachineBlockEntityFactory;
import conductance.api.machine.event.MachineBuilder;
import conductance.api.machine.event.MultiBlockMachineBuilder;
import conductance.api.machine.event.MultiMachineBlockEntityFactory;
import conductance.api.machine.event.RegisterMachineEvent;
import conductance.api.machine.multi.IMultiBlockController;
import conductance.api.machine.multi.MultiMachineBlockEntity;
import conductance.api.machine.multi.MultiMachineType;

@RequiredArgsConstructor
final class RegisterMachineEventImpl implements RegisterMachineEvent {

	interface Delegate {
		<T extends MachineBlockEntity<T>> MachineType<T> simple(String registryName, MachineBlockEntityFactory<T> blockEntityFactory, Consumer<MachineBuilder<T>> builder);

		<T extends MultiMachineBlockEntity<T> & IMultiBlockController<T>> MultiMachineType<T> multi(String registryName, MultiMachineBlockEntityFactory<T> blockEntityFactory,
		                                                                                            Consumer<MultiBlockMachineBuilder<T>> builder);
	}

	private final Delegate delegate;

	@Override
	public <T extends MachineBlockEntity<T>> MachineType<T> register(final String registryName, final MachineBlockEntityFactory<T> blockEntityFactory, final Consumer<MachineBuilder<T>> builder) {
		return this.delegate.simple(registryName, blockEntityFactory, builder);
	}

	@Override
	public <T extends MultiMachineBlockEntity<T> & IMultiBlockController<T>> MultiMachineType<T> multi(
		final String registryName, final MultiMachineBlockEntityFactory<T> blockEntityFactory, final Consumer<MultiBlockMachineBuilder<T>> builder
	) {
		return this.delegate.multi(registryName, blockEntityFactory, builder);
	}
}
