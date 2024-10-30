package conductance.api.machine.data;

import java.lang.reflect.Field;
import net.minecraft.nbt.Tag;

public interface ManagedFieldValueHandler<T> {

	Class<T> getFieldType();

	T getValue(Field field, Object instance);

	boolean equals(T value1, T value2);

	Tag serialize(T value);

	T deserialize(Tag nbt);
}
