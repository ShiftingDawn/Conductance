package conductance.api.recipe.event;

import net.minecraft.tags.TagKey;
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
import conductance.api.recipe.MachineRecipe;
import conductance.api.recipe.RecipeElementType;
import conductance.api.util.IO;

public interface MachineRecipeBuilder {

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

	MachineRecipe build();
}
