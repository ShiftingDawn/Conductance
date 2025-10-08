package conductance.init.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import lombok.Getter;
import conductance.api.machine.CapIO;
import conductance.api.machine.MachineCapabilityInventory;
import conductance.api.machine.MachineInventory;
import conductance.api.machine.MachineType;

public class SteamSolidFuelBoilerMachine extends AbstractSteamBoilerMachine<SteamSolidFuelBoilerMachine> {

	private final @Getter MachineCapabilityInventory inputItems;

	public SteamSolidFuelBoilerMachine(final MachineType<SteamSolidFuelBoilerMachine> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
		this.inputItems = new MachineCapabilityInventory("input", this, new MachineInventory(1));
		this.inputItems.setIoMode(CapIO.IN);
		this.inputItems.addChangedListener(this::setChanged);
		this.inputItems.addChangedListener(this.getBoilerHandler()::revalidateTick);
	}

	@Override
	protected int consumeSingularInput() {
		final ItemStack stack = this.inputItems.getStackInSlot(0);
		if (stack.isEmpty()) {
			return 0;
		}
		final ItemStack copy = stack.copyWithCount(1);
		assert this.level != null;
		final int result = copy.getBurnTime(RecipeType.SMELTING, this.level.fuelValues());
		if (result > 0) {
			this.inputItems.extractItemInternal(0, 1, false);
		}
		return result;
	}
}
