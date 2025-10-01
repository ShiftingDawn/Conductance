package conductance.api.recipe;

import conductance.api.CAPI;

public record RecipeObject(Object data, double chance) {

	public boolean testChance() {
		if (this.chance == 0) {
			return false;
		} else if (this.chance >= 1) {
			return true;
		} else {
			return CAPI.RANDOM.nextDouble() < this.chance;
		}
	}
}
