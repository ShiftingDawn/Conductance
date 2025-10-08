package conductance.api.machine.multi;

import java.util.SortedSet;
import java.util.TreeSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import lombok.Getter;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineType;

public abstract class MultiPartMachineBlockEntity<T extends MultiPartMachineBlockEntity<T>> extends MachineBlockEntity<T> implements IMultiBlockPart {

	private final @Getter SortedSet<BlockPos> controllers = new TreeSet<>();

	public MultiPartMachineBlockEntity(final MachineType<T> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
	}

	@Override
	public void setConnectedTo(final BlockPos controllerPos, final boolean connect) {
		if (connect == this.controllers.contains(controllerPos)) {
			return;
		}
		if (connect) {
			this.controllers.add(controllerPos);
		} else {
			this.controllers.remove(controllerPos);
		}
		this.setChanged();
		this.syncToClient();
	}
}
