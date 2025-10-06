package conductance.api.machine.event;

import java.util.function.Consumer;
import conductance.api.machine.multi.MultiBlockStructureBuilder;
import conductance.api.machine.multi.MultiMachineBlockEntity;

public interface MultiBlockMachineBuilder<T extends MultiMachineBlockEntity<T>> extends AbstractMachineBuilder<T, MultiBlockMachineBuilder<T>> {

	MultiBlockMachineBuilder<T> structure(char controllerChar, Consumer<MultiBlockStructureBuilder> builder);
}
