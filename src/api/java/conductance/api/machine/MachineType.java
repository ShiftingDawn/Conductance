package conductance.api.machine;

import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.gui.GuiSetup;
import conductance.api.recipe.MachineRecipeType;

public interface MachineType<T extends MachineBlockEntity<T>> {

	Supplier<MachineBlock<T>> getBlock();

	Supplier<MachineBlockItem<T>> getItem();

	Supplier<BlockEntityType<T>> getBlockEntityType();

	MachineRecipeType[] getRecipeTypes();

	@Nullable GuiSetup getGuiSetup();

	String getDescriptionId();

	Component getName();

	default ResourceLocation getId() {
		return Objects.requireNonNull(CAPI.regs().machines().getKey(this), "Unregistered machine type");
	}
}
