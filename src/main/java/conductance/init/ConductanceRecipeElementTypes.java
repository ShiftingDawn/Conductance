package conductance.init;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
import com.mojang.serialization.Codec;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import conductance.api.recipe.RecipeModifier;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.recipe.event.RegisterRecipeElementTypeEvent;
import conductance.Conductance;
import static conductance.api.NCRecipeElementTypes.ENERGY;
import static conductance.api.NCRecipeElementTypes.FLUID;
import static conductance.api.NCRecipeElementTypes.ITEM;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceRecipeElementTypes {

	@EventListener(priority = -100)
	private static void init(final RegisterRecipeElementTypeEvent event) {
		ITEM = event.register("item", SizedIngredient.FLAT_CODEC, SizedIngredient.STREAM_CODEC, ConductanceRecipeElementTypes::copyItem);
		FLUID = event.register("fluid", SizedFluidIngredient.FLAT_CODEC, SizedFluidIngredient.STREAM_CODEC, ConductanceRecipeElementTypes::copyFluid);
		ENERGY = event.register("energy", Codec.LONG, StreamCodec.of(FriendlyByteBuf::writeVarLong, FriendlyByteBuf::readVarLong), ConductanceRecipeElementTypes::copyEnergy);
	}

	private static SizedIngredient copyItem(final SizedIngredient obj, final RecipeModifier mod) {
		if (obj.ingredient().isEmpty()) {
			return new SizedIngredient(Ingredient.EMPTY, 1);
		}
		return new SizedIngredient(obj.ingredient(), mod.apply(obj.count()).intValue());
	}

	private static SizedFluidIngredient copyFluid(final SizedFluidIngredient obj, final RecipeModifier mod) {
		if (obj.ingredient().isEmpty()) {
			return new SizedFluidIngredient(FluidIngredient.empty(), 1);
		}
		return new SizedFluidIngredient(obj.ingredient(), mod.apply(obj.amount()).intValue());
	}

	private static Long copyEnergy(final Long obj, final RecipeModifier mod) {
		return mod.apply(obj).longValue();
	}

	private ConductanceRecipeElementTypes() {
	}
}
