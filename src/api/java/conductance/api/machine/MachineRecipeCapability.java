package conductance.api.machine;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.recipe.MachineRecipe;
import conductance.api.recipe.RecipeElementType;
import conductance.api.util.IO;

public abstract class MachineRecipeCapability<T> extends MachineCapability implements IBlockCapabilityHandler {

	private final @Getter RecipeElementType<T> elementType;
	private final @Getter IO recipeIoMode;
	private final @Getter CapIO capabilityIoMode;

	protected MachineRecipeCapability(final MachineBlockEntity<?> machine, final RecipeElementType<T> elementType, final IO recipeIoMode, final CapIO capabilityIoMode) {
		super(elementType.getId().getPath() + "_" + recipeIoMode, machine);
		this.elementType = elementType;
		this.recipeIoMode = recipeIoMode;
		this.capabilityIoMode = capabilityIoMode;
	}

	@Nullable
	protected abstract List<T> handleInternal(IO io, MachineRecipe recipe, List<T> inputs, boolean simulate);

	public abstract List<T> getAvailableContent();

	public abstract int getMaxSpaceForContent(T object);

	@Nullable
	public final List<T> handle(final IO io, final MachineRecipe recipe, final List<T> inputs, final boolean simulate) {
		if (this.getMachine().getLevel() == null) {
			return inputs;
		}
		return this.handleInternal(io, recipe, new ArrayList<>(inputs.stream().map(obj -> this.elementType.getCloner().copy(obj)).toList()), simulate);
	}
}
