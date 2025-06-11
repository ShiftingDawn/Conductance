package conductance.core.sync;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.sync.IManaged;
import conductance.api.machine.sync.ManagedDataMap;
import conductance.api.machine.sync.OnSynchronized;
import conductance.api.machine.sync.Operation;
import conductance.api.machine.sync.Persisted;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.ReferenceKey;
import conductance.api.machine.sync.ReferenceSynchronizedListener;
import conductance.api.machine.sync.RequireRenderUpdate;
import conductance.api.machine.sync.SpecialHandled;
import conductance.api.machine.sync.Synchronized;
import conductance.api.util.world.WorldUtils;
import conductance.Conductance;

public class ManagedDataMapImpl implements ManagedDataMap {

	private final IManaged managed;
	private final ReferenceKey[] fields;
	private final Map<ReferenceKey, Reference> references;

	@Getter
	private final Object2IntMap<ReferenceKey> persistenceFields;
	private final Map<String, ReferenceKey> persistenceMapping;
	private final BitSet dirtyPersistenceFields;

	@Getter
	private final Object2IntMap<ReferenceKey> syncFields;
	private final Map<String, ReferenceKey> syncMapping;
	private final BitSet dirtySyncFields;
	@Getter(AccessLevel.PACKAGE)
	private final Map<ReferenceKey, List<ReferenceSynchronizedListener<?>>> syncClientUpdateListeners;

	public ManagedDataMapImpl(final IManaged managed) {
		this.managed = managed;
		this.fields = Util.make(new ArrayList<ReferenceKeyImpl>(), list ->
				ManagedDataMapImpl.collectFields(managed.getClass(), managed, list)
		).toArray(ReferenceKeyImpl[]::new);

		final Map<ReferenceKey, Reference> referenceMap = new HashMap<>();
		final ArrayList<ReferenceKey> persistenceFieldList = new ArrayList<>();
		final ArrayList<ReferenceKey> syncFieldList = new ArrayList<>();
		for (final ReferenceKey field : this.fields) {
			final ReflectionHolder holder = ReflectionHolder.of(field.getRawField(), managed);
			final Reference reference = ReferenceHelper.of(field, holder);
			referenceMap.put(field, reference);
			if (field.isPersisted()) {
				persistenceFieldList.add(field);
			}
			if (field.isSynchronized()) {
				syncFieldList.add(field);
			}
		}
		this.references = Collections.unmodifiableMap(referenceMap);
		this.persistenceFields = Object2IntMaps.unmodifiable(Util.make(new Object2IntArrayMap<>(), map -> {
			for (int i = 0; i < persistenceFieldList.size(); ++i) {
				final ReferenceKey refKey = persistenceFieldList.get(i);
				final Reference ref = this.references.get(refKey);
				final int finalI = i;
				ref.setPersistenceStateCallback(dirty -> this.onFieldPersistenceDirty(ref, finalI, dirty));
				map.put(refKey, i);
			}
		}));

		this.syncFields = Object2IntMaps.unmodifiable(Util.make(new Object2IntArrayMap<>(), map -> {
			for (int i = 0; i < syncFieldList.size(); ++i) {
				final ReferenceKey refKey = syncFieldList.get(i);
				final Reference ref = this.references.get(refKey);
				final int finalI = i;
				ref.setSyncStateCallback(dirty -> this.onFieldSyncDirty(ref, finalI, dirty));
				map.put(refKey, i);
			}
		}));

		this.persistenceMapping = Collections.unmodifiableMap(Util.make(new Object2ObjectArrayMap<>(), map -> this.persistenceFields.keySet().forEach(field -> {
			if (map.containsKey(field.getPersistenceKey())) {
				throw new RuntimeException("Duplicate field with persistence key %s (%s, %s)".formatted(field.getPersistenceKey(), map.get(field.getPersistenceKey()).getRawField(), field.getRawField()));
			}
			map.put(field.getPersistenceKey(), field);
		})));
		this.dirtyPersistenceFields = new BitSet(this.persistenceFields.size());

		this.syncMapping = Collections.unmodifiableMap(Util.make(new Object2ObjectArrayMap<>(), map -> this.syncFields.keySet().forEach(field -> {
			if (map.containsKey(field.getSyncKey())) {
				throw new RuntimeException("Duplicate field with synchronization key %s (%s, %s)".formatted(field.getSyncKey(), map.get(field.getSyncKey()).getRawField(), field.getRawField()));
			}
			map.put(field.getSyncKey(), field);
		})));
		this.dirtySyncFields = new BitSet(this.syncFields.size());
		this.syncClientUpdateListeners = Collections.unmodifiableMap(Util.make(new IdentityHashMap<>(), map -> {
			for (final ReferenceKey key : this.syncFields.keySet()) {
				final ArrayList<ReferenceSynchronizedListener<?>> listeners = new ArrayList<>();
				map.put(key, listeners);
				if (key.getRawField().isAnnotationPresent(OnSynchronized.class)) {
					final Method method = ManagedDataMapImpl.findSyncListenerMethod(key.getRawField(), managed.getClass());
					listeners.add((oldValue, newValue) -> {
						try {
							method.invoke(managed, oldValue, newValue);
						} catch (final IllegalAccessException | InvocationTargetException e) {
							throw new RuntimeException(e);
						}
					});
				}
				if (key.getRawField().isAnnotationPresent(RequireRenderUpdate.class)) {
					listeners.add((oldValue, newValue) -> WorldUtils.requestRenderUpdate(
							SyncHelperImpl.tryGetLevel(managed), SyncHelperImpl.tryGetBlockPos(managed), SyncHelperImpl.tryGetBlockState(managed)
					));
				}
			}
		}));
	}

	private void onFieldPersistenceDirty(final Reference reference, final int index, final boolean isDirty) {
		this.dirtyPersistenceFields.set(index, isDirty);
	}

	private void onFieldSyncDirty(final Reference reference, final int index, final boolean isDirty) {
		this.dirtySyncFields.set(index, isDirty);
	}

	public void init() {
		this.references.values().forEach(Reference::init);
		this.tick();
	}

	@Override
	public void tick() {
		this.references.forEach((key, reference) -> {
			if (!key.hasSpecialHandling()) {
				reference.tick();
			} else if (((ReferenceKeyImpl) key).specialTestDirty(reference.getValueHolder().get())) {
				reference.markDirty();
			}
		});
	}

	@Nullable
	public Reference getReference(final ReferenceKey field) {
		return this.references.get(field);
	}

	public boolean hasDirtyPersistentFields() {
		return !this.dirtyPersistenceFields.isEmpty();
	}

	public boolean hasDirtySyncFields() {
		return !this.dirtySyncFields.isEmpty();
	}

	@Override
	public boolean isDirty() {
		return this.hasDirtyPersistentFields() || this.hasDirtySyncFields();
	}

	@Override
	public void markDirty() {
		this.syncFields.keySet().stream().map(this.references::get).forEach(Reference::markDirty);
	}

	final boolean hasSyncClientUpdateListeners(final ReferenceKey key) {
		final List<ReferenceSynchronizedListener<?>> listeners = this.getSyncClientUpdateListeners().get(key);
		return listeners != null && !listeners.isEmpty();
	}

	@SuppressWarnings("unchecked")
	final <T> void notifySyncClientUpdateListeners(final Reference ref, @Nullable final T oldValue, @Nullable final T newValue) {
		final List<ReferenceSynchronizedListener<?>> listeners = this.getSyncClientUpdateListeners().get(ref.getKey());
		if (listeners != null) {
			listeners.forEach(l -> {
				final ReferenceSynchronizedListener<T> listener = (ReferenceSynchronizedListener<T>) l;
				try {
					listener.onReferenceSynchronized(oldValue, newValue);
				} catch (final Throwable e) {
					Conductance.LOGGER.error("An error occurred while notifying client field sync listeners", e);
				}
			});
		}
	}

	@Override
	public CompoundTag serialize(final Operation operation, final HolderLookup.Provider registries) {
		final CompoundTag result = new CompoundTag();
		(operation.isPersist() ? this.persistenceMapping : this.syncMapping).entrySet().stream().filter(entry -> {
			if (operation.isFull()) {
				return true;
			}
			if (operation.isPersist()) {
				return this.dirtyPersistenceFields.get(this.persistenceFields.getInt(entry.getValue()));
			} else {
				return this.dirtySyncFields.get(this.syncFields.getInt(entry.getValue()));
			}
		}).forEach(entry -> {
			try {
				final Reference ref = this.getReference(entry.getValue());
				final Tag serializedRef = SyncHelperImpl.serializeRef(operation, ref, registries);
				result.put(entry.getKey(), Objects.requireNonNullElseGet(serializedRef, CompoundTag::new));
				ref.clearPersistenceMark();
			} catch (final Throwable e) {
				Conductance.LOGGER.error("An error occurred while serializing field {}", entry.getValue().getRawField(), e);
			}
		});
		return result;
	}

	@Override
	public void deserialize(final Operation operation, final CompoundTag tag, final HolderLookup.Provider registries) {
		for (final String tagKey : tag.getAllKeys()) {
			final ReferenceKey field = (operation.isPersist() ? this.persistenceMapping : this.syncMapping).get(tagKey);
			if (field == null) {
				Conductance.LOGGER.warn("Cannot deserialize data for key {} since it has no mapping.", tagKey);
			} else {
				try {
					final Reference ref = this.getReference(field);
					final Tag itemTag = tag.get(tagKey);
					SyncHelperImpl.deserializeRef(this, operation, ref, itemTag instanceof final CompoundTag c && c.isEmpty() ? null : itemTag, registries);
					if (operation.isPersist()) {
						ref.clearPersistenceMark();
					} else {
						ref.clearSyncMark();
					}
				} catch (final Throwable e) {
					Conductance.LOGGER.error("An error occurred while deserializing field {}", field.getRawField(), e);
				}
			}
		}
	}

	private static void collectFields(final Class<?> clazz, final Object containingInstance, final List<ReferenceKeyImpl> list) {
		for (final Field field : clazz.getDeclaredFields()) {
			if (!Modifier.isStatic(field.getModifiers())) {
				final boolean persist = field.isAnnotationPresent(Persisted.class);
				final boolean synced = field.isAnnotationPresent(Synchronized.class);
				if (persist || synced) {
					if (!SyncFieldSerializerRegisterImpl.INSTANCE.canHandleType(field.getGenericType())) {
						throw new IllegalStateException("Field " + field + " is marked for managing but is not supported.");
					}
					list.add(Util.make(ReferenceKeyImpl.of(field, containingInstance), key -> {
						if (key.hasSpecialHandling()) {
							final Method[] methods = ManagedDataMapImpl.findSpecialHandlerMethods(key.getRawField(), clazz);
							key.setSpecialHandlerTestDirtyMethod(methods[0]);
							key.setSpecialHandlerSerializeMethod(methods[1]);
							key.setSpecialHandlerDeserializeMethod(methods[2]);
						}
					}));
				}
			}
		}
		if (clazz.getSuperclass() != Object.class) {
			ManagedDataMapImpl.collectFields(clazz.getSuperclass(), containingInstance, list);
		}
	}

	private static Method findSyncListenerMethod(final Field field, final Class<?> clazz) {
		Class<?> clazz2 = clazz;
		final String methodName = field.getAnnotation(OnSynchronized.class).method();
		Method method = null;
		while (clazz2 != null && method == null) {
			try {
				method = clazz2.getDeclaredMethod(methodName, field.getType(), field.getType());
				method.setAccessible(true);
				if (!Void.TYPE.equals(method.getReturnType())) {
					throw new IllegalArgumentException("Listener method %s for field %s#%s can only return void".formatted(methodName, clazz.getName(), field.getName()));
				}
			} catch (final NoSuchMethodException ignored) {
			}
			clazz2 = clazz2.getSuperclass();
		}
		if (method == null) {
			throw new IllegalArgumentException("Could not find the listener method %s for field %s#%s".formatted(methodName, clazz.getName(), field.getName()));
		}
		return method;
	}

	private static Method[] findSpecialHandlerMethods(final Field field, final Class<?> clazz) {
		Class<?> clazz2 = clazz;
		final Method[] methods = new Method[3];
		final SpecialHandled annotation = field.getAnnotation(SpecialHandled.class);
		while (clazz2 != null && (methods[0] == null || methods[1] == null || methods[2] == null)) {
			if (methods[0] == null) {
				try {
					methods[0] = clazz2.getDeclaredMethod(annotation.testDirtyMethod(), field.getType());
					methods[0].setAccessible(true);
					if (!Boolean.TYPE.equals(methods[0].getReturnType())) {
						throw new IllegalArgumentException("Special handler dirty test method %s must return a boolean".formatted(methods[0]));
					}
				} catch (final NoSuchMethodException ignored) {
				}
			}
			if (methods[1] == null) {
				try {
					methods[1] = clazz2.getDeclaredMethod(annotation.serializeMethod(), field.getType());
					methods[1].setAccessible(true);
					if (!CompoundTag.class.equals(methods[1].getReturnType())) {
						throw new IllegalArgumentException("Special handler serialize method %s must return a %s".formatted(methods[1], CompoundTag.class.getName()));
					}
				} catch (final NoSuchMethodException ignored) {
				}
			}
			if (methods[2] == null) {
				try {
					methods[2] = clazz2.getDeclaredMethod(annotation.deserializeMethod(), CompoundTag.class);
					methods[2].setAccessible(true);
					if (!field.getType().isAssignableFrom(methods[2].getReturnType())) {
						throw new IllegalArgumentException("Special handler deserialize method %s must return a %s".formatted(methods[1], field.getType().getName()));
					}
				} catch (final NoSuchMethodException ignored) {
				}
			}
			clazz2 = clazz2.getSuperclass();
		}
		if (methods[0] == null) {
			throw new IllegalArgumentException("Could not find the dirty test method %s for field %s#%s".formatted(annotation.testDirtyMethod(), clazz.getName(), field.getName()));
		}
		if (methods[1] == null) {
			throw new IllegalArgumentException("Could not find the serialize method %s for field %s#%s".formatted(annotation.serializeMethod(), clazz.getName(), field.getName()));
		}
		if (methods[2] == null) {
			throw new IllegalArgumentException("Could not find the deserialize method %s for field %s#%s".formatted(annotation.deserializeMethod(), clazz.getName(), field.getName()));
		}
		return methods;
	}
}
