package conductance.core.data;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.Util;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public final class ManagedClassWrapper {

	@Getter
	private final Class<?> clazz;
	@Getter
	private final ManagedFieldWrapper[] fields;
	private final Map<String, ManagedFieldWrapper> fieldByName;
	private final Map<String, ManagedFieldWrapper> fieldByPersistenceKey;
	@Getter
	@Nullable
	private final ManagedClassWrapper parentWrapper;

	ManagedClassWrapper(final Class<?> clazz, @Nullable final ManagedClassWrapper parentWrapper) {
		this.clazz = clazz;
		this.fields = ManagedClassWrapper.findFields(clazz);
		this.fieldByName = Collections.unmodifiableMap(Util.make(new HashMap<>(), map -> Arrays.stream(this.fields).forEach(field -> map.put(field.getName(), field))));
		this.fieldByPersistenceKey = Collections.unmodifiableMap(Util.make(new HashMap<>(), map ->
				Arrays.stream(this.fields).filter(field -> field.getPersistenceKey() != null).forEach(field -> map.put(field.getPersistenceKey(), field))
		));
		this.parentWrapper = parentWrapper;
	}

	@Nullable
	public ManagedFieldWrapper getByName(final String persistenceKey) {
		return this.fieldByName.get(persistenceKey);
	}

	@Nullable
	public ManagedFieldWrapper getByPersistenceKey(final String persistenceKey) {
		return this.fieldByPersistenceKey.get(persistenceKey);
	}

	private static ManagedFieldWrapper[] findFields(final Class<?> clazz) {
		final List<ManagedFieldWrapper> list = new ArrayList<>();
		for (final Field field : clazz.getDeclaredFields()) {
			if (Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			final ManagedFieldWrapper fieldWrapper = ManagedFieldWrapper.create(field);
			if (fieldWrapper != null) {
				field.setAccessible(true);
				list.add(fieldWrapper);
			}
		}
		return list.toArray(ManagedFieldWrapper[]::new);
	}
}
