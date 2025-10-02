package conductance.api.recipe;

import javax.annotation.Nullable;
import conductance.api.CAPI;

public record RecipeElement(Object data, double chance) {

	public boolean testChance() {
		if (this.chance == 0) {
			return false;
		} else if (this.chance >= 1) {
			return true;
		} else {
			return CAPI.RANDOM.nextDouble() < this.chance;
		}
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public RecipeElement copy(final RecipeElementType<?> elementType, @Nullable final RecipeModifier modifier) {
		final RecipeElementCloner cloner = elementType.getCloner();
		if (modifier == null || this.chance == 0) {
			return new RecipeElement(cloner.copy(this.data), this.chance);
		} else {
			return new RecipeElement(cloner.copy(this.data, modifier), this.chance);
		}
	}
}
