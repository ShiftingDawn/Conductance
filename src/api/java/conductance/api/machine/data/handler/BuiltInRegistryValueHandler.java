package conductance.api.machine.data.handler;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import conductance.api.machine.data.serializer.DataSerializer;
import conductance.api.machine.data.serializer.StringSerializer;

@SuppressWarnings("unchecked")
public final class BuiltInRegistryValueHandler<T> extends ManagedObjectValueHandler {

	private final Registry<T> registry;

	public BuiltInRegistryValueHandler(final Class<T> typeClass, final Registry<T> registry) {
		super(typeClass, false);
		this.registry = registry;
	}

	@Override
	public DataSerializer<?> readFromField(final InstancedField field, final HolderLookup.Provider registries) {
		return StringSerializer.of(this.registry.getKey((T) field.get()).toString());
	}

	@Override
	public void writeToField(final InstancedField field, final DataSerializer<?> value, final HolderLookup.Provider registries) {
		final StringSerializer stringSerializer = this.testSerializer(value, StringSerializer.class);
		final ResourceLocation id = ResourceLocation.parse(stringSerializer.getData());
		field.set(this.registry.get(id));
	}
}
