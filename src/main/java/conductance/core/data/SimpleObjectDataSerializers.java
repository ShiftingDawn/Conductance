package conductance.core.data;

import java.util.UUID;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.data.DataSerializer;

abstract class SimpleObjectDataSerializers<T> implements DataSerializer<T> {

	private final Class<T> typeClass;
	private final boolean shallowHandlerCheck;

	protected SimpleObjectDataSerializers(final Class<T> typeClass, final boolean shallowHandlerCheck) {
		this.typeClass = typeClass;
		this.shallowHandlerCheck = shallowHandlerCheck;
	}

	@Override
	public boolean canHandle(final Class<?> type) {
		return this.shallowHandlerCheck ? type == this.typeClass : this.typeClass.isAssignableFrom(type);
	}

	public static final class SerializerString extends SimpleObjectDataSerializers<String> {

		SerializerString() {
			super(String.class, true);
		}

		@Override
		public Tag serialize(final String value, final HolderLookup.Provider provider) {
			return StringTag.valueOf(value);
		}

		@Override
		public @Nullable String deserialize(final Tag tag, final HolderLookup.Provider provider) {
			return tag instanceof final StringTag stringTag ? stringTag.getAsString() : null;
		}

		@Override
		public void toNetwork(final RegistryFriendlyByteBuf buf, final String value) {
			buf.writeUtf(value);
		}

		@Override
		public @Nullable String fromNetwork(final RegistryFriendlyByteBuf buf) {
			return buf.readUtf();
		}
	}

	public static final class SerializerResourceLocation extends SimpleObjectDataSerializers<ResourceLocation> {

		SerializerResourceLocation() {
			super(ResourceLocation.class, true);
		}

		@Override
		public Tag serialize(final ResourceLocation value, final HolderLookup.Provider provider) {
			return StringTag.valueOf(value.toString());
		}

		@Override
		public @Nullable ResourceLocation deserialize(final Tag tag, final HolderLookup.Provider provider) {
			return tag instanceof final StringTag stringTag ? ResourceLocation.parse(stringTag.getAsString()) : null;
		}

		@Override
		public void toNetwork(final RegistryFriendlyByteBuf buf, final ResourceLocation value) {
			buf.writeResourceLocation(value);
		}

		@Override
		public @Nullable ResourceLocation fromNetwork(final RegistryFriendlyByteBuf buf) {
			return buf.readResourceLocation();
		}
	}

	public static final class SerializerUUID extends SimpleObjectDataSerializers<UUID> {

		SerializerUUID() {
			super(UUID.class, true);
		}

		@Override
		public Tag serialize(final UUID value, final HolderLookup.Provider provider) {
			return StringTag.valueOf(value.toString());
		}

		@Override
		public @Nullable UUID deserialize(final Tag tag, final HolderLookup.Provider provider) {
			return tag instanceof final StringTag stringTag ? UUID.fromString(stringTag.getAsString()) : null;
		}

		@Override
		public void toNetwork(final RegistryFriendlyByteBuf buf, final UUID value) {
			buf.writeUUID(value);
		}

		@Override
		public @Nullable UUID fromNetwork(final RegistryFriendlyByteBuf buf) {
			return buf.readUUID();
		}
	}

	public static final class SerializerBlockState extends SimpleObjectDataSerializers<BlockState> {

		SerializerBlockState() {
			super(BlockState.class, false);
		}

		@Override
		public Tag serialize(final BlockState value, final HolderLookup.Provider provider) {
			return NbtUtils.writeBlockState(value);
		}

		@Override
		public @Nullable BlockState deserialize(final Tag tag, final HolderLookup.Provider provider) {
			if (!(tag instanceof final CompoundTag compoundTag)) {
				return null;
			}
			return NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), compoundTag);
		}

		@Override
		public void toNetwork(final RegistryFriendlyByteBuf buf, final BlockState value) {
			buf.writeNbt(NbtUtils.writeBlockState(value));
		}

		@Override
		public @Nullable BlockState fromNetwork(final RegistryFriendlyByteBuf buf) {
			return NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), buf.readNbt());
		}
	}
}
