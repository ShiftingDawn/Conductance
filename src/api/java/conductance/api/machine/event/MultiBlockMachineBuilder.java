package conductance.api.machine.event;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.world.level.block.state.BlockState;
import conductance.api.machine.multi.MultiBlockStructureBuilder;
import conductance.api.machine.multi.MultiMachineBlockEntity;

public interface MultiBlockMachineBuilder<T extends MultiMachineBlockEntity<T>> extends AbstractMachineBuilder<T, MultiBlockMachineBuilder<T>> {

	MultiBlockMachineBuilder<T> structure(char controllerChar, Consumer<MultiBlockStructureBuilder> builder);

	MultiBlockMachineBuilder<T> casingAppearance(Supplier<BlockState> appearance);
}
