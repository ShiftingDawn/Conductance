package conductance.init;

import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import conductance.api.NCRecipeElementTypes;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.recipe.event.RegisterRecipeElementTypeEvent;
import conductance.Conductance;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceRecipeElementTypes {

	@EventListener(priority = -100)
	private static void init(final RegisterRecipeElementTypeEvent event) {
		NCRecipeElementTypes.ITEM = event.register("item", SizedIngredient.NESTED_CODEC, SizedIngredient.STREAM_CODEC);
		NCRecipeElementTypes.FLUID = event.register("fluid", SizedFluidIngredient.CODEC, SizedFluidIngredient.STREAM_CODEC);
	}

	private ConductanceRecipeElementTypes() {
	}
}
