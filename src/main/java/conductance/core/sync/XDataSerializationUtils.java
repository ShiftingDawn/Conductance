package conductance.core.sync;

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
	public static Tag writeRefToAdapter(final Operation operation, final ReferenceImpl ref) {
		final ReferenceHandler handler = CAPI.syncHelper().getHandlerByType(ref.getKey().getRawType());
		if (handler == null) {
			Conductance.LOGGER.warn("Cannot serialize field {} because no matching {} was registered", ref.getKey().getRawField(), ReferenceHandler.class.getName());
			return null;
		}
		final Serializer<?> serializer = handler.readFromReference(operation, ref);
		return serializer.serialize(ref);
	}

	public static void readRefFromAdapter(final Operation operation, final ReferenceImpl ref, final Tag tag) {
		final ReferenceHandler handler = CAPI.syncHelper().getHandlerByType(ref.getKey().getRawType());
		if (handler == null) {
			Conductance.LOGGER.warn("Cannot deserialize field {} because no matching {} was registered", ref.getKey().getRawField(), ReferenceHandler.class.getName());
			return;
		}
		final Serializer<?> serializer = CAPI.syncHelper().getSerializerByHandler(handler);
		serializer.deserialize(ref, tag);
		handler.writeToReference(operation, ref, serializer);
	}

	public static void writeRefToNetwork(final Operation operation, final RegistryFriendlyByteBuf buf, final ReferenceImpl ref) {
		final ReferenceHandler handler = CAPI.syncHelper().getHandlerByType(ref.getKey().getRawType());
		if (handler == null) {
			Conductance.LOGGER.warn("Cannot serialize field {} because no matching {} was registered", ref.getKey().getRawField(), ReferenceHandler.class.getName());
			return;
		}
		final Serializer<?> serializer = handler.readFromReference(operation, ref);
		serializer.toNetwork(ref, buf);
	}

	public static void readRefFromNetwork(final Operation operation, final RegistryFriendlyByteBuf buf, final ReferenceImpl ref) {
		final ReferenceHandler handler = CAPI.syncHelper().getHandlerByType(ref.getKey().getRawType());
		if (handler == null) {
			Conductance.LOGGER.warn("Cannot deserialize field {} because no matching {} was registered", ref.getKey().getRawField(), ReferenceHandler.class.getName());
			return;
		}
		final Serializer<?> serializer = handler.readFromReference(operation, ref);
		serializer.fromNetwork(ref, buf);
	}

	private XDataSerializationUtils() {
	}
}
