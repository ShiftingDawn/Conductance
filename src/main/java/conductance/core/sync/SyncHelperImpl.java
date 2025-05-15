package conductance.core.sync;

import java.lang.reflect.Type;
import java.util.Objects;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.sync.Checker;
import conductance.api.machine.sync.IManaged;
import conductance.api.machine.sync.ManagedDataMap;
import conductance.api.machine.sync.Operation;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.ReferenceHandler;
import conductance.api.machine.sync.Serializer;
import conductance.api.machine.sync.SyncHelper;
import conductance.Conductance;

public final class SyncHelperImpl implements SyncHelper {

	private static final CompoundTag NULL_TAG = Util.make(new CompoundTag(), nbt -> nbt.putString("NULL", "NULL"));
	public static final SyncHelperImpl INSTANCE = new SyncHelperImpl();

	private SyncHelperImpl() {
	}

	@Override
	public ManagedDataMap requestDataMap(final IManaged managed) {
		return new ManagedDataMapImpl(managed);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean isEqual(@Nullable final Object value1, @Nullable final Object value2) {
		if ((value1 == null) != (value2 == null)) {
			return false;
		}
		if (value1 == null) {
			return true;
		}
		if (!value1.getClass().isAssignableFrom(value2.getClass()) && !value2.getClass().isAssignableFrom(value1.getClass())) {
			return false;
		}
		final Checker checker1 = SyncFieldSerializerRegisterImpl.INSTANCE.getChecker(value1.getClass());
		final Checker checker2 = SyncFieldSerializerRegisterImpl.INSTANCE.getChecker(value2.getClass());
		if (checker1 != checker2) {
			return false;
		}
		return checker1 == null ? Objects.equals(value1, value2) : checker1.equals(value1, value2) && checker2.equals(value1, value2);
	}

	@Override
	public int getSerializerId(final Serializer<?> serializer) {
		return SyncFieldSerializerRegisterImpl.INSTANCE.getSerializerId(serializer);
	}

	@Override
	public Serializer<?> getSerializerById(final int sid) {
		return SyncFieldSerializerRegisterImpl.INSTANCE.getSerializerById(sid);
	}

	@Override
	@Nullable
	public ReferenceHandler getHandlerByType(final Type type) {
		return SyncFieldSerializerRegisterImpl.INSTANCE.getHandler(type);
	}

	@Override
	public Serializer<?> getSerializerByHandler(final ReferenceHandler handler) {
		return SyncFieldSerializerRegisterImpl.INSTANCE.getSerializerByHandler(handler);
	}

	public static Tag writeRefToNbt(final Operation operation, final Reference ref, final HolderLookup.Provider registries) {
		final ReferenceHandler handler = CAPI.syncHelper().getHandlerByType(ref.getKey().getRawType());
		if (handler == null) {
			Conductance.LOGGER.warn("Cannot serialize field {} because no matching {} was registered", ref.getKey().getRawField(), ReferenceHandler.class.getName());
			return SyncHelperImpl.NULL_TAG;
		}
		if (ref.getKey().hasSpecialHandling()) {
			final Object currentValue = ref.getValueHolder().get();
			if (currentValue == null) {
				return SyncHelperImpl.NULL_TAG;
			}
			final CompoundTag serializedField = ((ReferenceKeyImpl) ref.getKey()).specialSerialize(currentValue);
			return Util.make(new CompoundTag(), nbt -> {
				nbt.put("k", serializedField);
				final Serializer<?> serializer = handler.readFromReference(operation, ref, registries);
				final Tag tag = serializer.serialize(operation, ref, registries);
				nbt.put("v", Objects.requireNonNullElse(tag, SyncHelperImpl.NULL_TAG));
			});
		}
		final Serializer<?> serializer = handler.readFromReference(operation, ref, registries);
		return Objects.requireNonNullElse(serializer.serialize(operation, ref, registries), SyncHelperImpl.NULL_TAG);
	}

	public static void readRefFromNbt(final Operation operation, final Reference ref, final Tag tag, final HolderLookup.Provider registries) {
		final ReferenceHandler handler = CAPI.syncHelper().getHandlerByType(ref.getKey().getRawType());
		if (handler == null) {
			Conductance.LOGGER.warn("Cannot deserialize field {} because no matching {} was registered", ref.getKey().getRawField(), ReferenceHandler.class.getName());
			return;
		}
		if (ref.getKey().hasSpecialHandling()) {
			if (SyncHelperImpl.NULL_TAG.equals(tag)) {
				ref.getValueHolder().set(null);
				return;
			}
			final CompoundTag serializedField = ((CompoundTag) tag).getCompound("k");
			Object currentValue = ref.getValueHolder().get();
			if (currentValue == null || !Objects.equals(serializedField, ((ReferenceKeyImpl) ref.getKey()).specialSerialize(currentValue))) {
				currentValue = ((ReferenceKeyImpl) ref.getKey()).specialDeserialize(serializedField);
				ref.getValueHolder().set(currentValue);
			}
			final Serializer<?> serializer = CAPI.syncHelper().getSerializerByHandler(handler);
			serializer.deserialize(operation, ref, ((CompoundTag) tag).getCompound("v"), registries);
			handler.writeToReference(operation, ref, serializer, registries);
			return;
		}
		final Serializer<?> serializer = CAPI.syncHelper().getSerializerByHandler(handler);
		serializer.deserialize(operation, ref, SyncHelperImpl.NULL_TAG.equals(tag) ? null : tag, registries);
		handler.writeToReference(operation, ref, serializer, registries);
	}

	public static void writeRefToNetwork(final Operation operation, final RegistryFriendlyByteBuf buf, final Reference ref, final HolderLookup.Provider registries) {
		final ReferenceHandler handler = CAPI.syncHelper().getHandlerByType(ref.getKey().getRawType());
		if (handler == null) {
			Conductance.LOGGER.warn("Cannot serialize field {} because no matching {} was registered", ref.getKey().getRawField(), ReferenceHandler.class.getName());
			return;
		}
		if (ref.getKey().hasSpecialHandling()) {
			final Object currentValue = ref.getValueHolder().get();
			if (currentValue == null) {
				buf.writeBoolean(false);
				return;
			}
			buf.writeBoolean(true);
			buf.writeNbt(((ReferenceKeyImpl) ref.getKey()).specialSerialize(currentValue));
			final Serializer<?> serializer = handler.readFromReference(operation, ref, registries);
			serializer.toNetwork(operation, ref, buf, registries);
			return;
		}
		final Serializer<?> serializer = handler.readFromReference(operation, ref, registries);
		serializer.toNetwork(operation, ref, buf, registries);
	}

	public static void readRefFromNetwork(final ManagedDataMapImpl map, final Operation operation, final RegistryFriendlyByteBuf buf, final Reference ref, final HolderLookup.Provider registries) {
		final ReferenceHandler handler = CAPI.syncHelper().getHandlerByType(ref.getKey().getRawType());
		if (handler == null) {
			Conductance.LOGGER.warn("Cannot deserialize field {} because no matching {} was registered", ref.getKey().getRawField(), ReferenceHandler.class.getName());
			return;
		}
		if (ref.getKey().hasSpecialHandling()) {
			if (!buf.readBoolean()) {
				if (map.hasSyncClientUpdateListeners(ref.getKey())) {
					final Object oldValue = ref.getValueHolder().get();
					ref.getValueHolder().set(null);
					map.notifySyncClientUpdateListeners(ref, oldValue, null);
				} else {
					ref.getValueHolder().set(null);
				}
				return;
			}
			final CompoundTag serializedField = buf.readNbt();
			final Object oldValue = ref.getValueHolder().get();
			Object currentValue = oldValue;
			if (currentValue == null || !Objects.equals(serializedField, ((ReferenceKeyImpl) ref.getKey()).specialSerialize(currentValue))) {
				currentValue = ((ReferenceKeyImpl) ref.getKey()).specialDeserialize(serializedField);
				ref.getValueHolder().set(currentValue);
			}
			final Serializer<?> serializer = CAPI.syncHelper().getSerializerByHandler(handler);
			serializer.fromNetwork(operation, ref, buf, registries);
			handler.writeToReference(operation, ref, serializer, registries);
			final Object newValue = ref.getValueHolder().get();
			map.notifySyncClientUpdateListeners(ref, oldValue, newValue);
			return;
		}
		if (map.hasSyncClientUpdateListeners(ref.getKey())) {
			final Object oldValue = ref.getValueHolder().get();
			final Serializer<?> serializer = CAPI.syncHelper().getSerializerByHandler(handler);
			serializer.fromNetwork(operation, ref, buf, registries);
			handler.writeToReference(operation, ref, serializer, registries);
			final Object newValue = ref.getValueHolder().get();
			map.notifySyncClientUpdateListeners(ref, oldValue, newValue);
		} else {
			final Serializer<?> serializer = CAPI.syncHelper().getSerializerByHandler(handler);
			serializer.fromNetwork(operation, ref, buf, registries);
			handler.writeToReference(operation, ref, serializer, registries);
		}
	}
}
