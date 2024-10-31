package conductance.core.datahandlers;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import conductance.api.machine.data.handler.InstancedField;
import conductance.api.machine.data.handler.ManagedObjectValueHandler;
import conductance.api.machine.data.serializer.DataSerializer;
import conductance.api.machine.data.serializer.StringSerializer;

public final class ResourceLocationValueHandler extends ManagedObjectValueHandler {

	public ResourceLocationValueHandler() {
		super(ResourceLocation.class, true);
	}

	@Override
	public DataSerializer<?> readFromField(final InstancedField field, final HolderLookup.Provider registries) {
		return StringSerializer.of(field.get().toString());
	}

	@Override
	public void writeToField(final InstancedField field, final DataSerializer<?> value, final HolderLookup.Provider registries) {
		final StringSerializer stringSerializer = this.testSerializer(value, StringSerializer.class);
		field.set(ResourceLocation.parse(stringSerializer.getData()));
	}
}
