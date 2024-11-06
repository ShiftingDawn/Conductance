package nl.appelgebakje22.xdata.handlers;

import java.util.function.Supplier;
import nl.appelgebakje22.xdata.Operation;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;

public class SimpleObjectHandler implements ReferenceHandler {

	private final Class<?> typeClass;
	private final boolean shallowEqualityCheck;
	private final Supplier<? extends Serializer<?>> serializerFactory;

	public SimpleObjectHandler(final Class<?> typeClass, final boolean shallowEqualityCheck, final Supplier<? extends Serializer<?>> serializerFactory) {
		this.typeClass = typeClass;
		this.shallowEqualityCheck = shallowEqualityCheck;
		this.serializerFactory = serializerFactory;
	}

	@Override
	public boolean canHandle(final Class<?> clazz) {
		return this.shallowEqualityCheck ? clazz.equals(this.typeClass) : this.typeClass.isAssignableFrom(clazz);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public Serializer<?> readFromReference(final Operation operation, final Reference ref) {
		return XData.make(this.serializerFactory.get(), serializer -> ((Serializer) serializer).setData(ref.getValueHolder().get()));
	}

	@Override
	public void writeToReference(final Operation operation, final Reference ref, final Serializer<?> serializer) {
		ref.getValueHolder().set(serializer.getData());
	}
}
