package conductance.api.recipe;

import javax.annotation.Nullable;
import conductance.api.CAPI;

public record RecipeElement(Object data, int chance, int maxChange, int tieredChanceBoost) {

	public boolean testChance(final int chanceTier) {
		final int maxChance = Math.min(this.chance() + chanceTier * this.tieredChanceBoost(), this.maxChange);
		return this.chance >= 100 || CAPI.RANDOM.nextInt() < maxChance;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public RecipeElement copy(final IRecipeElementType<?> elementType, @Nullable final RecipeModifier modifier) {
		final RecipeElementCloner cloner = elementType.getCloner();
		if (modifier == null || this.chance == 0) {
			return new RecipeElement(cloner.copy(this.data), this.chance, this.maxChange, this.tieredChanceBoost);
		} else {
			return new RecipeElement(cloner.copy(this.data, modifier), this.chance, this.maxChange, this.tieredChanceBoost);
		}
	}
}
