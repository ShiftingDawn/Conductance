package conductance.api;

import conductance.api.material.MaterialGenerationHandler;

@SuppressWarnings("NotNullFieldNotInitialized")
public final class NCMaterialGenerationHandlers {

	public static MaterialGenerationHandler DUST;
	public static MaterialGenerationHandler INGOT;
	public static MaterialGenerationHandler GEM;
	public static MaterialGenerationHandler BLOCK;
	public static MaterialGenerationHandler NUGGET;

	public static MaterialGenerationHandler PLATE;
	public static MaterialGenerationHandler ROD;

	private NCMaterialGenerationHandlers() {
	}
}
