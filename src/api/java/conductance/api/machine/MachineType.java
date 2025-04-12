package conductance.api.machine;

import net.minecraft.world.level.block.entity.BlockEntityType;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.gui.MachineGuiSupplier;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.registry.IRegistryObject;

public interface MachineType<T extends MachineBlockEntity<T>> extends IRegistryObject<String> {

	NCRecipeType[] getRecipeTypes();

	NonNullSupplier<? extends MachineBlock<T>> getBlock();

	NonNullSupplier<BlockEntityType<T>> getBlockEntityType();

	@Nullable
	MachineGuiSupplier getGuiSupplier();
}
