package conductance.api.recipe;

public interface AutoRecipeDataCallback {

	void accept(int inAmount, int outAmount, int time, long energy);
}
