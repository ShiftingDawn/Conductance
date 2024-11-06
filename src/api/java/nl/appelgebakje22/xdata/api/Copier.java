package nl.appelgebakje22.xdata.api;

public interface Copier<T> {

	boolean canHandle(Class<?> clazz);

	T copy(T value);
}
