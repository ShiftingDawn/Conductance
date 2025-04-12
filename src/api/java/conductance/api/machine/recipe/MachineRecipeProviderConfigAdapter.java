package conductance.api.machine.recipe;

import java.util.Map;

public interface MachineRecipeProviderConfigAdapter {

	NCRecipeType[] getRecipeTypes();

	NCRecipeType getRecipeType();

	int getActiveRecipeType();

	void setActiveRecipeType(int type);

	Map<IRecipeElementType<?>, Integer> getOutputLimits();

	default boolean keepTickables() {
		return true;
	}

	default boolean isProcessingAvailable() {
		return true;
	}

	RecipeProcessor getRecipeProcessor();

	default void beforeWorking() {
	}

	default void onWorking() {
	}

	default void onWaiting() {
	}

	default void afterWorking() {
	}
}