package conductance.init.machine.boiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
import conductance.api.recipe.RecipeElement;

final class BoilerFakeRecipeHandler extends RecipeHandler {

	public static final int BASE_MAX_PRODUCTION = 120 / 4; //4 Times per second
	private static final int WATER_TO_STEAM = 10;
	private final BoilerFakeRecipeCapabilityHolder holder;
	private final int maxProduction;
	private final int maxWater;
	private int fuelTime;

	BoilerFakeRecipeHandler(final MachineBlockEntity<?> machine, final BoilerFakeRecipeCapabilityHolder holder, final int maxProduction) {
		super("boiler", machine, holder);
		this.holder = holder;
		this.maxProduction = maxProduction;
		this.maxWater = this.maxProduction / BoilerFakeRecipeHandler.WATER_TO_STEAM;
	}

	@Override
	public void serialize(final ValueOutput output) {
		super.serialize(output);
		output.putInt("fuel", this.fuelTime);
	}

	@Override
	public void deserialize(final ValueInput input) {
		super.deserialize(input);
		this.fuelTime = input.getIntOr("fuel", 0);
	}

	@Override
	protected @Nullable RecipePair findRecipe() {
		if (this.holder.getWaterTank() == null || this.holder.getSteamTank() == null) {
			return null;
		}
		final boolean hadFuel = this.fuelTime > 0;
		if (this.fuelTime == 0) {
			final int fuel = this.holder.getFuelForInput();
			if (fuel == 0) {
				return null;
			}
			this.fuelTime = fuel;
			this.setChanged();
		}
		int water = this.holder.getWaterTank().getAvailableContent().stream().mapToInt(SizedFluidIngredient::amount).sum();
		if (water == 0) {
			return null;
		}
		if (water > this.maxWater) {
			water = this.maxWater;
		}
		final int space = this.holder.getSteamTank().getMaxSpaceForContent(BoilerFakeRecipeHandler.makeSteamIngredient(this.maxProduction));
		if (space == 0) {
			return null;
		}
		int maxProduce = this.fuelTime * 12;
		if (maxProduce > this.maxProduction) {
			maxProduce = this.maxProduction;
		}
		if (maxProduce > space) {
			maxProduce = space;
		}
		if (maxProduce > water * BoilerFakeRecipeHandler.WATER_TO_STEAM) {
			maxProduce = water * BoilerFakeRecipeHandler.WATER_TO_STEAM;
		}
		final int fuelToConsume = maxProduce / 12;
		this.fuelTime -= fuelToConsume;
		maxProduce = fuelToConsume * 12;
		if (maxProduce == 0) {
			return null;
		}
		final int finalProduce = maxProduce;
		final MachineRecipe recipe = new DummyMachineRecipe(
			this.holder.getRecipeType(),
			CAPI.make(new HashMap<>(), map -> {
				if (!hadFuel) {
					this.holder.addInputs((type, data) -> {
						map.computeIfAbsent(type, k -> new ArrayList<>())
							.add(new RecipeElement(data, 1));
					});
				}
				map.computeIfAbsent(NCRecipeElementTypes.FLUID, k -> new ArrayList<>())
					.add(new RecipeElement(BoilerFakeRecipeHandler.makeWaterIngredient(finalProduce / BoilerFakeRecipeHandler.WATER_TO_STEAM), 1));
			}),
			Map.of(NCRecipeElementTypes.FLUID, List.of(new RecipeElement(BoilerFakeRecipeHandler.makeSteamIngredient(finalProduce), 1))),
			Map.of(),
			Map.of(),
			5,
			0
		);
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
