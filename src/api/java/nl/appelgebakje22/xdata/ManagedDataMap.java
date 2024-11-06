package nl.appelgebakje22.xdata;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import lombok.Getter;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.NetworkInput;
import nl.appelgebakje22.xdata.adapter.NetworkOutput;
import nl.appelgebakje22.xdata.adapter.TableAdapter;
import nl.appelgebakje22.xdata.api.IManaged;
import nl.appelgebakje22.xdata.api.Persisted;
import nl.appelgebakje22.xdata.api.Synchronized;
import nl.appelgebakje22.xdata.ref.Reference;
import nl.appelgebakje22.xdata.ref.ReferenceKey;
import nl.appelgebakje22.xdata.ref.ReflectionHolder;
import org.jetbrains.annotations.Nullable;

public class ManagedDataMap {

	private final IManaged managed;
	private final ReferenceKey[] fields;
	private final Map<ReferenceKey, Reference> references;

	@Getter
	private final Object2IntMap<ReferenceKey> persistenceFields;
	private final Map<String, ReferenceKey> persistenceMapping;
	private final BitSet dirtyPersistenceFields;

	@Getter
	private final Object2IntMap<ReferenceKey> syncFields;
	private final Map<String, ReferenceKey> syncMapper;
	private final BitSet dirtySyncFields;

	public ManagedDataMap(final IManaged managed) {
		this.managed = managed;
		this.fields = ManagedDataMap.collectFields(managed.getClass());

		final Map<ReferenceKey, Reference> referenceMap = new HashMap<>();
		final ArrayList<ReferenceKey> persistenceFieldList = new ArrayList<>();
		final ArrayList<ReferenceKey> syncFieldList = new ArrayList<>();
		for (final ReferenceKey field : this.fields) {
			final ReflectionHolder holder = ReflectionHolder.of(field.getRawField(), managed);
			final Reference reference = Reference.of(field, holder);
			referenceMap.put(field, reference);
			if (field.isPersisted()) {
				persistenceFieldList.add(field);
			}
			if (field.isSynchronized()) {
				syncFieldList.add(field);
			}
		}
		this.references = Collections.unmodifiableMap(referenceMap);

		final Object2IntMap<ReferenceKey> persistenceFields = new Object2IntArrayMap<>();
		for (int i = 0; i < persistenceFieldList.size(); ++i) {
			final ReferenceKey refKey = persistenceFieldList.get(i);
			final Reference ref = this.references.get(refKey);
			final int finalI = i;
			ref.setPersistenceStateCallback(dirty -> this.onFieldPersistenceDirty(ref, finalI, dirty));
			persistenceFields.put(refKey, i);
		}
		this.persistenceFields = Object2IntMaps.unmodifiable(persistenceFields);
		final Object2IntMap<ReferenceKey> syncFields = new Object2IntArrayMap<>();
		for (int i = 0; i < syncFieldList.size(); ++i) {
			final ReferenceKey refKey = syncFieldList.get(i);
			final Reference ref = this.references.get(refKey);
			final int finalI = i;
			ref.setSyncStateCallback(dirty -> this.onFieldSyncDirty(ref, finalI, dirty));
			syncFields.put(refKey, i);
		}
		this.syncFields = Object2IntMaps.unmodifiable(syncFields);

		this.persistenceMapping = Collections.unmodifiableMap(XData.make(new Object2ObjectArrayMap<>(), map -> this.persistenceFields.keySet().forEach(field -> {
			if (map.containsKey(field.getPersistenceKey())) {
				throw new RuntimeException("Duplicate field with persistence key %s (%s, %s)".formatted(field.getPersistenceKey(), map.get(field.getPersistenceKey()).getRawField(), field.getRawField()));
			}
			map.put(field.getPersistenceKey(), field);
		})));
		this.dirtyPersistenceFields = new BitSet(this.persistenceFields.size());

		this.syncMapper = Collections.unmodifiableMap(XData.make(new Object2ObjectArrayMap<>(), map -> this.syncFields.keySet().forEach(field -> {
			if (map.containsKey(field.getSyncKey())) {
				throw new RuntimeException("Duplicate field with synchronization key %s (%s, %s)".formatted(field.getSyncKey(), map.get(field.getSyncKey()).getRawField(), field.getRawField()));
			}
			map.put(field.getSyncKey(), field);
		})));
		this.dirtySyncFields = new BitSet(this.syncFields.size());
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
		this.references.values().forEach(Reference::tick);
		if (this.hasDirtySyncFields()) {
			//TODO SYNC
		}
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

	public TableAdapter serialize(final Operation operation, final AdapterFactory adapters) {
		final TableAdapter result = adapters.table();
		this.persistenceMapping.entrySet().stream().filter(entry -> switch (operation) {
			case FULL -> true;
			case PARTIAL -> this.dirtyPersistenceFields.get(this.persistenceFields.getInt(entry.getValue()));
		}).forEach(entry -> {
			final Reference ref = this.getReference(entry.getValue());
			final BaseAdapter serializedRef = XDataSerializationUtils.writeRefToAdapter(operation, adapters, ref);
			if (serializedRef != null) {
				result.set(entry.getKey(), serializedRef);
			}
			ref.clearPersistenceMark();
		});
		return result;
	}

	public void deserialize(final Operation operation, final AdapterFactory adapters, final TableAdapter table) {
		for (final String tagKey : table.getKeys()) {
			final ReferenceKey field = this.persistenceMapping.get(tagKey);
			if (field == null) {
				//TODO log
				System.out.println("Cannot deserialize data for key " + tagKey + " since it has no mapping.");
			} else {
				final Reference ref = this.getReference(field);
				XDataSerializationUtils.readRefFromAdapter(operation, adapters, ref, table.get(tagKey));
				ref.clearPersistenceMark();
			}
		}
	}

	public void toNetwork(final Operation operation, final NetworkOutput output) {
		output.write(switch (operation) {
			case FULL -> this.syncMapper.size();
			case PARTIAL -> this.dirtySyncFields.cardinality();
		});
		this.syncMapper.entrySet().stream().filter(entry -> switch (operation) {
			case FULL -> true;
			case PARTIAL -> this.dirtyPersistenceFields.get(this.syncFields.getInt(entry.getValue()));
		}).forEach(entry -> {
			output.write(entry.getKey());
			final Reference ref = this.getReference(entry.getValue());
			XDataSerializationUtils.writeRefToNetwork(operation, output, ref);
			ref.clearSyncMark();
		});
	}

	public void fromNetwork(final Operation operation, final NetworkInput input) {
		final int iterationCount = input.readInt();
		for (int i = 0; i < iterationCount; ++i) {
			final String syncKey = input.readString();
			final ReferenceKey field = this.syncMapper.get(syncKey);
			if (field == null) {
				//TODO log
				System.out.println("Cannot deserialize data for key " + syncKey + " since it has no mapping.");
			} else {
				final Reference ref = this.getReference(field);
				XDataSerializationUtils.readRefFromNetwork(operation, input, ref);
				ref.clearSyncMark();
			}
		}
	}

	private static ReferenceKey[] collectFields(final Class<?> clazz) {
		final List<ReferenceKey> resultList = new ArrayList<>();
		Class<?> currentClazz = clazz;
		while (currentClazz != null) {
			for (final Field field : currentClazz.getDeclaredFields()) {
				if (!Modifier.isStatic(field.getModifiers())) {
					final boolean persist = field.isAnnotationPresent(Persisted.class);
					final boolean synced = field.isAnnotationPresent(Synchronized.class);
					if (persist || synced) {
						if (!XDataRegister.canHandleType(field.getGenericType())) {
							throw new IllegalStateException("Field " + field + " is marked for XData but is not supported.");
						}
						resultList.add(ReferenceKey.of(field));
					}
				}
			}
			currentClazz = currentClazz.getSuperclass();
		}
		return resultList.toArray(new ReferenceKey[0]);
	}
}
