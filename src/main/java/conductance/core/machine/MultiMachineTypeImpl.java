package conductance.core.machine;

import net.minecraft.network.chat.MutableComponent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.multi.MultiBlockStructure;
import conductance.api.machine.multi.MultiMachineType;

final class MultiMachineTypeImpl<T extends MachineBlockEntity<T>> extends MachineTypeImpl<T> implements MultiMachineType<T> {

	@Getter
	@Setter(AccessLevel.PACKAGE)
	private MultiBlockStructure structure;

	MultiMachineTypeImpl(final String descriptionId, final MutableComponent name) {
		super(descriptionId, name);
	}
}
