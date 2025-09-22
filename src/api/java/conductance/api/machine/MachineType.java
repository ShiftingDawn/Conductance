package conductance.api.machine;

import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import conductance.api.CAPI;

public interface MachineType<T extends MachineBlockEntity<T>> {

	Supplier<MachineBlock<T>> getBlock();

	Supplier<MachineBlockItem<T>> getItem();

	Supplier<BlockEntityType<T>> getBlockEntityType();

	String getDescriptionId();

	Component getName();

	default ResourceLocation getId() {
		return Objects.requireNonNull(CAPI.regs().machines().getKey(this), "Unregistered machine type");
	}
}
