package nl.appelgebakje22.xdata.api;

import lombok.Getter;
import lombok.Setter;
import nl.appelgebakje22.xdata.XDataRegister;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.NetworkInput;
import nl.appelgebakje22.xdata.adapter.NetworkOutput;
import nl.appelgebakje22.xdata.ref.Reference;
import org.jetbrains.annotations.Nullable;

public abstract class Serializer<T> {

	@Getter
	@Setter
	private T data;

	@Nullable
	public abstract BaseAdapter serialize(Reference ref, AdapterFactory adapters);

	public abstract void deserialize(Reference ref, AdapterFactory adapters, BaseAdapter adapter);

	public abstract void toNetwork(Reference ref, NetworkOutput output);

	public abstract void fromNetwork(Reference ref, NetworkInput input);

	protected <A extends BaseAdapter> A testAdapter(final BaseAdapter adapter, final Class<A> expectedType) {
		if (expectedType.isAssignableFrom(adapter.getClass())) {
			return expectedType.cast(adapter);
		}
		throw new IllegalArgumentException("Expected Adapter of type %s, got: %s".formatted(expectedType.getName(), adapter.getClass().getName()));
	}

	public final int getSid() {
		return XDataRegister.getSerializerId(this);
	}
}
