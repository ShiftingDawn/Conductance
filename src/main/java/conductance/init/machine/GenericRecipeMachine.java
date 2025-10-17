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
import conductance.api.machine.MachineCapabilityFluidAutoOutput;
import conductance.api.machine.MachineCapabilityItemAutoOutput;
import conductance.api.machine.MachineFluidHandler;
import conductance.api.machine.MachineInventory;
import conductance.api.machine.MachineRecipeCapability;
import conductance.api.machine.MachineRecipeCapabilityEnergy;
import conductance.api.machine.MachineRecipeCapabilityFluids;
import conductance.api.machine.MachineRecipeCapabilityItems;
import conductance.api.machine.MachineType;
import conductance.api.machine.RecipeCapabilityHolder;
import conductance.api.machine.RecipeHandler;
import conductance.api.recipe.MachineRecipeModifier;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.recipe.RecipeElementType;
import conductance.api.recipe.RecipeHelper;
import conductance.api.tier.Tier;
import conductance.api.util.IO;

public class GenericRecipeMachine extends MachineBlockEntity<GenericRecipeMachine> implements RecipeCapabilityHolder {

	private final @Getter Tier tier;
	private final @Getter RecipeHandler recipeHandler;
	@Getter
	private final @Nullable MachineRecipeCapabilityItems inputItems;
	@Getter
	private final @Nullable MachineRecipeCapabilityItems outputItems;
	@Getter
	private final @Nullable MachineRecipeCapabilityFluids inputFluids;
	@Getter
	private final @Nullable MachineRecipeCapabilityFluids outputFluids;
	@Getter
	private final MachineRecipeCapabilityEnergy energy;
	private final Table<RecipeElementType<?>, IO, List<MachineRecipeCapability<?>>> recipeCapabilities;
	private final Lazy<IntSortedSet> recipePrograms;
	@Getter
	private final @Nullable MachineCapabilityItemAutoOutput itemAutoOutput;
	@Getter
	private final @Nullable MachineCapabilityFluidAutoOutput fluidAutoOutput;

	public GenericRecipeMachine(final MachineType<GenericRecipeMachine> type, final Tier tier, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
		this.tier = tier;
		this.recipeHandler = new RecipeHandler(this, this);
		final int inputItemLimit = this.getRecipeType().getLimit(IO.IN, NCRecipeElementTypes.ITEM);
		this.inputItems = inputItemLimit > 0 ? CAPI.make(new MachineRecipeCapabilityItems(this, inputItemLimit, IO.IN, CapIO.IN, MachineInventory::new), inv -> {
			inv.addChangedListener(this.recipeHandler::revalidateTick);
		}) : null;
		final int outputItemLimit = this.getRecipeType().getLimit(IO.OUT, NCRecipeElementTypes.ITEM);
		this.outputItems = outputItemLimit > 0 ? CAPI.make(new MachineRecipeCapabilityItems(this, outputItemLimit, IO.OUT, CapIO.OUT, MachineInventory::new), inv -> {
			inv.addChangedListener(this.recipeHandler::revalidateTick);
		}) : null;
		final int inputFluidLimit = this.getRecipeType().getLimit(IO.IN, NCRecipeElementTypes.FLUID);
		this.inputFluids = inputFluidLimit > 0 ? CAPI.make(new MachineRecipeCapabilityFluids(this, inputFluidLimit, IO.IN, CapIO.IN, tankCount -> new MachineFluidHandler(tankCount, CAPI.BUCKET * 16)), handler -> {
			handler.addChangedListener(this.recipeHandler::revalidateTick);
		}) : null;
		final int outputFluidLimit = this.getRecipeType().getLimit(IO.OUT, NCRecipeElementTypes.FLUID);
		this.outputFluids = outputFluidLimit > 0 ? CAPI.make(new MachineRecipeCapabilityFluids(this, outputFluidLimit, IO.OUT, CapIO.OUT, tankCount -> new MachineFluidHandler(tankCount, CAPI.BUCKET * 16)), handler -> {
			handler.addChangedListener(this.recipeHandler::revalidateTick);
		}) : null;
		this.energy = this.isEnergyGenerator()
			? MachineRecipeCapabilityEnergy.createOutput(this, IO.OUT, tier.getVoltage() * 64, tier.getVoltage(), this.getMaxEnergyAmperage(), false)
			: MachineRecipeCapabilityEnergy.createInput(this, IO.IN, tier.getVoltage() * 64, tier.getVoltage(), this.getMaxEnergyAmperage(), false);
		this.energy.addChangedListener(this.recipeHandler::revalidateTick);
		this.recipeCapabilities = Tables.unmodifiableTable(Util.make(HashBasedTable.create(), table -> {
			table.put(NCRecipeElementTypes.ITEM, IO.IN, this.inputItems != null ? List.of(this.inputItems) : List.of());
			table.put(NCRecipeElementTypes.ITEM, IO.OUT, this.outputItems != null ? List.of(this.outputItems) : List.of());
			table.put(NCRecipeElementTypes.FLUID, IO.IN, this.inputFluids != null ? List.of(this.inputFluids) : List.of());
			table.put(NCRecipeElementTypes.FLUID, IO.OUT, this.outputFluids != null ? List.of(this.outputFluids) : List.of());
			table.put(NCRecipeElementTypes.ENERGY, this.isEnergyGenerator() ? IO.OUT : IO.IN, List.of(this.energy));
		}));
		if (this.inputItems != null) {
			this.recipePrograms = Lazy.of(() -> RecipeHelper.findPrograms(this.inputItems.getInventory()));
			this.inputItems.addChangedListener(this.recipePrograms::invalidate);
		} else {
			this.recipePrograms = Lazy.of(() -> IntSortedSets.EMPTY_SET);
		}
		if (this.outputItems != null) {
			this.itemAutoOutput = new MachineCapabilityItemAutoOutput("item_auto_output", this, this.outputItems.getInventory());
		} else {
			this.itemAutoOutput = null;
		}
		if (this.outputFluids != null) {
			this.fluidAutoOutput = new MachineCapabilityFluidAutoOutput("fluid_auto_output", this, this.outputFluids.getHandler());
		} else {
			this.fluidAutoOutput = null;
		}
	}

	protected boolean isEnergyGenerator() {
		return false;
	}

	protected long getMaxEnergyAmperage() {
		return 2L;
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
	public Tier getMaxRecipeTier() {
		return this.tier;
	}

	@Override
	public @Nullable MachineRecipeModifier getRecipeModifier() {
		return this.getMachineType().getRecipeModifier();
	}

	@Override
	public IntSortedSet getRecipePrograms() {
		return this.recipePrograms.get();
	}
}
