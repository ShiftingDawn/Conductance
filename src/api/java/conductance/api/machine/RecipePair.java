package conductance.api.machine;

import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import conductance.api.recipe.MachineRecipe;

public record RecipePair(MachineRecipe realRecipe, @Nullable MachineRecipe modifiedRecipe) {

	public boolean isModified() {
		return this.modifiedRecipe != null;
	}

	public MachineRecipe getUsableRecipe() {
		return Objects.requireNonNullElse(this.modifiedRecipe, this.realRecipe);
	}
}
