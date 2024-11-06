package nl.appelgebakje22.xdata;

import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.NetworkInput;
import nl.appelgebakje22.xdata.adapter.NetworkOutput;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;
import org.jetbrains.annotations.Nullable;

public class XDataSerializationUtils {

	@Nullable
	public static BaseAdapter writeRefToAdapter(final Operation operation, final AdapterFactory adapters, final Reference ref) {
		final ReferenceHandler handler = XDataRegister.getHandler(ref.getKey().getRawType());
		if (handler == null) {
			//TODO logging
			System.out.printf("Cannot serialize field %s because no matching %s was registered%n", ref.getKey().getRawField(), ReferenceHandler.class.getName());
			return null;
		}
		final Serializer<?> serializer = handler.readFromReference(operation, ref);
		return serializer.serialize(ref, adapters);
	}

	public static void readRefFromAdapter(final Operation operation, final AdapterFactory adapters, final Reference ref, final BaseAdapter adapter) {
		final ReferenceHandler handler = XDataRegister.getHandler(ref.getKey().getRawType());
		if (handler == null) {
			//TODO logging
			System.out.printf("Cannot deserialize field %s because no matching %s was registered%n", ref.getKey().getRawField(), ReferenceHandler.class.getName());
			return;
		}
		final Serializer<?> serializer = XDataRegister.getSerializerByHandler(handler);
		serializer.deserialize(ref, adapters, adapter);
		handler.writeToReference(operation, ref, serializer);
	}

	public static void writeRefToNetwork(final Operation operation, final NetworkOutput output, final Reference ref) {
		final ReferenceHandler handler = XDataRegister.getHandler(ref.getKey().getRawType());
		if (handler == null) {
			//TODO logging
			System.out.printf("Cannot serialize field %s because no matching %s was registered%n", ref.getKey().getRawField(), ReferenceHandler.class.getName());
			return;
		}
		final Serializer<?> serializer = handler.readFromReference(operation, ref);
		serializer.toNetwork(ref, output);
	}

	public static void readRefFromNetwork(final Operation operation, final NetworkInput input, final Reference ref) {
		final ReferenceHandler handler = XDataRegister.getHandler(ref.getKey().getRawType());
		if (handler == null) {
			//TODO logging
			System.out.printf("Cannot deserialize field %s because no matching %s was registered%n", ref.getKey().getRawField(), ReferenceHandler.class.getName());
			return;
		}
		final Serializer<?> serializer = handler.readFromReference(operation, ref);
		serializer.fromNetwork(ref, input);
	}
}
