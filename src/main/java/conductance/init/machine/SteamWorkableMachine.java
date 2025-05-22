package conductance.init.machine;

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
import lombok.Getter;
import lombok.Setter;
import conductance.api.machine.MachineType;
import conductance.api.machine.capability.MachineRecipeCapability;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.machine.recipe.RecipeCapabilityHolder;
import conductance.api.machine.recipe.RecipeProcessor;
import conductance.api.machine.recipe.WorkableMachineRecipeProviderConfigAdapter;
import conductance.api.machine.sync.Persisted;
import conductance.api.machine.sync.Synchronized;
import conductance.api.util.IOMode;

public abstract class SteamWorkableMachine<T extends SteamWorkableMachine<T>> extends SteamMachine<T> implements WorkableMachineRecipeProviderConfigAdapter, RecipeCapabilityHolder {

	@Persisted
	@Synchronized
	@Getter
	private final RecipeProcessor recipeProcessor;
	@Getter
	private Table<IOMode, IRecipeElementType<?>, List<MachineRecipeCapability<?>>> recipeCapabilities;
	protected final List<ISubscription> recipeActionSubscriptions;
	@Getter
	@Setter
	@Persisted
	public int activeRecipeType;

	public SteamWorkableMachine(final MachineType<T> machineType, final BlockPos pos, final BlockState blockState) {
		super(machineType, pos, blockState);
		this.recipeCapabilities = Tables.newCustomTable(new EnumMap<>(IOMode.class), HashMap::new);
		this.recipeActionSubscriptions = new ArrayList<>();
		this.recipeProcessor = new RecipeProcessor(this, this, this);
	}

	@Override
	public void onLoad() {
		super.onLoad();
		this.getCapabilities().forEach(action -> {
			if (action instanceof final MachineRecipeCapability<?> recipeCapability) {
				if (!this.recipeCapabilities.contains(recipeCapability.getCapabilityIoMode(), recipeCapability.getElementType())) {
					this.recipeCapabilities.put(recipeCapability.getCapabilityIoMode(), recipeCapability.getElementType(), new ArrayList<>());
				}
				final List<MachineRecipeCapability<?>> handlers = this.recipeCapabilities.get(recipeCapability.getCapabilityIoMode(), recipeCapability.getElementType());
				if (handlers != null) {
					handlers.add(recipeCapability);
				}
				this.recipeActionSubscriptions.add(recipeCapability.addChangedListener(this.recipeProcessor::updateTickable));
			}
		});
	}

	@Override
	public boolean keepTickables() {
		return false;
	}

	@Override
	public void onUnload() {
		this.recipeActionSubscriptions.forEach(ISubscription::unsubscribe);
		this.recipeActionSubscriptions.clear();
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
