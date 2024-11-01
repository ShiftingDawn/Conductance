package conductance.core.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.data.handler.InstancedField;
import conductance.api.machine.data.handler.ManagedFieldValueHandler;
import conductance.api.machine.data.serializer.DataSerializer;

final class Helper {

	static DataSerializer<?> getSerializer(final ManagedFieldValueHandler handler) {
		final int id = ManagedFieldValueHandlerRegistry.INSTANCE.getSerializerForHandler(handler);
		return ManagedFieldValueHandlerRegistry.INSTANCE.createSerializer(id);
	}

	@Nullable
	static Tag serializeField(final InstancedField field, final HolderLookup.Provider registries) {
		if (field.get() == null) {
			//TODO handle null
			return null;
		}
		final ManagedFieldValueHandler handler = ManagedFieldValueHandlerRegistry.INSTANCE.getHandler(field.get().getClass());
		if (handler == null) {
			return null;
		}
		final DataSerializer<?> serializer = handler.readFromField(field, registries);
		return serializer.serialize(registries);
	}

	static void deserializeField(final InstancedField field, final Tag tag, final HolderLookup.Provider registries) {
		if (field.get() == null) {
			//TODO handle null
			return;
		}
		final ManagedFieldValueHandler handler = ManagedFieldValueHandlerRegistry.INSTANCE.getHandler(field.get().getClass());
		if (handler == null) {
			return;
		}
		final DataSerializer<?> serializer = Helper.getSerializer(handler);
		serializer.deserialize(tag, registries);
		handler.writeToField(field, serializer, registries);
	}

	private Helper() {
	}
}
