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
		final DataSerializer<?> serializer = field.getHandler().readFromField(field);
		return serializer.serialize(registries);
	}

	static void deserializeField(final InstancedField field, final Tag tag, final HolderLookup.Provider registries) {
		final DataSerializer<?> serializer = Helper.getSerializer(field.getHandler());
		serializer.deserialize(tag, registries);
		field.getHandler().writeToField(field, serializer);
	}

	private Helper() {
	}
}
