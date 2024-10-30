package conductance.core.data;

import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.data.DataSerializer;

final class EnumSerializer implements DataSerializer<Enum<?>> {

	@Override
	public boolean canHandle(final Class<?> type) {
		return type.isEnum();
	}

	@Override
	public Tag serialize(final Enum<?> value, final HolderLookup.Provider provider) {
		return Util.make(new CompoundTag(), nbt -> {
			nbt.putString("c", value.getDeclaringClass().getName());
			nbt.putInt("o", value.ordinal());
		});
	}

	@Override
	public @Nullable Enum<?> deserialize(final Tag tag, final HolderLookup.Provider provider) {
		if (!(tag instanceof final CompoundTag compoundTag)) {
			return null;
		}
		final String className = compoundTag.getString("c");
		try {
			final Class<?> clazz = Class.forName(className);
			return (Enum<?>) clazz.getEnumConstants()[compoundTag.getInt("o")];
		} catch (final ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void toNetwork(final RegistryFriendlyByteBuf buf, final Enum<?> value) {
		buf.writeUtf(value.getDeclaringClass().getName());
		buf.writeEnum(value);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public @Nullable Enum<?> fromNetwork(final RegistryFriendlyByteBuf buf) {
		try {
			return (Enum<?>) buf.readEnum((Class) Class.forName(buf.readUtf()));
		} catch (final ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
