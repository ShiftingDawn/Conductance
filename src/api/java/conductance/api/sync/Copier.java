package conductance.api.sync;

public interface Copier<T> {

	boolean canHandle(Class<?> clazz);

	T copy(T value);
}
