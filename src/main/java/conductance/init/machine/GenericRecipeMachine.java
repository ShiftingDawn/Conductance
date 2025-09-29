package conductance.init.machine;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import lombok.Getter;
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
import conductance.api.recipe.RecipeHelper;
import conductance.api.util.IO;

public final class GenericRecipeMachine extends MachineBlockEntity<GenericRecipeMachine> implements RecipeCapabilityHolder {

	private final @Getter MachineRecipeCapabilityItems inputItems;
	private final @Getter MachineRecipeCapabilityItems outputItems;
	private final @Getter RecipeHandler recipeHandler;
	private final Lazy<IntSortedSet> recipePrograms;

	public GenericRecipeMachine(final MachineType<GenericRecipeMachine> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
		this.inputItems = new MachineRecipeCapabilityItems(this, this.getRecipeType().getLimit(IO.IN, NCRecipeElementTypes.ITEM), IO.IN, CapIO.IN, MachineInventory::new);
		this.inputItems.addChangedListener(this::setChanged);
		this.outputItems = new MachineRecipeCapabilityItems(this, this.getRecipeType().getLimit(IO.OUT, NCRecipeElementTypes.ITEM), IO.OUT, CapIO.OUT, MachineInventory::new);
		this.outputItems.addChangedListener(this::setChanged);
		this.recipeHandler = new RecipeHandler(this, this);
		this.inputItems.addChangedListener(this.recipeHandler::revalidateTick);
		this.outputItems.addChangedListener(this.recipeHandler::revalidateTick);
		this.recipePrograms = Lazy.of(() -> RecipeHelper.findPrograms(this.inputItems.getInventory()));
		this.inputItems.addChangedListener(this.recipePrograms::invalidate);
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

	@Override
	public IntSortedSet getRecipePrograms() {
		return this.recipePrograms.get();
	}
}
