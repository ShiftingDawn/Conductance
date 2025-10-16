package conductance.api.machine.multi;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import conductance.api.CAPI;
import conductance.api.NCBlockStateProperties;
import conductance.api.block.BlockRotationHelper;
import conductance.api.block.FacingAndRotation;
import conductance.api.coil.CoilBlockType;
import conductance.api.util.Internal;

public class MultiControllerMachineBlockEntity<T extends MultiControllerMachineBlockEntity<T>> extends MultiMachineBlockEntity<T> implements IMultiBlockController<T> {

	public static final int REQUEST_STRUCTURE_FORMED = 1;
	public static final int REQUEST_STRUCTURE_INVALID = 2;
	private final @Getter Set<IMultiBlockPart> parts = new HashSet<>();
	private final Set<BlockPos> activeBlocks = new HashSet<>();
	private final @Getter Lock structureCheckLock = new ReentrantLock();
	private @Getter boolean structureFormed = false;
	@Nullable
	private @Getter CoilBlockType coilType;
	@UnknownNullability
	private @Getter StructureCheckContext lastContext;

	public MultiControllerMachineBlockEntity(final MultiMachineType<T> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
	}

	@Override
	public void onLoad() {
		super.onLoad();
		if (this.level instanceof final ServerLevel serverLevel) {
			Internal.MULTIBLOCK_CONTROLLER_LOAD.accept(serverLevel, this);
			MultiControllerMachineBlockEntity.checkStructure(serverLevel, this);
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
		this.lastContext = ctx;
		this.coilType = ctx.getNotSet(StructureCheckContext.COIL_TYPE);
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
			if (this.coilType != null) {
				output.store("coils", ResourceLocation.CODEC, this.coilType.getId());
			}
		});
	}

	@Override
	public void onStructureInvalid(final StructureCheckContext ctx) {
		this.structureFormed = false;
		this.coilType = null;
		this.lastContext = ctx;
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

	public int getCoilTemperature() {
		return this.coilType != null ? this.coilType.getTemperature() : 0;
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
				this.coilType = input.read("coils", ResourceLocation.CODEC).map(CAPI.regs().coilBlockTypes()::getValue).orElse(null);
			}
			case MultiControllerMachineBlockEntity.REQUEST_STRUCTURE_INVALID -> {
				this.structureFormed = false;
				this.coilType = null;
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

	public static void checkStructure(final ServerLevel level, final IMultiBlockController<?> controller) {
		final StructureCheckContext ctx = new StructureCheckContext();
		if (controller.checkStructureAsyncLocked(ctx)) {
			level.getServer().execute(() -> controller.onStructureFormed(ctx));
		} else {
			level.getServer().execute(() -> controller.onStructureInvalid(ctx));
		}
	}
}
