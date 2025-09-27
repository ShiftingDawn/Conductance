package conductance.init.machine;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import conductance.api.NCRecipeElementTypes;
import conductance.api.machine.CapIO;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineInventory;
import conductance.api.machine.MachineRecipeCapability;
import conductance.api.machine.MachineRecipeCapabilityItems;
import conductance.api.machine.MachineType;
import conductance.api.machine.RecipeCapabilityHolder;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.recipe.RecipeElementType;
import conductance.api.util.IO;

public final class PulverizerMachine extends MachineBlockEntity<PulverizerMachine> implements RecipeCapabilityHolder {

	private final MachineRecipeCapabilityItems inputItems;
	private final MachineRecipeCapabilityItems outputItems;
	private final RecipeHandler recipeHandler;

	public PulverizerMachine(final MachineType<PulverizerMachine> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
		this.inputItems = new MachineRecipeCapabilityItems(this, this.getRecipeType().getLimit(IO.IN, NCRecipeElementTypes.ITEM), IO.IN, CapIO.IN, MachineInventory::new);
		this.inputItems.addChangedListener(this::setChanged);
		this.outputItems = new MachineRecipeCapabilityItems(this, this.getRecipeType().getLimit(IO.OUT, NCRecipeElementTypes.ITEM), IO.OUT, CapIO.OUT, MachineInventory::new);
		this.outputItems.addChangedListener(this::setChanged);
		this.recipeHandler = new RecipeHandler(this, this);
		this.inputItems.addChangedListener(this.recipeHandler::revalidateTick);
		this.outputItems.addChangedListener(this.recipeHandler::revalidateTick);
	}

	@Override
	public List<MachineRecipeCapability<?>> getRecipeCapabilities(final RecipeElementType<?> elementType, final IO io) {
		if (elementType != NCRecipeElementTypes.ITEM) {
			return List.of();
		}
		return switch (io) {
			case IN -> List.of(this.inputItems);
			case OUT -> List.of(this.outputItems);
		};
	}

	@Override
	public MachineRecipeType getRecipeType() {
		return this.getMachineType().getRecipeTypes()[0];
	}
}
