package conductance.init;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import com.mojang.serialization.Codec;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import conductance.api.NCRecipeElementTypes;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.recipe.RecipeModifier;
import conductance.api.recipe.event.RegisterRecipeElementTypeEvent;
import conductance.Conductance;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceRecipeElementTypes {

	@EventListener(priority = -100)
	private static void init(final RegisterRecipeElementTypeEvent event) {
		NCRecipeElementTypes.ITEM = event.register("item", SizedIngredient.NESTED_CODEC, SizedIngredient.STREAM_CODEC, ConductanceRecipeElementTypes::itemCloner);
		NCRecipeElementTypes.FLUID = event.register("fluid", SizedFluidIngredient.CODEC, SizedFluidIngredient.STREAM_CODEC, ConductanceRecipeElementTypes::fluidCloner);
		NCRecipeElementTypes.ENERGY = event.register("energy", Codec.LONG, StreamCodec.of(FriendlyByteBuf::writeVarLong, FriendlyByteBuf::readVarLong), ConductanceRecipeElementTypes::energyCloner);
	}

	private static SizedIngredient itemCloner(final SizedIngredient ingredient, final RecipeModifier modifier) {
		return new SizedIngredient(ingredient.ingredient(), modifier.apply(ingredient.count()).intValue());
	}

	private static SizedFluidIngredient fluidCloner(final SizedFluidIngredient ingredient, final RecipeModifier modifier) {
		return new SizedFluidIngredient(ingredient.ingredient(), modifier.apply(ingredient.amount()).intValue());
	}

	private static Long energyCloner(final Long energy, final RecipeModifier modifier) {
		return modifier.apply(energy).longValue();
	}

	private ConductanceRecipeElementTypes() {
	}
}
