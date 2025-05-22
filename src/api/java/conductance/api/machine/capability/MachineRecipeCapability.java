package conductance.api.machine.capability;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.ICapabilityHandler;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.recipe.IRecipe;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.util.IOMode;

public abstract class MachineRecipeCapability<T> extends MachineCapability implements ICapabilityHandler {

	@Getter
	private final IOMode capabilityIoMode;
	@Getter
	private final IOMode handlerIoMode;
	@Getter
	private final IRecipeElementType<T> elementType;

	protected MachineRecipeCapability(final MachineBlockEntity<?> machineBlockEntity, final IRecipeElementType<T> elementType, final IOMode capabilityIoMode, final IOMode handlerIoMode) {
		super(machineBlockEntity);
		this.elementType = elementType;
		this.capabilityIoMode = capabilityIoMode;
		this.handlerIoMode = handlerIoMode;
	}

	@Nullable
	protected abstract List<T> handleInternal(IOMode ioMode, IRecipe recipe, List<T> inputs, boolean simulate);

	@Nullable
	public final List<T> handle(final IOMode ioMode, final IRecipe recipe, final List<T> inputs, final boolean simulate) {
		return this.handleInternal(ioMode, recipe, new ArrayList<>(inputs.stream().map(obj -> this.elementType.getCloner().copy(obj)).toList()), simulate);
	}
}
