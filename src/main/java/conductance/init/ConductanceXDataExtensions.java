package conductance.init;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import conductance.api.machine.xdata.BlockPosSerializer;
import conductance.api.machine.xdata.BlockStateHandler;
import conductance.api.machine.xdata.BuiltInRegistryHandler;
import conductance.api.machine.xdata.FluidStackSerializer;
import conductance.api.machine.xdata.ItemStackSerializer;
import conductance.api.machine.xdata.NbtSerializableHandler;
import conductance.api.machine.xdata.NbtSerializer;
import conductance.api.machine.xdata.ResourceLocationSerializer;
import static nl.appelgebakje22.xdata.XDataRegister.register;

public final class ConductanceXDataExtensions {

	public static void init() {

		register(NbtSerializer.class, NbtSerializer::new, Tag.class, false);
		register(ItemStackSerializer.class, ItemStackSerializer::new, ItemStack.class, true);
		register(FluidStackSerializer.class, FluidStackSerializer::new, FluidStack.class, true);
		register(BlockPosSerializer.class, BlockPosSerializer::new, BlockPos.class, false);

		register(NbtSerializer.class, NbtSerializer::new, new NbtSerializableHandler(), -1000);
		register(NbtSerializer.class, NbtSerializer::new, new BlockStateHandler());
		register(ResourceLocationSerializer.class, ResourceLocationSerializer::new, ResourceLocation.class, true);

		register(ResourceLocationSerializer.class, ResourceLocationSerializer::new, new BuiltInRegistryHandler<>(Block.class, BuiltInRegistries.BLOCK));
		register(ResourceLocationSerializer.class, ResourceLocationSerializer::new, new BuiltInRegistryHandler<>(Item.class, BuiltInRegistries.ITEM));
		register(ResourceLocationSerializer.class, ResourceLocationSerializer::new, new BuiltInRegistryHandler<>(Fluid.class, BuiltInRegistries.FLUID));
	}

	private ConductanceXDataExtensions() {
	}
}
