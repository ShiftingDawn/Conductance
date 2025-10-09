package conductance.api.machine.multi;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import lombok.Getter;
import conductance.api.CAPI;
import conductance.api.block.BlockRotationHelper;
import conductance.api.util.Internal;

public class MultiControllerMachineBlockEntity<T extends MultiControllerMachineBlockEntity<T>> extends MultiMachineBlockEntity<T> implements IMultiBlockController<T> {

	public static final int REQUEST_STRUCTURE_FORMED = 1;
	public static final int REQUEST_STRUCTURE_INVALID = 2;
	private final int structureCheckTimerOffset = CAPI.RANDOM.nextInt(100);
	private final @Getter Set<IMultiBlockPart> parts = new HashSet<>();
	private @Getter boolean structureFormed = false;

	public MultiControllerMachineBlockEntity(final MultiMachineType<T> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
	}

	@Override
	public void onLoad() {
		MultiControllerMachineBlockEntity.checkStructure(this, true);
		if (this.isStructureFormed()) {
			super.onLoad();
		}
		if (this.level instanceof final ServerLevel serverLevel) {
			Internal.MULTIBLOCK_CONTROLLER_LOAD.accept(serverLevel, this);
		}
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		if (this.level instanceof final ServerLevel serverLevel) {
			Internal.MULTIBLOCK_CONTROLLER_UNLOAD.accept(serverLevel, this);
		}
	}

	@Override
	public void onStructureFormed(final StructureCheckContext ctx) {
		if (this.structureFormed) {
			return;
		}
		this.structureFormed = true;
		this.onLoad();
		ctx.get(StructureCheckContext.PARTS).forEach(this::addPart);
		this.sendToClient(MultiControllerMachineBlockEntity.REQUEST_STRUCTURE_FORMED, output -> {
			final ValueOutput.TypedOutputList<BlockPos> list = output.list("parts", BlockPos.CODEC);
			for (final IMultiBlockPart part : this.parts) {
				if (part instanceof final BlockEntity blockEntity) {
					list.add(blockEntity.getBlockPos());
				}
			}
		});
	}

	@Override
	public void onStructureInvalid(final StructureCheckContext ctx) {
		if (!this.structureFormed) {
			return;
		}
		this.structureFormed = false;
		this.onUnload();
		this.removeAllParts();
		this.sendToClient(MultiControllerMachineBlockEntity.REQUEST_STRUCTURE_INVALID, null);
	}

	private void removeAllParts() {
		new HashSet<>(this.parts).forEach(this::removePart);
	}

	@Override
	protected void handleServerRequest(final int requestId, final ValueInput input) {
		switch (requestId) {
			case MultiControllerMachineBlockEntity.REQUEST_STRUCTURE_FORMED -> this.onClient(level -> input.list("parts", BlockPos.CODEC).ifPresent(list -> {
				for (final BlockPos partPos : list) {
					if (level.getBlockEntity(partPos) instanceof final IMultiBlockPart part) {
						this.addPart(part);
					}
				}
			}));
			case MultiControllerMachineBlockEntity.REQUEST_STRUCTURE_INVALID -> {
				this.removeAllParts();
				this.syncToClient();
			}
		}
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

	public static boolean checkStructure(final IMultiBlockController<?> controller, final boolean forceCheck) {
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
