package conductance.init.machine;

import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import lombok.Getter;
import conductance.api.NCRecipeElementTypes;
import conductance.api.machine.CapIO;
import conductance.api.machine.MachineInventory;
import conductance.api.machine.MachineRecipeCapability;
import conductance.api.machine.MachineRecipeCapabilityItems;
import conductance.api.machine.MachineType;
import conductance.api.recipe.RecipeElementType;
import conductance.api.util.IO;

public class SteamSolidFuelBoilerMachine extends AbstractSteamBoilerMachine<SteamSolidFuelBoilerMachine> {

	private final @Getter MachineRecipeCapabilityItems inputItems;

	public SteamSolidFuelBoilerMachine(final MachineType<SteamSolidFuelBoilerMachine> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
		this.inputItems = new MachineRecipeCapabilityItems(this, 1, IO.IN, CapIO.IN, MachineInventory::new);
		this.inputItems.addChangedListener(this::setChanged);
		this.inputItems.addChangedListener(this.getBoilerHandler()::revalidateTick);
	}

	@Override
	public List<MachineRecipeCapability<?>> getRecipeCapabilities(final RecipeElementType<?> elementType, final IO io) {
		if (io == IO.IN && elementType == NCRecipeElementTypes.ITEM) {
			return List.of(this.inputItems);
		}
		return super.getRecipeCapabilities(elementType, io);
	}

	@Override
	public int getFuelForInput() {
		final ItemStack stack = this.inputItems.getStackInSlot(0);
		if (stack.isEmpty()) {
			return 0;
		}
		final ItemStack copy = stack.copyWithCount(1);
		assert this.level != null;
		return copy.getBurnTime(RecipeType.SMELTING, this.level.fuelValues());
	}

	@Override
	public void addInputs(final BiConsumer<RecipeElementType<?>, Object> consumer) {
		final ItemStack stack = this.inputItems.getStackInSlot(0);
		if (stack.isEmpty()) {
			return;
		}
		consumer.accept(NCRecipeElementTypes.ITEM, SizedIngredient.of(stack.getItem(), 1));
	}
}
