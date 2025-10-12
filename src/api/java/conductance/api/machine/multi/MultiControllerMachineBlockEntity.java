package conductance.api.machine.multi;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import lombok.Getter;
import conductance.api.CAPI;
import conductance.api.NCBlockStateProperties;
import conductance.api.block.BlockRotationHelper;
import conductance.api.block.FacingAndRotation;
import conductance.api.util.Internal;

public class MultiControllerMachineBlockEntity<T extends MultiControllerMachineBlockEntity<T>> extends MultiMachineBlockEntity<T> implements IMultiBlockController<T> {

	public static final int REQUEST_STRUCTURE_FORMED = 1;
	public static final int REQUEST_STRUCTURE_INVALID = 2;
	private final int structureCheckTimerOffset = CAPI.RANDOM.nextInt(60);
	private final @Getter Set<IMultiBlockPart> parts = new HashSet<>();
	private final Set<BlockPos> activeBlocks = new HashSet<>();
	private final @Getter Lock structureCheckLock = new ReentrantLock();
	private @Getter boolean structureFormed = false;

	public MultiControllerMachineBlockEntity(final MultiMachineType<T> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
	}

	@Override
	public void onLoad() {
		super.onLoad();
		if (this.level instanceof final ServerLevel serverLevel) {
			Internal.MULTIBLOCK_CONTROLLER_LOAD.accept(serverLevel, this);
		}
	}

	@Override
	public void onUnload() {
		super.onUnload();
		if (this.level instanceof final ServerLevel serverLevel) {
			Internal.MULTIBLOCK_CONTROLLER_UNLOAD.accept(serverLevel, this);
		}
	}

	@Override
	public void onStructureFormed(final StructureCheckContext ctx) {
		this.structureFormed = true;
		this.parts.clear();
		ctx.get(StructureCheckContext.PARTS).forEach(this::addPart);
		this.activeBlocks.addAll(ctx.get(StructureCheckContext.ACTIVE_BLOCKS));
		this.setWorkingState(this.isCurrentlyWorking());
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
		this.structureFormed = false;
		this.invalidateController(false);
		this.sendToClient(MultiControllerMachineBlockEntity.REQUEST_STRUCTURE_INVALID, null);
	}

	@Override
	public void preRemoveSideEffects(final BlockPos pos, final BlockState state) {
		super.preRemoveSideEffects(pos, state);
		this.invalidateController(true);
	}

	private void invalidateController(final boolean isRemoved) {
		this.removeAllParts();
		if (isRemoved) {
			this.setActiveBlocks(false);
		} else {
			this.setWorkingState(false);
		}
		this.activeBlocks.clear();
	}

	private void removeAllParts() {
		new HashSet<>(this.parts).forEach(this::removePart);
	}

	@Override
	public void setWorkingState(final boolean working) {
		super.setWorkingState(working);
		this.setActiveBlocks(working);
	}

	private void setActiveBlocks(final boolean active) {
		for (final BlockPos activeBlockPos : this.activeBlocks) {
			final BlockState state = this.level.getBlockState(activeBlockPos);
			if (state.hasProperty(NCBlockStateProperties.ACTIVE)) {
				this.level.setBlockAndUpdate(activeBlockPos, state.setValue(NCBlockStateProperties.ACTIVE, active));
			}
		}
	}

	@Override
	protected void handleServerRequest(final int requestId, final ValueInput input) {
		switch (requestId) {
			case MultiControllerMachineBlockEntity.REQUEST_STRUCTURE_FORMED -> {
				this.structureFormed = true;
				this.onClient(level -> input.list("parts", BlockPos.CODEC).ifPresent(list -> {
					for (final BlockPos partPos : list) {
						if (level.getBlockEntity(partPos) instanceof final IMultiBlockPart part) {
							this.addPart(part);
						}
					}
				}));
			}
			case MultiControllerMachineBlockEntity.REQUEST_STRUCTURE_INVALID -> {
				this.structureFormed = false;
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
		final FacingAndRotation facingAndRotation = BlockRotationHelper.getExtendedRotation(this.getBlockState());
		return new MultiBlockInfo(this.level, this.worldPosition, facingAndRotation.getFacing(), facingAndRotation.getRotation());
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

	public static void checkStructure(final IMultiBlockController<?> controller, final boolean forceCheck) {
		if (!forceCheck && controller instanceof final MultiControllerMachineBlockEntity<?> blockEntity) {
			if (blockEntity.level != null && blockEntity.level.getGameTime() % blockEntity.structureCheckTimerOffset != 0) {
				return;
			}
		}
		final StructureCheckContext ctx = new StructureCheckContext();
		if (controller.checkStructureAsyncLocked(ctx)) {
			if (controller.getLevel() instanceof final ServerLevel serverLevel) {
				serverLevel.getServer().execute(() -> {
					final Lock lock = controller.getStructureCheckLock();
					lock.lock();
					final StructureCheckContext ctx2 = new StructureCheckContext();
					try {
						if (controller.checkStructureAsyncLockedBlocking(ctx2)) {
							controller.onStructureFormed(ctx2);
						} else {
							controller.onStructureInvalid(ctx2);
						}
					} finally {
						lock.unlock();
					}
				});
			}
		} else {
			if (controller.getLevel() instanceof final ServerLevel serverLevel) {
				serverLevel.getServer().execute(() -> {
					final Lock lock = controller.getStructureCheckLock();
					lock.lock();
					try {
						controller.onStructureInvalid(ctx);
					} finally {
						lock.unlock();
					}
				});
			}
		}
	}
}
