package conductance.api.machine.multi;

import java.util.function.Supplier;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineType;

public interface MultiMachineType<T extends MachineBlockEntity<T>> extends MachineType<T> {

	MultiBlockStructure getStructure();

	@Nullable Supplier<BlockState> getCasingAppearance();
}
