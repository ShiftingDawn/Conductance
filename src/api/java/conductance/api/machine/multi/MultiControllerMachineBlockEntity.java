package conductance.api.machine.multi;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import conductance.api.CAPI;
import conductance.api.block.BlockRotationHelper;

public class MultiControllerMachineBlockEntity<T extends MultiControllerMachineBlockEntity<T>> extends MultiMachineBlockEntity<T> implements IMultiBlockController {

	private final int structureCheckTimerOffset = CAPI.RANDOM.nextInt(100);
	private final Set<IMultiBlockPart> parts = new HashSet<>();

	public MultiControllerMachineBlockEntity(final MultiMachineType<T> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
	}

	@Override
	public void onStructureFormed(final StructureCheckContext ctx) {
		ctx.get(StructureCheckContext.PARTS).forEach(this::addPart);
	}

	@Override
	public void onStructureInvalid(final StructureCheckContext ctx) {
		new HashSet<>(this.parts).forEach(this::removePart);
	}

	@Override
	public MultiBlockStructure getStructure() {
		return this.getMachineType().getStructure();
	}

	@Override
	public MultiBlockInfo getMultiBlockInfo() {
		return new MultiBlockInfo(this.level, this.worldPosition, BlockRotationHelper.getFacing(this.getBlockState()));
	}

	@Override
	public boolean hasPart(final IMultiBlockPart part) {
		return this.parts.contains(part);
	}

	@Override
	public void addPart(final IMultiBlockPart part) {
		if (this.parts.add(part)) {
			part.setConnectedTo(this.getBlockPos(), true);
		}
	}

	@Override
	public void removePart(final IMultiBlockPart part) {
		if (this.parts.remove(part)) {
			part.setConnectedTo(this.getBlockPos(), false);
		}
	}

	public static boolean checkStructure(final IMultiBlockController controller, final boolean forceCheck) {
		if (!forceCheck && controller instanceof final MultiControllerMachineBlockEntity<?> blockEntity) {
			if (blockEntity.level != null && blockEntity.level.getGameTime() % blockEntity.structureCheckTimerOffset != 0) {
				return true;
			}
		}
		final StructureCheckContext ctx = new StructureCheckContext();
		if (controller.checkStructure(ctx)) {
			controller.onStructureFormed(ctx);
			return true;
		} else {
			controller.onStructureInvalid(ctx);
			return false;
		}
	}
}
