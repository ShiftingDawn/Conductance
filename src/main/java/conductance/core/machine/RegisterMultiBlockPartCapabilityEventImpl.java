package conductance.core.machine;

import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import conductance.api.machine.event.RegisterMultiBlockPartCapabilityEvent;
import conductance.api.machine.multi.MultiBlockPartCapability;

@RequiredArgsConstructor
final class RegisterMultiBlockPartCapabilityEventImpl implements RegisterMultiBlockPartCapabilityEvent {

	private final Function<String, MultiBlockPartCapability> delegate;

	@Override
	public MultiBlockPartCapability register(final String registryName) {
		return this.delegate.apply(registryName);
	}
}
