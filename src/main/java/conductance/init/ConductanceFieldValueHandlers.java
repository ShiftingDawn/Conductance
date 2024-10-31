package conductance.init;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import conductance.api.machine.data.handler.BuiltInRegistryValueHandler;
import conductance.api.machine.data.serializer.ArraySerializer;
import conductance.api.machine.data.serializer.BlockPosSerializer;
import conductance.api.machine.data.serializer.FluidStackSerializer;
import conductance.api.machine.data.serializer.ItemStackSerializer;
import conductance.api.machine.data.serializer.NbtDataSerializer;
import conductance.api.machine.data.serializer.PrimitiveDataSerializer;
import conductance.api.machine.data.serializer.StringSerializer;
import conductance.api.machine.data.serializer.UuidSerializer;
import conductance.api.plugin.ManagedFieldValueHandlerRegister;
import conductance.core.datahandlers.BlockStateValueHandler;
import conductance.core.datahandlers.EnumValueHandler;
import conductance.core.datahandlers.NbtSerializableValueHandler;
import conductance.core.datahandlers.PrimitiveValueHandler;
import conductance.core.datahandlers.ResourceLocationValueHandler;

public final class ConductanceFieldValueHandlers {

	public static void init(final ManagedFieldValueHandlerRegister register) {
		register.register(PrimitiveDataSerializer.BooleanSerializer.class, PrimitiveDataSerializer.BooleanSerializer::new, new PrimitiveValueHandler.BooleanHandler());
		register.register(PrimitiveDataSerializer.ByteSerializer.class, PrimitiveDataSerializer.ByteSerializer::new, new PrimitiveValueHandler.ByteHandler());
		register.register(PrimitiveDataSerializer.ShortSerializer.class, PrimitiveDataSerializer.ShortSerializer::new, new PrimitiveValueHandler.ShortHandler());
		register.register(PrimitiveDataSerializer.IntegerSerializer.class, PrimitiveDataSerializer.IntegerSerializer::new, new PrimitiveValueHandler.IntegerHandler());
		register.register(PrimitiveDataSerializer.LongSerializer.class, PrimitiveDataSerializer.LongSerializer::new, new PrimitiveValueHandler.LongHandler());
		register.register(PrimitiveDataSerializer.FloatSerializer.class, PrimitiveDataSerializer.FloatSerializer::new, new PrimitiveValueHandler.FloatHandler());
		register.register(PrimitiveDataSerializer.DoubleSerializer.class, PrimitiveDataSerializer.DoubleSerializer::new, new PrimitiveValueHandler.DoubleHandler());
		register.register(PrimitiveDataSerializer.CharacterSerializer.class, PrimitiveDataSerializer.CharacterSerializer::new, new PrimitiveValueHandler.CharacterHandler());

		register.registerSerializer(ArraySerializer.class, ArraySerializer::new);

		register.register(StringSerializer.class, StringSerializer::new, String.class, true);
		register.register(UuidSerializer.class, UuidSerializer::new, UUID.class, true);
		register.register(NbtDataSerializer.class, NbtDataSerializer::new, Tag.class, false);
		register.register(ItemStackSerializer.class, ItemStackSerializer::new, ItemStack.class, true);
		register.register(FluidStackSerializer.class, FluidStackSerializer::new, FluidStack.class, true);
		register.register(BlockPosSerializer.class, BlockPosSerializer::new, BlockPos.class, false);

		register.register(NbtDataSerializer.class, NbtDataSerializer::new, new NbtSerializableValueHandler(), -1000);
		register.register(StringSerializer.class, StringSerializer::new, new EnumValueHandler());
		register.register(NbtDataSerializer.class, NbtDataSerializer::new, new BlockStateValueHandler());
		register.register(StringSerializer.class, StringSerializer::new, new ResourceLocationValueHandler());

		register.register(StringSerializer.class, StringSerializer::new, new BuiltInRegistryValueHandler<>(Block.class, BuiltInRegistries.BLOCK));
		register.register(StringSerializer.class, StringSerializer::new, new BuiltInRegistryValueHandler<>(Item.class, BuiltInRegistries.ITEM));
		register.register(StringSerializer.class, StringSerializer::new, new BuiltInRegistryValueHandler<>(Fluid.class, BuiltInRegistries.FLUID));
	}

	private ConductanceFieldValueHandlers() {
	}
}
