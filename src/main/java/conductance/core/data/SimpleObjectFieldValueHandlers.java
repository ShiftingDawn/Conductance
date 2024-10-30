package conductance.core.data;

import java.util.UUID;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.ManagedObjectValueHandler;

final class SimpleObjectFieldValueHandlers {

	private SimpleObjectFieldValueHandlers() {
	}

	public static class HandlerString extends ManagedObjectValueHandler<String> {

		HandlerString() {
			super(String.class, true);
		}

		@Override
		public Tag serialize(final String value) {
			return StringTag.valueOf(value);
		}

		@Override
		public @Nullable String deserialize(final Tag nbt) {
			return nbt.getAsString();
		}
	}

	public static class HandlerResourceLocation extends ManagedObjectValueHandler<ResourceLocation> {

		HandlerResourceLocation() {
			super(ResourceLocation.class, true);
		}

		@Override
		public Tag serialize(final ResourceLocation value) {
			return StringTag.valueOf(value.toString());
		}

		@Override
		public @Nullable ResourceLocation deserialize(final Tag nbt) {
			return ResourceLocation.parse(nbt.getAsString());
		}
	}

	public static class HandlerUUID extends ManagedObjectValueHandler<UUID> {

		HandlerUUID() {
			super(UUID.class, true);
		}

		@Override
		public Tag serialize(final UUID value) {
			return StringTag.valueOf(value.toString());
		}

		@Override
		public @Nullable UUID deserialize(final Tag nbt) {
			return UUID.fromString(nbt.getAsString());
		}
	}

	public static class HandlerBlockState extends ManagedObjectValueHandler<BlockState> {

		HandlerBlockState() {
			super(BlockState.class, false);
		}

		@Override
		public Tag serialize(final BlockState value) {
			return NbtUtils.writeBlockState(value);
		}

		@Override
		public @Nullable BlockState deserialize(final Tag nbt) {
			return NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), (CompoundTag) nbt);
		}
	}
}
