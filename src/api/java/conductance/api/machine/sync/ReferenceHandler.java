package conductance.api.machine.sync;

public interface ReferenceHandler {

	boolean canHandle(Class<?> clazz);

	Serializer<?> readFromReference(Operation operation, Reference ref);

	void writeToReference(Operation operation, Reference ref, Serializer<?> serializer);

	default <T extends Serializer<?>> T testSerializer(final Serializer<?> serializer, final Class<T> expectedType) {
		if (expectedType.isAssignableFrom(serializer.getClass())) {
			return expectedType.cast(serializer);
		}
		throw new IllegalArgumentException("Expected %s of type %s, got: %s".formatted(Serializer.class.getName(), expectedType.getName(), serializer.getClass().getName()));
	}
}
