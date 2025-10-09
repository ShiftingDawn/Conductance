package conductance.init.machine;

import java.util.ArrayList;
import java.util.HashMap;
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
import conductance.api.recipe.DummyMachineRecipe;
import conductance.api.recipe.MachineRecipe;
import conductance.api.recipe.RecipeElement;

public class BoilerFakeRecipeHandler extends RecipeHandler {

	private static final int MAX_PRODUCTION = 120 / 4; //4 Times per second
	private static final int WATER_TO_STEAM = 10;
	private static final int MAX_WATER = BoilerFakeRecipeHandler.MAX_PRODUCTION / BoilerFakeRecipeHandler.WATER_TO_STEAM;
	private final BoilerFakeRecipeCapabilityHolder holder;
	private int fuelTime;

	public BoilerFakeRecipeHandler(final MachineBlockEntity<?> machine, final BoilerFakeRecipeCapabilityHolder holder) {
		super("boiler", machine, holder);
		this.holder = holder;
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
	protected @Nullable MachineRecipe findRecipe() {
		final boolean hadFuel = this.fuelTime > 0;
		if (this.fuelTime == 0) {
			final int fuel = this.holder.getFuelForInput();
			if (fuel == 0) {
				return null;
			}
			this.fuelTime = fuel;
			this.setChanged();
		}
		int water = this.holder.getWaterTank().getFluidInTank(0).getAmount();
		if (water == 0) {
			return null;
		}
		if (water > BoilerFakeRecipeHandler.MAX_WATER) {
			water = BoilerFakeRecipeHandler.MAX_WATER;
		}
		final int space = this.holder.getSteamTank().getTankCapacity(0) - this.holder.getSteamTank().getFluidInTank(0).getAmount();
		if (space == 0) {
			return null;
		}
		int maxProduce = this.fuelTime * 12;
		if (maxProduce > BoilerFakeRecipeHandler.MAX_PRODUCTION) {
			maxProduce = BoilerFakeRecipeHandler.MAX_PRODUCTION;
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
		return new DummyMachineRecipe(
			null,
			CAPI.make(new HashMap<>(), map -> {
				if (!hadFuel) {
					this.holder.addInputs((type, data) -> {
						map.computeIfAbsent(type, k -> new ArrayList<>())
							.add(new RecipeElement(data, 1));
					});
				}
				final TagKey<Fluid> waterTag = CAPI.materials().getFluidTag(NCMaterials.WATER, NCMaterialGenerationHandlers.LIQUID);
				map.computeIfAbsent(NCRecipeElementTypes.FLUID, k -> new ArrayList<>())
					.add(new RecipeElement(new SizedFluidIngredient(FluidIngredient.of(BuiltInRegistries.FLUID.getOrThrow(waterTag)), finalProduce / BoilerFakeRecipeHandler.WATER_TO_STEAM), 1));
			}),
			Map.of(NCRecipeElementTypes.FLUID, CAPI.make(new ArrayList<>(), list -> {
				final TagKey<Fluid> steamTag = CAPI.materials().getFluidTag(NCMaterials.STEAM, NCMaterialGenerationHandlers.GAS);
				list.add(new RecipeElement(new SizedFluidIngredient(FluidIngredient.of(BuiltInRegistries.FLUID.getOrThrow(steamTag)), finalProduce), 1));
			})),
			Map.of(),
			Map.of(),
			5,
			0
		);
	}
}
