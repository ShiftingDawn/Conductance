package conductance.api.machine.multi;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;
import lombok.Getter;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineModelProperties;
import conductance.api.machine.MachineType;

public abstract class MultiPartMachineBlockEntity<T extends MultiPartMachineBlockEntity<T>> extends MachineBlockEntity<T> implements IMultiBlockPart {

	private final @Getter SortedSet<BlockPos> controllers = new TreeSet<>();

	public MultiPartMachineBlockEntity(final MachineType<T> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
	}

	@Override
	public void setConnectedTo(final BlockPos controllerPos, final boolean connect) {
		if (connect) {
			this.controllers.add(controllerPos);
		} else {
			this.controllers.remove(controllerPos);
		}
		this.setChanged();
		this.syncToClient();
	}

	@Override
	public void onUnload() {
		super.onUnload();
		if (this.level instanceof final ServerLevel serverLevel) {
			for (final BlockPos controllerPos : this.controllers) {
				if (serverLevel.getBlockEntity(controllerPos) instanceof final IMultiBlockController<?> controller) {
					controller.removePart(this);
				}
			}
		}
	}

	@Override
	public ModelData getModelData() {
		final ModelData.Builder builder = super.getModelData().derive();
		if (this.level != null) {
			for (final BlockPos controllerPos : this.controllers) {
				if (this.level.getBlockEntity(controllerPos) instanceof final IMultiBlockController<?> controller && controller.isStructureFormed()) {
					final Supplier<BlockState> casingAppearance = controller.getMachineType().getCasingAppearance();
					if (casingAppearance == null) {
						continue;
					}
					builder.with(MachineModelProperties.APPEARANCE, casingAppearance.get());
					break;
				}
			}
		}
		return builder.build();
	}
}
