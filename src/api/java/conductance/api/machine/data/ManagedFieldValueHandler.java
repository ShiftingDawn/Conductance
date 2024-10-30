package conductance.api.machine.data;

import java.lang.reflect.Field;
import org.jetbrains.annotations.Nullable;

public interface ManagedFieldValueHandler<T> {

	boolean canHandle(Class<?> type);

	@Nullable
	T readFromField(Field field, Object instance);

	void writeToField(Field field, Object instance, @Nullable T value);

	boolean equals(T value1, T value2);
}
