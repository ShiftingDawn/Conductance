package conductance.api.machine.xdata;

import net.minecraft.core.Registry;
import lombok.RequiredArgsConstructor;
import nl.appelgebakje22.xdata.Operation;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;

@RequiredArgsConstructor
public final class BuiltInRegistryHandler<T> implements ReferenceHandler {

	private final Class<T> typeClass;
	private final Registry<T> registry;

	@Override
	public boolean canHandle(final Class<?> clazz) {
		return this.typeClass.isAssignableFrom(clazz);
	}

	@Override
	public Serializer<?> readFromReference(final Operation operation, final Reference ref) {
		final T data = (T) ref.getValueHolder().get();
		return ResourceLocationSerializer.of(this.registry.getKey(data));
	}

	@Override
	public void writeToReference(final Operation operation, final Reference ref, final Serializer<?> serializer) {
		final ResourceLocationSerializer resourceLocationSerializer = this.testSerializer(serializer, ResourceLocationSerializer.class);
		ref.getValueHolder().set(this.registry.get(resourceLocationSerializer.getData()));
	}
}
