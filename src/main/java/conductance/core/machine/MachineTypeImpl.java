package conductance.core.machine;

import java.util.function.Supplier;
import net.minecraft.world.level.block.entity.BlockEntityType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import conductance.api.machine.MachineBlock;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineBlockItem;
import conductance.api.machine.MachineType;

final class MachineTypeImpl<T extends MachineBlockEntity<T>> implements MachineType<T> {

	@Getter
	@Setter(AccessLevel.PACKAGE)
	private Supplier<MachineBlock<T>> block;
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private Supplier<MachineBlockItem<T>> item;

	@Getter
	@Setter(AccessLevel.PACKAGE)
	private Supplier<BlockEntityType<T>> blockEntityType;
}
