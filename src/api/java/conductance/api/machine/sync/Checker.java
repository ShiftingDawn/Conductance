package conductance.api.machine.sync;

public interface Checker<T> {

	boolean canHandle(Class<?> clazz);

	boolean equals(T value1, T value2);
}
