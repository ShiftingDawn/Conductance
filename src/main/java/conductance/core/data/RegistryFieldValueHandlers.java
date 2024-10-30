package conductance.core.data;

import java.lang.reflect.Field;
import java.util.Objects;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.data.ManagedFieldValueHandler;

abstract class RegistryFieldValueHandlers<T> implements ManagedFieldValueHandler<T> {

	private final Registry<T> registry;

	private RegistryFieldValueHandlers(final Registry<T> registry) {
		this.registry = registry;
	}

	@SuppressWarnings("unchecked")
	@Override
	public @Nullable T getValue(final Field field, final Object instance) {
		try {
			return (T) field.get(instance);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setValue(final Field field, final Object instance, @Nullable final T value) {
		try {
			field.set(instance, value);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean equals(final T value1, final T value2) {
		return Objects.equals(this.registry.getKey(value1), this.registry.getKey(value2));
	}

	@Override
	public Tag serialize(final T value) {
		return StringTag.valueOf(this.registry.getKey(value).toString());
	}

	@Override
	public @Nullable T deserialize(final Tag nbt) {
		return this.registry.get(ResourceLocation.parse(nbt.getAsString()));
	}

	public static class HandlerBlock extends RegistryFieldValueHandlers<Block> {

		HandlerBlock() {
			super(BuiltInRegistries.BLOCK);
		}

		@Override
		public boolean canHandle(final Class<?> type) {
			return Block.class.isAssignableFrom(type);
		}
	}

	public static class HandlerItem extends RegistryFieldValueHandlers<Item> {

		HandlerItem() {
			super(BuiltInRegistries.ITEM);
		}

		@Override
		public boolean canHandle(final Class<?> type) {
			return Item.class.isAssignableFrom(type);
		}
	}

	public static class HandlerFluid extends RegistryFieldValueHandlers<Fluid> {

		HandlerFluid() {
			super(BuiltInRegistries.FLUID);
		}

		@Override
		public boolean canHandle(final Class<?> type) {
			return Fluid.class.isAssignableFrom(type);
		}
	}
}
