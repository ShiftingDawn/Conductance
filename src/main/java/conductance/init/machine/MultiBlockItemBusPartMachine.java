package conductance.init.machine;

import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCMultiBlockPartCapabilities;
import conductance.api.machine.CapIO;
import conductance.api.machine.CapabilityHelper;
import conductance.api.machine.MachineInventory;
import conductance.api.machine.MachineRecipeCapability;
import conductance.api.machine.MachineRecipeCapabilityItems;
import conductance.api.machine.MachineTick;
import conductance.api.machine.MachineType;
import conductance.api.machine.multi.MultiBlockPartCapability;
import conductance.api.machine.multi.WorkableMultiPartMachineBlockEntity;
import conductance.api.util.IO;

public final class MultiBlockItemBusPartMachine extends WorkableMultiPartMachineBlockEntity<MultiBlockItemBusPartMachine> {

	private final @Getter MachineRecipeCapabilityItems items;
	private final @Getter IO io;
	private @Nullable MachineTick tick = null;

	public MultiBlockItemBusPartMachine(final MachineType<MultiBlockItemBusPartMachine> type, final BlockPos pos, final BlockState blockState, final IO io, final int slots) {
		super(type, pos, blockState);
		this.items = new MachineRecipeCapabilityItems(this, slots, io, CapIO.BOTH, MachineInventory::new);
		this.items.addChangedListener(this::revalidateTick);
		this.io = io;
	}

	@Override
	public boolean getDefaultWorkingState() {
		return true;
	}

	protected void revalidateTick() {
		if (!this.isServerSide()) {
			return;
		}
		this.tick = this.addTick(this::tick, this.tick);
	}

	private void tick() {
		assert this.tick != null;
		if ((this.io == IO.OUT && this.items.getInventory().isEmpty()) || !this.isWorking()) {
			this.tick.invalidate();
			return;
		}
		if (this.haveTicksPassed(10)) {
			switch (this.io) {
				case IN -> CapabilityHelper.tryImportItems(this.items.getInventory(), this.getLevel(), this.getBlockPos().relative(this.getFacing()), this.getFacing().getOpposite());
				case OUT -> CapabilityHelper.tryExportItems(this.items.getInventory(), this.getLevel(), this.getBlockPos().relative(this.getFacing()), this.getFacing().getOpposite());
			}
		}
	}

	public void setAutoEnabled(final boolean enabled) {
		if (enabled != this.isWorking()) {
			this.setWorking(enabled);
			this.setChanged();
		}
	}

	@Override
	public void setChanged() {
		super.setChanged();
		this.revalidateTick();
	}

	@Override
	public MultiBlockPartCapability getPartCapability() {
		return switch (this.io) {
			case IN -> NCMultiBlockPartCapabilities.ITEMS_IN;
			case OUT -> NCMultiBlockPartCapabilities.ITEMS_OUT;
		};
	}

	@Override
	public void attachCapabilities(final BiConsumer<IO, MachineRecipeCapability<?>> consumer) {
		consumer.accept(this.io, this.items);
	}
}
