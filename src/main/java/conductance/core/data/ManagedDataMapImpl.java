package conductance.core.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import lombok.Getter;
import conductance.api.machine.data.ManagedDataMap;
import conductance.Conductance;

public final class ManagedDataMapImpl implements ManagedDataMap {

	@Getter
	private final Class<?> clazz;
	@Getter
	private final ManagedClassWrapper wrapper;
	@Getter
	private final Object instance;
	@Getter
	private final ManagedFieldWrapper[] fields;
	private final Map<ManagedFieldWrapper, InstancedField<?>> fieldInstances;
	private final Map<String, ManagedFieldWrapper> persistenceFields;
	@Getter
	private boolean dirty = false;

	ManagedDataMapImpl(final Class<?> clazz, final ManagedClassWrapper wrapper, final Object instance) {
		this.clazz = clazz;
		this.wrapper = wrapper;
		this.instance = instance;
		this.fields = ManagedDataMapImpl.collectFields(wrapper);
		this.fieldInstances = Collections.unmodifiableMap(Util.make(new IdentityHashMap<>(), map -> Arrays.stream(this.fields).forEach(field -> map.put(field, InstancedField.of(field, instance)))));
		this.persistenceFields = Collections.unmodifiableMap(Util.make(new HashMap<>(), map ->
				Arrays.stream(this.fields).filter(field -> field.getPersistenceKey() != null).forEach(field -> map.put(field.getPersistenceKey(), field))
		));
	}

	public void tick() {
		for (final InstancedField<?> instancedField : this.fieldInstances.values()) {
			instancedField.tick();
			if (instancedField.isDirty()) {
				this.markDirty();
			}
		}
	}

	public void init() {
		this.persistenceFields.values().stream().map(this.fieldInstances::get).forEach(InstancedField::init);
	}

	@Override
	public void saveAllData(final CompoundTag nbt, final HolderLookup.Provider registries) {
		nbt.put("conductance_persist", Util.make(new CompoundTag(), tag -> this.persistenceFields.forEach((fieldName, fieldWrapper) -> {
			final InstancedField<?> instancedField = this.fieldInstances.get(fieldWrapper);
			final Tag serializedTag = instancedField.serialize(registries);
			if (serializedTag != null) {
				tag.put(fieldName, serializedTag);
			}
			instancedField.markNotDirty();
		})));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readAllData(final CompoundTag nbt, final HolderLookup.Provider registries) {
		final CompoundTag tag = nbt.getCompound("conductance_persist");
		tag.getAllKeys().forEach(tagKey -> {
			final ManagedFieldWrapper fieldWrapper = this.persistenceFields.get(tagKey);
			if (fieldWrapper == null) {
				Conductance.LOGGER.warn("No ManagedField found for persistence key {} when loading {}", tagKey, this.instance.getClass().getName());
				return;
			}
			if (tag.contains(tagKey)) {
				final InstancedField<Object> instancedField = (InstancedField<Object>) this.fieldInstances.get(fieldWrapper);
				final Object data = instancedField.deserialize(tag.get(tagKey), registries);
				instancedField.set(data);
				instancedField.markNotDirty();
			}
		});
	}

	public void markDirty() {
		this.dirty = true;
	}

	public void markNotDirty() {
		this.dirty = false;
		this.fieldInstances.values().forEach(InstancedField::markNotDirty);
	}

	private static ManagedFieldWrapper[] collectFields(final ManagedClassWrapper topWrapper) {
		final List<ManagedFieldWrapper> list = new ArrayList<>();
		ManagedClassWrapper currentWrapper = topWrapper;
		while (currentWrapper != null) {
			list.addAll(Arrays.asList(currentWrapper.getFields()));
			currentWrapper = currentWrapper.getParentWrapper();
		}
		return list.toArray(ManagedFieldWrapper[]::new);
	}
}
