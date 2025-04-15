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
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;
import lombok.Setter;
import conductance.api.NCRecipeElementTypes;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineType;
import conductance.api.machine.capability.MachineRecipeCapability;
import conductance.api.machine.capability.MachineRecipeCapabilityItems;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.machine.recipe.RecipeCapabilityHolder;
import conductance.api.machine.recipe.RecipeProcessor;
import conductance.api.machine.recipe.WorkableMachineRecipeProviderConfigAdapter;
import conductance.api.util.IOMode;

public class BaseWorkableMachine<T extends BaseWorkableMachine<T>> extends MachineBlockEntity<T> implements WorkableMachineRecipeProviderConfigAdapter, RecipeCapabilityHolder {

	protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(BaseWorkableMachine.class, MachineBlockEntity.MANAGED_FIELD_HOLDER);

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
	private final RecipeProcessor recipeProcessor;
	@Getter
	private Table<IOMode, IRecipeElementType<?>, List<MachineRecipeCapability<?>>> recipeCapabilities;
	protected final List<ISubscription> recipeCapabilitySubscriptions = new ArrayList<>();
	@Getter
	@Setter
	@Persisted
	private int activeRecipeType;

	public BaseWorkableMachine(final MachineType<T> machineType, final BlockPos pos, final BlockState blockState) {
		super(machineType, pos, blockState);
		this.recipeCapabilities = Tables.newCustomTable(new EnumMap<>(IOMode.class), HashMap::new);
		this.inputInventory = this.createItemInputAction();
		this.outputInventory = this.createItemOutputAction();
		this.recipeProcessor = new RecipeProcessor(this, this, this);
	}

	protected MachineRecipeCapabilityItems createItemInputAction() {
		return new MachineRecipeCapabilityItems(this, this.getRecipeType().getMaxInputs(NCRecipeElementTypes.ITEM), IOMode.INPUT);
	}

	protected MachineRecipeCapabilityItems createItemOutputAction() {
		final int slots = this.getMachineType().getRecipeOutputLimits().getOrDefault(NCRecipeElementTypes.ITEM, this.getRecipeType().getMaxOutputs(NCRecipeElementTypes.ITEM));
		return new MachineRecipeCapabilityItems(this, slots, IOMode.OUTPUT);
	}

	@Override
	public ManagedFieldHolder getFieldHolder() {
		return BaseWorkableMachine.MANAGED_FIELD_HOLDER;
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
	public Map<IRecipeElementType<?>, Integer> getOutputLimits() {
		return this.getMachineType().getRecipeOutputLimits();
	}
}
