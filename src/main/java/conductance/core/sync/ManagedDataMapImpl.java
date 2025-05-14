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
import java.util.function.BiFunction;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
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
import conductance.api.machine.sync.Synchronized;
import conductance.Conductance;
import conductance.core.sync.ref.ReferenceImpl;
import conductance.core.sync.ref.ReferenceKeyImpl;
import conductance.core.sync.ref.ReflectionHolder;

public class ManagedDataMapImpl implements ManagedDataMap {

	private static final BiFunction<Field, Class<?>, Method> SYNC_LISTENER_CACHE;
	private final IManaged managed;
	private final ReferenceKeyImpl[] fields;
	private final Map<ReferenceKey, ReferenceImpl> references;

	@Getter
	private final Object2IntMap<ReferenceKey> persistenceFields;
	private final Map<String, ReferenceKey> persistenceMapping;
	private final BitSet dirtyPersistenceFields;

	@Getter
	private final Object2IntMap<ReferenceKey> syncFields;
	private final Map<String, ReferenceKey> syncMapper;
	private final BitSet dirtySyncFields;
	@Getter(AccessLevel.PACKAGE)
	private final Map<ReferenceKey, List<ReferenceSynchronizedListener<?>>> syncClientUpdateListeners;

	public ManagedDataMapImpl(final IManaged managed) {
		this.managed = managed;
		this.fields = Util.make(new ArrayList<ReferenceKeyImpl>(), list -> ManagedDataMapImpl.collectFields(managed.getClass(), list)).toArray(ReferenceKeyImpl[]::new);

		final Map<ReferenceKey, ReferenceImpl> referenceMap = new HashMap<>();
		final ArrayList<ReferenceKey> persistenceFieldList = new ArrayList<>();
		final ArrayList<ReferenceKey> syncFieldList = new ArrayList<>();
		for (final ReferenceKeyImpl field : this.fields) {
			final ReflectionHolder holder = ReflectionHolder.of(field.getRawField(), managed);
			final ReferenceImpl reference = ReferenceImpl.of(field, holder);
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

		this.syncMapper = Collections.unmodifiableMap(Util.make(new Object2ObjectArrayMap<>(), map -> this.syncFields.keySet().forEach(field -> {
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
					final Method method = ManagedDataMapImpl.SYNC_LISTENER_CACHE.apply(key.getRawField(), managed.getClass());
					if (method != null) {
						listeners.add((oldValue, newValue) -> {
							try {
								method.invoke(managed, oldValue, newValue);
							} catch (final IllegalAccessException | InvocationTargetException e) {
								throw new RuntimeException(e);
							}
						});
					}
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
		this.tick();
	}

	public void tick() {
		this.references.values().forEach(ReferenceImpl::tick);
	}

	@Nullable
	public ReferenceImpl getReference(final ReferenceKey field) {
		return this.references.get(field);
	}

	public boolean hasDirtyPersistentFields() {
		return !this.dirtyPersistenceFields.isEmpty();
	}

	public boolean hasDirtySyncFields() {
		return !this.dirtySyncFields.isEmpty();
	}

	@Override
	public void markDirty() {
		this.syncFields.keySet().stream().map(this.references::get).forEach(ReferenceImpl::markDirty);
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
				} catch (final Throwable t) {
					Conductance.LOGGER.error("An error occurred while notifying client field sync listeners", t);
				}
			});
		}
	}

	@Override
	public CompoundTag serialize(final Operation operation, final HolderLookup.Provider registries) {
		final CompoundTag result = new CompoundTag();
		this.persistenceMapping.entrySet().stream().filter(entry -> switch (operation) {
			case FULL -> true;
			case PARTIAL -> this.dirtyPersistenceFields.get(this.persistenceFields.getInt(entry.getValue()));
		}).forEach(entry -> {
			final ReferenceImpl ref = this.getReference(entry.getValue());
			final Tag serializedRef = XDataSerializationUtils.writeRefToNbt(operation, ref, registries);
			if (serializedRef != null) {
				result.put(entry.getKey(), serializedRef);
			}
			ref.clearPersistenceMark();
		});
		return result;
	}

	@Override
	public void deserialize(final Operation operation, final CompoundTag nbt, final HolderLookup.Provider registries) {
		for (final String tagKey : nbt.getAllKeys()) {
			final ReferenceKey field = this.persistenceMapping.get(tagKey);
			if (field == null) {
				Conductance.LOGGER.warn("Cannot deserialize data for key {} since it has no mapping.", tagKey);
			} else {
				final ReferenceImpl ref = this.getReference(field);
				XDataSerializationUtils.readRefFromNbt(operation, ref, nbt.get(tagKey), registries);
				ref.clearPersistenceMark();
			}
		}
	}

	@Override
	public void toNetwork(final Operation operation, final RegistryFriendlyByteBuf buf, final HolderLookup.Provider registries) {
		buf.writeVarInt(switch (operation) {
			case FULL -> this.syncMapper.size();
			case PARTIAL -> this.dirtySyncFields.cardinality();
		});
		this.syncMapper.entrySet().stream().filter(entry -> switch (operation) {
			case FULL -> true;
			case PARTIAL -> this.dirtyPersistenceFields.get(this.syncFields.getInt(entry.getValue()));
		}).forEach(entry -> {
			buf.writeUtf(entry.getKey());
			final ReferenceImpl ref = this.getReference(entry.getValue());
			XDataSerializationUtils.writeRefToNetwork(operation, buf, ref, registries);
			ref.clearSyncMark();
		});
	}

	@Override
	public void fromNetwork(final Operation operation, final RegistryFriendlyByteBuf buf, final HolderLookup.Provider registries) {
		final int iterationCount = buf.readVarInt();
		for (int i = 0; i < iterationCount; ++i) {
			final String syncKey = buf.readUtf();
			final ReferenceKey field = this.syncMapper.get(syncKey);
			if (field == null) {
				Conductance.LOGGER.warn("Cannot deserialize data for key {} since it has no mapping.", syncKey);
			} else {
				final ReferenceImpl ref = this.getReference(field);
				XDataSerializationUtils.readRefFromNetwork(this, operation, buf, ref, registries);
				ref.clearSyncMark();
			}
		}
	}

	private static void collectFields(final Class<?> clazz, final List<ReferenceKeyImpl> list) {
		for (final Field field : clazz.getDeclaredFields()) {
			if (!Modifier.isStatic(field.getModifiers())) {
				final boolean persist = field.isAnnotationPresent(Persisted.class);
				final boolean synced = field.isAnnotationPresent(Synchronized.class);
				if (persist || synced) {
					if (!SyncFieldSerializerRegisterImpl.INSTANCE.canHandleType(field.getGenericType())) {
						throw new IllegalStateException("Field " + field + " is marked for managing but is not supported.");
					}
					list.add(ReferenceKeyImpl.of(field));
				}
			}
		}
		if (clazz.getSuperclass() != Object.class) {
			ManagedDataMapImpl.collectFields(clazz.getSuperclass(), list);
		}
	}

	static {
		SYNC_LISTENER_CACHE = Util.memoize((field, clazz) -> {
			final Class<?> clazz2 = clazz;
			assert clazz2 != null;
			final String methodName = field.getAnnotation(OnSynchronized.class).method();
			Method method = null;
			while (clazz != null && method == null) {
				try {
					method = clazz.getDeclaredMethod(methodName, field.getType(), field.getType());
					method.setAccessible(true);
				} catch (final NoSuchMethodException ignored) {
				}
				clazz = clazz.getSuperclass();
			}
			if (method == null) {
				Conductance.LOGGER.error("Could not find the listener method {} for field {}#{}", methodName, clazz2.getName(), field.getName());
			}
			return method;
		});
	}
}
