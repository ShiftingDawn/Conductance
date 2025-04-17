package conductance.machine;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;
import lombok.Setter;
import conductance.api.NCRecipeElementTypes;
import conductance.api.machine.IOverclockable;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineType;
import conductance.api.machine.capability.MachineRecipeCapability;
import conductance.api.machine.capability.MachineRecipeCapabilityEnergy;
import conductance.api.machine.capability.MachineRecipeCapabilityFluids;
import conductance.api.machine.capability.MachineRecipeCapabilityItems;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.machine.recipe.RecipeCapabilityHolder;
import conductance.api.machine.recipe.RecipeProcessor;
import conductance.api.machine.recipe.WorkableMachineRecipeProviderConfigAdapter;
import conductance.api.util.IOMode;
import conductance.api.util.tier.Tier;
import conductance.api.util.tier.TierHolder;

public class TieredWorkableMachine<T extends TieredWorkableMachine<T>> extends MachineBlockEntity<T> implements WorkableMachineRecipeProviderConfigAdapter, RecipeCapabilityHolder, TierHolder, IOverclockable {

	protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(TieredWorkableMachine.class, MachineBlockEntity.MANAGED_FIELD_HOLDER);
	@Getter
	private final Tier tier;
	@Persisted
	@DescSynced
	private Tier overclockTier;
	@Persisted
	@DescSynced
	@Getter
	private final MachineRecipeCapabilityItems inputInventory;
	@Persisted
	@DescSynced
	@Getter
	private final MachineRecipeCapabilityItems outputInventory;
	@Persisted
	@DescSynced
	@Getter
	private final MachineRecipeCapabilityFluids inputTank;
	@Persisted
	@DescSynced
	@Getter
	private final MachineRecipeCapabilityFluids outputTank;
	@Persisted
	@DescSynced
	@Getter
	private final MachineRecipeCapabilityEnergy energy;
	@Persisted
	@DescSynced
	@Getter
	private final RecipeProcessor recipeProcessor;
	@Getter
	private Table<IOMode, IRecipeElementType<?>, List<MachineRecipeCapability<?>>> recipeCapabilities;
	protected final List<ISubscription> recipeCapabilitySubscriptions = new ArrayList<>();
	@Getter
	@Setter
	@Persisted
	private int activeRecipeType;

	public TieredWorkableMachine(final MachineType<T> machineType, final BlockPos pos, final BlockState blockState, final Tier tier) {
		super(machineType, pos, blockState);
		this.tier = tier;
		this.overclockTier = tier;
		this.recipeCapabilities = Tables.newCustomTable(new EnumMap<>(IOMode.class), HashMap::new);
		this.inputInventory = this.createItemInputAction();
		this.outputInventory = this.createItemOutputAction();
		this.inputTank = this.createFluidInputAction();
		this.outputTank = this.createFluidOutputAction();
		this.energy = this.createEnergyAction();
		this.recipeProcessor = new RecipeProcessor(this, this, this);
	}

	protected MachineRecipeCapabilityItems createItemInputAction() {
		return new MachineRecipeCapabilityItems(this, this.getRecipeType().getMaxInputs(NCRecipeElementTypes.ITEM), IOMode.INPUT);
	}

	protected MachineRecipeCapabilityItems createItemOutputAction() {
		final int slots = this.getMachineType().getRecipeOutputLimits().getOrDefault(NCRecipeElementTypes.ITEM, this.getRecipeType().getMaxOutputs(NCRecipeElementTypes.ITEM));
		return new MachineRecipeCapabilityItems(this, slots, IOMode.OUTPUT);
	}

	protected MachineRecipeCapabilityFluids createFluidInputAction() {
		return new MachineRecipeCapabilityFluids(this, this.getRecipeType().getMaxInputs(NCRecipeElementTypes.FLUID), 16 * FluidHelper.getBucket(), IOMode.INPUT);
	}

	protected MachineRecipeCapabilityFluids createFluidOutputAction() {
		final int slots = this.getMachineType().getRecipeOutputLimits().getOrDefault(NCRecipeElementTypes.FLUID, this.getRecipeType().getMaxOutputs(NCRecipeElementTypes.FLUID));
		return new MachineRecipeCapabilityFluids(this, slots, 16 * FluidHelper.getBucket(), IOMode.OUTPUT);
	}

	protected MachineRecipeCapabilityEnergy createEnergyAction() {
		if (this.isEnergyGenerator()) {
			return MachineRecipeCapabilityEnergy.createOutput(this, this.tier.getVoltage() * 64, this.tier.getVoltage(), this.getMaxEnergyAmperage());
		} else {
			return MachineRecipeCapabilityEnergy.createInput(this, this.tier.getVoltage() * 64, this.tier.getVoltage(), this.getMaxEnergyAmperage());
		}
	}

	@Override
	public ManagedFieldHolder getFieldHolder() {
		return TieredWorkableMachine.MANAGED_FIELD_HOLDER;
	}

	@Override
	public void onLoad() {
		super.onLoad();
		this.getCapabilities().forEach(capability -> {
			if (capability instanceof final MachineRecipeCapability<?> recipeCapability) {
				if (!this.recipeCapabilities.contains(recipeCapability.getCapabilityIoMode(), recipeCapability.getElementType())) {
					this.recipeCapabilities.put(recipeCapability.getCapabilityIoMode(), recipeCapability.getElementType(), new ArrayList<>());
				}
				final List<MachineRecipeCapability<?>> list = this.recipeCapabilities.get(recipeCapability.getCapabilityIoMode(), recipeCapability.getElementType());
				assert list != null;
				list.add(recipeCapability);
				this.recipeCapabilitySubscriptions.add(recipeCapability.addChangedListener(this.recipeProcessor::updateTickable));
			}
		});
	}

	@Override
	public void onUnload() {
		super.onUnload();
		this.recipeCapabilitySubscriptions.forEach(ISubscription::unsubscribe);
		this.recipeCapabilitySubscriptions.clear();
	}

	@Override
	public boolean keepTickables() {
		return false;
	}

	@Override
	public NCRecipeType[] getRecipeTypes() {
		return this.getMachineType().getRecipeTypes();
	}

	@Override
	public NCRecipeType getRecipeType() {
		return this.getRecipeTypes()[this.activeRecipeType];
	}

	@Override
	public Tier getOverclockTier() {
		return this.overclockTier;
	}

	@Override
	public void setOverclockTier(final Tier newTier) {
		if (!this.isClientSide() && newTier.getIndex() >= this.getMinOverclockTier().getIndex() && newTier.getIndex() <= this.getMaxOverclockTier().getIndex()) {
			this.overclockTier = newTier;
			this.recipeProcessor.markDirty();
		}
	}

	@Override
	public Tier getMaxOverclockTier() {
		return this.tier;
	}

	@Override
	public Map<IRecipeElementType<?>, Integer> getOutputLimits() {
		return this.getMachineType().getRecipeOutputLimits();
	}

	protected boolean isEnergyGenerator() {
		return false;
	}

	protected long getMaxEnergyAmperage() {
		return 2L;
	}
}
