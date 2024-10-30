package conductance.core.data;

import java.lang.reflect.Field;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.data.ManagedFieldValueHandler;

final class NbtSerializableValueHandler implements ManagedFieldValueHandler<INBTSerializable<?>> {

	NbtSerializableValueHandler() {
	}

	@Override
	public boolean canHandle(final Class<?> type) {
		return false;
	}

	@Override
	public @Nullable INBTSerializable<?> getValue(final Field field, final Object instance) {
		return null;
	}

	@Override
	public void setValue(final Field field, final Object instance, @Nullable final INBTSerializable<?> value) {

	}

	@Override
	public boolean equals(final INBTSerializable<?> value1, final INBTSerializable<?> value2) {
		return false;
	}

	@Override
	public Tag serialize(final INBTSerializable<?> value, final HolderLookup.Provider registries) {
		return null;
	}

	@Override
	public @Nullable INBTSerializable<?> deserialize(final Tag nbt, final HolderLookup.Provider registries) {
		return null;
	}
}
