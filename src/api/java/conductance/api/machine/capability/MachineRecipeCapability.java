package conductance.api.machine.capability;

import java.util.List;
import java.util.function.Predicate;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.recipe.IRecipe;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.util.IOMode;

public abstract class MachineRecipeCapability<T> extends MachineCapability implements Predicate<T> {

	protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MachineRecipeCapability.class, MachineCapability.MANAGED_FIELD_HOLDER);

	@Getter
	private final IRecipeElementType<T> elementType;

	protected MachineRecipeCapability(final MachineBlockEntity<?> machineBlockEntity, final IRecipeElementType<T> elementType) {
		super(machineBlockEntity);
		this.elementType = elementType;
	}


	public abstract List<T> handleRecipe(IOMode ioMode, IRecipe recipe, List<T> inputs, boolean simulate);

	@Override
	public ManagedFieldHolder getFieldHolder() {
		return MachineRecipeCapability.MANAGED_FIELD_HOLDER;
	}
}
