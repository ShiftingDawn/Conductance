package conductance.core.sync.handlers;

import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.sync.Operation;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.ReferenceHandler;
import conductance.api.machine.sync.Serializer;
import conductance.core.sync.serializers.PrimitiveCodecSerializer;

public class EnumHandler implements ReferenceHandler {

	@Override
	public boolean canHandle(final Class<?> clazz) {
		return clazz.isEnum();
	}

	@Override
	public Serializer<?> readFromReference(final Operation operation, final Reference ref, final HolderLookup.Provider registries) {
		final Object data = ref.getValueHolder().get();
		return Util.make(new PrimitiveCodecSerializer.StringSerializer(), s -> s.setData(((Enum<?>) data).name()));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void writeToReference(final Operation operation, final Reference ref, final Serializer<?> rawSerializer, final HolderLookup.Provider registries) {
		final PrimitiveCodecSerializer.StringSerializer serializer = this.testSerializer(rawSerializer, PrimitiveCodecSerializer.StringSerializer.class);
		final Enum<?> newValue = EnumHandler.getEnum((Class<Enum>) ref.getKey().getRawField().getType(), serializer.getData());
		ref.getValueHolder().set(newValue);
	}

	@Nullable
	private static <T extends Enum<T>> T getEnum(final Class<T> type, final String name) {
		for (final T constant : type.getEnumConstants()) {
			if (constant.name().equals(name)) {
				return constant;
			}
		}
		return null;
	}
}
