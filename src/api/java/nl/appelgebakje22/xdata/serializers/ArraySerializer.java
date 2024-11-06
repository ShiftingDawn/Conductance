package nl.appelgebakje22.xdata.serializers;

import java.util.Collection;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.XDataRegister;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.ArrayAdapter;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.NetworkInput;
import nl.appelgebakje22.xdata.adapter.NetworkOutput;
import nl.appelgebakje22.xdata.adapter.TableAdapter;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.ArrayReference;
import nl.appelgebakje22.xdata.ref.CollectionReference;
import nl.appelgebakje22.xdata.ref.Reference;
import org.jetbrains.annotations.Nullable;

public class ArraySerializer extends Serializer<Serializer<?>[]> {

	@Override
	public @Nullable BaseAdapter serialize(final Reference ref, final AdapterFactory adapters) {
		final ArrayAdapter array = adapters.array();
		final Reference[] arrayRefs = ArraySerializer.makeRefs(ref, this.getData().length);
		for (int i = 0; i < this.getData().length; ++i) {
			final Serializer<?> serializer = this.getData()[i];
			final TableAdapter entry = array.addTable();
			entry.set("sid", serializer.getSid());
			final BaseAdapter dataAdapter = serializer.serialize(arrayRefs[i], adapters);
			if (dataAdapter != null) {
				entry.set("dat", dataAdapter);
			}
		}
		return array;
	}

	@Override
	public void deserialize(final Reference ref, final AdapterFactory adapters, final BaseAdapter adapter) {
		final ArrayAdapter array = this.testAdapter(adapter, ArrayAdapter.class);
		final Serializer<?>[] arr = new Serializer[array.size()];
		final Reference[] arrayRefs = ArraySerializer.makeRefs(ref, arr.length);
		for (int i = 0; i < arr.length; ++i) {
			final TableAdapter entry = this.testAdapter(array.get(i), TableAdapter.class);
			arr[i] = XDataRegister.getSerializerById(entry.getInt("sid"));
			if (arr[i] == null) {
				throw new IllegalStateException("Could not create %s with id %s".formatted(Serializer.class.getName(), entry.getInt("sid")));
			}
			final BaseAdapter dataAdapter = entry.get("dat");
			if (dataAdapter != null) {
				arr[i].deserialize(arrayRefs[i], adapters, dataAdapter);
			}
		}
		this.setData(arr);
	}

	@Override
	public void toNetwork(final Reference ref, final NetworkOutput output) {
		final Reference[] arrayRefs = ArraySerializer.makeRefs(ref, this.getData().length);
		output.write(this.getData().length);
		for (int i = 0; i < this.getData().length; ++i) {
			final Serializer<?> serializer = this.getData()[i];
			output.write(serializer.getSid());
			serializer.toNetwork(arrayRefs[i], output);
		}
	}

	@Override
	public void fromNetwork(final Reference ref, final NetworkInput input) {
		final Serializer<?>[] arr = new Serializer[input.readInt()];
		final Reference[] arrayRefs = ArraySerializer.makeRefs(ref, arr.length);
		for (int i = 0; i < arr.length; ++i) {
			final int sid = input.readInt();
			arr[i] = XDataRegister.getSerializerById(sid);
			if (arr[i] == null) {
				throw new IllegalStateException("Could not create %s with id %s".formatted(Serializer.class.getName(), sid));
			}
			arr[i].fromNetwork(arrayRefs[i], input);
		}
		this.setData(arr);
	}

	private static Reference[] makeRefs(final Reference arrayRef, final int length) {
		final Class<?> fieldType = arrayRef.getKey().getRawField().getType();
		if (fieldType.isArray()) {
			final Object array = arrayRef.getValueHolder().get();
			final Class<?> arrayType = fieldType.getComponentType();
			final Reference[] result = new Reference[length];
			for (int i = 0; i < result.length; ++i) {
				result[i] = ArrayReference.of(arrayRef.getKey(), array, i, arrayType);
			}
			return result;
		} else if (Collection.class.isAssignableFrom(fieldType)) {
			final Collection<?> collection = (Collection<?>) arrayRef.getValueHolder().get();
			final Reference[] result = new Reference[length];
			for (int i = 0; i < result.length; ++i) {
				result[i] = CollectionReference.of(arrayRef.getKey(), collection, i);
			}
			return result;
		} else {
			throw new IllegalStateException("Could not create %ss for unknown array field-type %s".formatted(Reference.class.getName(), fieldType));
		}
	}

	public static ArraySerializer of(final Serializer<?>[] data) {
		return XData.make(new ArraySerializer(), serializer -> serializer.setData(data));
	}
}
