package conductance.api.recipe.event;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import conductance.api.CAPI;
import conductance.api.NCRecipeElementTypes;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.recipe.RecipeDataToken;
import conductance.api.recipe.RecipeElement;
import conductance.api.recipe.RecipeElementType;
import conductance.api.tier.Tier;
import conductance.api.util.IO;

public interface MachineRecipeBuilder {

	MachineRecipeBuilder chance(double chance);

	default MachineRecipeBuilder change1() {
		return this.chance(1);
	}

	MachineRecipeBuilder perTick(boolean perTick);

	<T> MachineRecipeBuilder add(IO io, RecipeElementType<T> elementType, T obj);

	default <T> MachineRecipeBuilder in(final RecipeElementType<T> elementType, final T obj) {
		return this.add(IO.IN, elementType, obj);
	}

	default MachineRecipeBuilder in(final SizedIngredient ingredient) {
		return this.in(NCRecipeElementTypes.ITEM, ingredient);
	}

	default MachineRecipeBuilder in(final Ingredient ingredient, final int count) {
		return this.in(NCRecipeElementTypes.ITEM, new SizedIngredient(ingredient, count));
	}

	default MachineRecipeBuilder in(final ItemStack ingredient) {
		return this.in(ingredient.getItem(), ingredient.getCount());
	}

	default MachineRecipeBuilder in(final ItemLike ingredient, final int count) {
		return this.in(NCRecipeElementTypes.ITEM, SizedIngredient.of(ingredient, count));
	}

	default MachineRecipeBuilder in(final ItemLike ingredient) {
		return this.in(ingredient, 1);
	}

	default MachineRecipeBuilder in(final Holder<? extends Item> ingredient, final int count) {
		return this.in(ingredient.value(), count);
	}

	default MachineRecipeBuilder in(final Holder<? extends Item> ingredient) {
		return this.in(ingredient, 1);
	}

	default MachineRecipeBuilder in(final Supplier<? extends Item> ingredient, final int count) {
		return this.in(ingredient.get(), count);
	}

	default MachineRecipeBuilder in(final Supplier<? extends Item> ingredient) {
		return this.in(ingredient, 1);
	}

	default MachineRecipeBuilder in(final SizedFluidIngredient ingredient) {
		return this.in(NCRecipeElementTypes.FLUID, ingredient);
	}

	default MachineRecipeBuilder in(final FluidIngredient ingredient, final int amount) {
		return this.in(NCRecipeElementTypes.FLUID, new SizedFluidIngredient(ingredient, amount));
	}

	default MachineRecipeBuilder in(final FluidStack ingredient) {
		return this.in(ingredient.getFluid(), ingredient.getAmount());
	}

	default MachineRecipeBuilder in(final Fluid ingredient, final int amount) {
		return this.in(NCRecipeElementTypes.FLUID, SizedFluidIngredient.of(ingredient, amount));
	}

	default MachineRecipeBuilder in(final Fluid ingredient) {
		return this.in(ingredient, FluidType.BUCKET_VOLUME);
	}

	MachineRecipeBuilder in(TagKey<?> tag, int count);

	default MachineRecipeBuilder in(final TagKey<?> tagKey) {
		return this.in(tagKey, -1);
	}

	default MachineRecipeBuilder in(final Material material, final MaterialGenerationHandler handler, final int count) {
		if (handler.hasItem() || handler.hasBlock()) {
			this.in(CAPI.materials().getItemTag(material, handler), count);
		} else if (handler.hasFluid()) {
			this.in(CAPI.materials().getFluidTag(material, handler), count);
		}
		return this;
	}

	default MachineRecipeBuilder in(final Material material, final MaterialGenerationHandler handler) {
		return this.in(material, handler, 1);
	}

	default <T> MachineRecipeBuilder nc(final RecipeElementType<T> elementType, final T obj) {
		this.chance(0);
		this.add(IO.IN, elementType, obj);
		this.change1();
		return this;
	}

	default MachineRecipeBuilder nc(final SizedIngredient ingredient) {
		return this.nc(NCRecipeElementTypes.ITEM, ingredient);
	}

	default MachineRecipeBuilder nc(final Ingredient ingredient, final int count) {
		return this.nc(NCRecipeElementTypes.ITEM, new SizedIngredient(ingredient, count));
	}

	default MachineRecipeBuilder nc(final ItemStack ingredient) {
		return this.nc(ingredient.getItem(), ingredient.getCount());
	}

	default MachineRecipeBuilder nc(final ItemLike ingredient, final int count) {
		return this.nc(NCRecipeElementTypes.ITEM, SizedIngredient.of(ingredient, count));
	}

	default MachineRecipeBuilder nc(final ItemLike ingredient) {
		return this.nc(ingredient, 1);
	}

	default MachineRecipeBuilder nc(final Holder<? extends Item> ingredient, final int count) {
		return this.nc(ingredient.value(), count);
	}

	default MachineRecipeBuilder nc(final Holder<? extends Item> ingredient) {
		return this.nc(ingredient, 1);
	}

	default MachineRecipeBuilder nc(final Supplier<? extends Item> ingredient, final int count) {
		return this.nc(ingredient.get(), count);
	}

	default MachineRecipeBuilder nc(final Supplier<? extends Item> ingredient) {
		return this.nc(ingredient, 1);
	}

	default MachineRecipeBuilder nc(final SizedFluidIngredient ingredient) {
		return this.nc(NCRecipeElementTypes.FLUID, ingredient);
	}

	default MachineRecipeBuilder nc(final FluidIngredient ingredient, final int amount) {
		return this.nc(NCRecipeElementTypes.FLUID, new SizedFluidIngredient(ingredient, amount));
	}

	default MachineRecipeBuilder nc(final FluidStack ingredient) {
		return this.nc(ingredient.getFluid(), ingredient.getAmount());
	}

	default MachineRecipeBuilder nc(final Fluid ingredient, final int amount) {
		return this.nc(NCRecipeElementTypes.FLUID, SizedFluidIngredient.of(ingredient, amount));
	}

	default MachineRecipeBuilder nc(final Fluid ingredient) {
		return this.nc(ingredient, FluidType.BUCKET_VOLUME);
	}

	MachineRecipeBuilder nc(TagKey<?> tag, int count);

	default MachineRecipeBuilder nc(final TagKey<?> tagKey) {
		return this.nc(tagKey, -1);
	}

	default MachineRecipeBuilder nc(final Material material, final MaterialGenerationHandler handler, final int count) {
		if (handler.hasItem() || handler.hasBlock()) {
			this.nc(CAPI.materials().getItemTag(material, handler), count);
		} else if (handler.hasFluid()) {
			this.nc(CAPI.materials().getFluidTag(material, handler), count);
		}
		return this;
	}

	default MachineRecipeBuilder nc(final Material material, final MaterialGenerationHandler handler) {
		return this.nc(material, handler, 1);
	}

	default <T> MachineRecipeBuilder out(final RecipeElementType<T> elementType, final T obj) {
		return this.add(IO.OUT, elementType, obj);
	}

	default MachineRecipeBuilder out(final SizedIngredient ingredient) {
		return this.out(NCRecipeElementTypes.ITEM, ingredient);
	}

	default MachineRecipeBuilder out(final Ingredient ingredient, final int count) {
		return this.out(NCRecipeElementTypes.ITEM, new SizedIngredient(ingredient, count));
	}

	default MachineRecipeBuilder out(final ItemStack ingredient) {
		return this.out(ingredient.getItem(), ingredient.getCount());
	}

	default MachineRecipeBuilder out(final ItemLike ingredient, final int count) {
		return this.out(NCRecipeElementTypes.ITEM, SizedIngredient.of(ingredient, count));
	}

	default MachineRecipeBuilder out(final ItemLike ingredient) {
		return this.out(ingredient, 1);
	}

	default MachineRecipeBuilder out(final Holder<? extends Item> ingredient, final int count) {
		return this.out(ingredient.value(), count);
	}

	default MachineRecipeBuilder out(final Holder<? extends Item> ingredient) {
		return this.out(ingredient, 1);
	}

	default MachineRecipeBuilder out(final Supplier<? extends Item> ingredient, final int count) {
		return this.out(ingredient.get(), count);
	}

	default MachineRecipeBuilder out(final Supplier<? extends Item> ingredient) {
		return this.out(ingredient, 1);
	}

	default MachineRecipeBuilder out(final SizedFluidIngredient ingredient) {
		return this.out(NCRecipeElementTypes.FLUID, ingredient);
	}

	default MachineRecipeBuilder out(final FluidIngredient ingredient, final int amount) {
		return this.out(NCRecipeElementTypes.FLUID, new SizedFluidIngredient(ingredient, amount));
	}

	default MachineRecipeBuilder out(final FluidStack ingredient) {
		return this.out(ingredient.getFluid(), ingredient.getAmount());
	}

	default MachineRecipeBuilder out(final Fluid ingredient, final int amount) {
		return this.out(NCRecipeElementTypes.FLUID, SizedFluidIngredient.of(ingredient, amount));
	}

	default MachineRecipeBuilder out(final Fluid ingredient) {
		return this.out(ingredient, FluidType.BUCKET_VOLUME);
	}

	MachineRecipeBuilder out(TagKey<?> tag, int count);

	default MachineRecipeBuilder out(final TagKey<?> tagKey) {
		return this.out(tagKey, -1);
	}

	default MachineRecipeBuilder out(final Material material, final MaterialGenerationHandler handler, final int count) {
		if (handler.hasItem() || handler.hasBlock()) {
			this.out(CAPI.materials().getItemTag(material, handler), count);
		} else if (handler.hasFluid()) {
			this.out(CAPI.materials().getFluidTag(material, handler), count);
		}
		return this;
	}

	default MachineRecipeBuilder out(final Material material, final MaterialGenerationHandler handler) {
		return this.out(material, handler, 1);
	}

	MachineRecipeBuilder duration(int recipeDuration);

	MachineRecipeBuilder program(int program);

	default MachineRecipeBuilder energyIn(final long energyPerTick) {
		return this.perTick(true).in(NCRecipeElementTypes.ENERGY, energyPerTick).perTick(false);
	}

	default MachineRecipeBuilder energyIn(final Tier tier) {
		return this.energyIn(tier.getRecipeVoltage());
	}

	default MachineRecipeBuilder energyOut(final long energyPerTick) {
		return this.perTick(true).out(NCRecipeElementTypes.ENERGY, energyPerTick).perTick(false);
	}

	<T> MachineRecipeBuilder data(RecipeDataToken<T> token, T value);

	MachineRecipeBuilder copy();

	Map<RecipeElementType<?>, List<RecipeElement>> getInputs();

	Map<RecipeElementType<?>, List<RecipeElement>> getOutputs();

	int getProgram();

	int getDuration();

	double getCurrentChance();
}
