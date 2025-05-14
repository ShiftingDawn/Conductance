package conductance.core.sync;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.sync.Operation;
import conductance.api.machine.sync.ReferenceHandler;
import conductance.api.machine.sync.Serializer;
import conductance.Conductance;
import conductance.core.sync.ref.ReferenceImpl;

public final class XDataSerializationUtils {

	@Nullable
	public static Tag writeRefToNbt(final Operation operation, final ReferenceImpl ref, final HolderLookup.Provider registries) {
		final ReferenceHandler handler = CAPI.syncHelper().getHandlerByType(ref.getKey().getRawType());
		if (handler == null) {
			Conductance.LOGGER.warn("Cannot serialize field {} because no matching {} was registered", ref.getKey().getRawField(), ReferenceHandler.class.getName());
			return null;
		}
		final Serializer<?> serializer = handler.readFromReference(operation, ref, registries);
		return serializer.serialize(operation, ref, registries);
	}

	public static void readRefFromNbt(final Operation operation, final ReferenceImpl ref, final Tag tag, final HolderLookup.Provider registries) {
		final ReferenceHandler handler = CAPI.syncHelper().getHandlerByType(ref.getKey().getRawType());
		if (handler == null) {
			Conductance.LOGGER.warn("Cannot deserialize field {} because no matching {} was registered", ref.getKey().getRawField(), ReferenceHandler.class.getName());
			return;
		}
		final Serializer<?> serializer = CAPI.syncHelper().getSerializerByHandler(handler);
		serializer.deserialize(operation, ref, tag, registries);
		handler.writeToReference(operation, ref, serializer, registries);
	}

	public static void writeRefToNetwork(final Operation operation, final RegistryFriendlyByteBuf buf, final ReferenceImpl ref, final HolderLookup.Provider registries) {
		final ReferenceHandler handler = CAPI.syncHelper().getHandlerByType(ref.getKey().getRawType());
		if (handler == null) {
			Conductance.LOGGER.warn("Cannot serialize field {} because no matching {} was registered", ref.getKey().getRawField(), ReferenceHandler.class.getName());
			return;
		}
		final Serializer<?> serializer = handler.readFromReference(operation, ref, registries);
		serializer.toNetwork(operation, ref, buf, registries);
	}

	public static void readRefFromNetwork(final ManagedDataMapImpl map, final Operation operation, final RegistryFriendlyByteBuf buf, final ReferenceImpl ref, final HolderLookup.Provider registries) {
		final ReferenceHandler handler = CAPI.syncHelper().getHandlerByType(ref.getKey().getRawType());
		if (handler == null) {
			Conductance.LOGGER.warn("Cannot deserialize field {} because no matching {} was registered", ref.getKey().getRawField(), ReferenceHandler.class.getName());
			return;
		}
		if (map.hasSyncClientUpdateListeners(ref.getKey())) {
			final Object oldValue = ref.getValueHolder().get();
			final Serializer<?> serializer = handler.readFromReference(operation, ref, registries);
			serializer.fromNetwork(operation, ref, buf, registries);
			final Object newValue = ref.getValueHolder().get();
			map.notifySyncClientUpdateListeners(ref, oldValue, newValue);
		} else {
			final Serializer<?> serializer = handler.readFromReference(operation, ref, registries);
			serializer.fromNetwork(operation, ref, buf, registries);
		}
	}

	private XDataSerializationUtils() {
	}
}
