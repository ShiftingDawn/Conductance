package conductance.init.machine;

import java.util.List;
import java.util.Objects;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import it.unimi.dsi.fastutil.ints.IntSortedSets;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.NCRecipeElementTypes;
import conductance.api.machine.CapIO;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineFluidHandler;
import conductance.api.machine.MachineInventory;
import conductance.api.machine.MachineRecipeCapability;
import conductance.api.machine.MachineRecipeCapabilityFluids;
import conductance.api.machine.MachineRecipeCapabilityItems;
import conductance.api.machine.MachineType;
import conductance.api.machine.RecipeCapabilityHolder;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.recipe.RecipeElementType;
import conductance.api.recipe.RecipeHelper;
import conductance.api.util.IO;

public final class GenericRecipeMachine extends MachineBlockEntity<GenericRecipeMachine> implements RecipeCapabilityHolder {

	private final @Getter RecipeHandler recipeHandler;
	@Getter
	private final @Nullable MachineRecipeCapabilityItems inputItems;
	@Getter
	private final @Nullable MachineRecipeCapabilityItems outputItems;
	@Getter
	private final @Nullable MachineRecipeCapabilityFluids inputFluids;
	@Getter
	private final @Nullable MachineRecipeCapabilityFluids outputFluids;
	private final Table<RecipeElementType<?>, IO, List<MachineRecipeCapability<?>>> recipeCapabilities;
	private final Lazy<IntSortedSet> recipePrograms;

	public GenericRecipeMachine(final MachineType<GenericRecipeMachine> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
		this.recipeHandler = new RecipeHandler(this, this);
		final int inputItemLimit = this.getRecipeType().getLimit(IO.IN, NCRecipeElementTypes.ITEM);
		if (inputItemLimit > 0) {
			this.inputItems = new MachineRecipeCapabilityItems(this, inputItemLimit, IO.IN, CapIO.IN, MachineInventory::new);
			this.inputItems.addChangedListener(this::setChanged);
			this.inputItems.addChangedListener(this.recipeHandler::revalidateTick);
		} else {
			this.inputItems = null;
		}
		final int outputItemLimit = this.getRecipeType().getLimit(IO.OUT, NCRecipeElementTypes.ITEM);
		if (outputItemLimit > 0) {
			this.outputItems = new MachineRecipeCapabilityItems(this, outputItemLimit, IO.OUT, CapIO.OUT, MachineInventory::new);
			this.outputItems.addChangedListener(this::setChanged);
			this.outputItems.addChangedListener(this.recipeHandler::revalidateTick);
		} else {
			this.outputItems = null;
		}
		final int inputFluidLimit = this.getRecipeType().getLimit(IO.IN, NCRecipeElementTypes.FLUID);
		if (inputFluidLimit > 0) {
			this.inputFluids = new MachineRecipeCapabilityFluids(this, inputFluidLimit, IO.IN, CapIO.IN, tankCount -> new MachineFluidHandler(tankCount, CAPI.BUCKET * 16));
			this.inputFluids.addChangedListener(this::setChanged);
			this.inputFluids.addChangedListener(this.recipeHandler::revalidateTick);
		} else {
			this.inputFluids = null;
		}
		final int outputFluidLimit = this.getRecipeType().getLimit(IO.OUT, NCRecipeElementTypes.FLUID);
		if (outputFluidLimit > 0) {
			this.outputFluids = new MachineRecipeCapabilityFluids(this, outputFluidLimit, IO.OUT, CapIO.OUT, tankCount -> new MachineFluidHandler(tankCount, CAPI.BUCKET * 16));
			this.outputFluids.addChangedListener(this::setChanged);
			this.outputFluids.addChangedListener(this.recipeHandler::revalidateTick);
		} else {
			this.outputFluids = null;
		}
		this.recipeCapabilities = Tables.unmodifiableTable(Util.make(HashBasedTable.create(), table -> {
			table.put(NCRecipeElementTypes.ITEM, IO.IN, this.inputItems != null ? List.of(this.inputItems) : List.of());
			table.put(NCRecipeElementTypes.ITEM, IO.OUT, this.outputItems != null ? List.of(this.outputItems) : List.of());
			table.put(NCRecipeElementTypes.FLUID, IO.IN, this.inputFluids != null ? List.of(this.inputFluids) : List.of());
			table.put(NCRecipeElementTypes.FLUID, IO.OUT, this.outputFluids != null ? List.of(this.outputFluids) : List.of());
		}));
		if (this.inputItems != null) {
			this.recipePrograms = Lazy.of(() -> RecipeHelper.findPrograms(this.inputItems.getInventory()));
			this.inputItems.addChangedListener(this.recipePrograms::invalidate);
		} else {
			this.recipePrograms = Lazy.of(() -> IntSortedSets.EMPTY_SET);
		}
	}

	@Override
	public List<MachineRecipeCapability<?>> getRecipeCapabilities(final RecipeElementType<?> elementType, final IO io) {
		return Objects.requireNonNullElse(this.recipeCapabilities.get(elementType, io), List.of());
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
