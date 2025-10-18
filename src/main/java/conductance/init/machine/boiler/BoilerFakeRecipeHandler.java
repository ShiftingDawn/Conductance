package conductance.init.machine.boiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.NCMaterialGenerationHandlers;
import conductance.api.NCMaterials;
import conductance.api.NCRecipeElementTypes;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.RecipeHandler;
import conductance.api.machine.RecipePair;
import conductance.api.recipe.DummyMachineRecipe;
import conductance.api.recipe.MachineRecipe;
import conductance.api.recipe.RecipeDataMap;
import conductance.api.recipe.RecipeElement;

final class BoilerFakeRecipeHandler extends RecipeHandler {

	private final BoilerFakeRecipeCapabilityHolder holder;

	BoilerFakeRecipeHandler(final MachineBlockEntity<?> machine, final BoilerFakeRecipeCapabilityHolder holder) {
		super("boiler", machine, holder);
		this.holder = holder;
	}

	@Override
	protected @Nullable RecipePair findRecipe() {
		if (this.holder.getWaterTank() == null || this.holder.getSteamTank() == null) {
			return null;
		}
		final int fuel = this.holder.getFuelForInput();
		if (fuel == 0) {
			return null;
		}
		final MachineRecipe recipe = new DummyMachineRecipe(
			this.holder.getRecipeType(),
			CAPI.make(new HashMap<>(), map -> {
				this.holder.addInputs((type, data) ->
					map.computeIfAbsent(type, k -> new ArrayList<>()).add(new RecipeElement(data, 1))
				);
			}),
			Map.of(),
			Map.of(NCRecipeElementTypes.FLUID, CAPI.make(new ArrayList<>(), list -> {
				list.add(new RecipeElement(BoilerFakeRecipeHandler.makeWaterIngredient(1), 1));
			})),
			Map.of(NCRecipeElementTypes.FLUID, CAPI.make(new ArrayList<>(), list -> {
				list.add(new RecipeElement(BoilerFakeRecipeHandler.makeSteamIngredient(64), 1));
			})),
			fuel,
			0,
			new RecipeDataMap(List.of())
		);
		if (this.holder.getRecipeModifier() != null) {
			return new RecipePair(recipe, this.holder.getRecipeModifier().modifyRecipe(recipe));
		}
		return new RecipePair(recipe, null);
	}

	private static SizedFluidIngredient makeWaterIngredient(final int amount) {
		final TagKey<Fluid> waterTag = CAPI.materials().getFluidTag(NCMaterials.WATER, NCMaterialGenerationHandlers.LIQUID);
		return new SizedFluidIngredient(FluidIngredient.of(BuiltInRegistries.FLUID.getOrThrow(waterTag)), amount);
	}

	private static SizedFluidIngredient makeSteamIngredient(final int amount) {
		final TagKey<Fluid> waterTag = CAPI.materials().getFluidTag(NCMaterials.STEAM, NCMaterialGenerationHandlers.GAS);
		return new SizedFluidIngredient(FluidIngredient.of(BuiltInRegistries.FLUID.getOrThrow(waterTag)), amount);
	}
}
