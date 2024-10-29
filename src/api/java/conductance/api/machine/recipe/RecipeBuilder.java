package conductance.api.machine.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
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
import conductance.api.NCRecipeElementTypes;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.util.MiscUtils;

public interface RecipeBuilder {

	RecipeBuilder perTick(boolean perTick);

	RecipeBuilder chance(int chance, int maxChance);

	default RecipeBuilder chance1() {
		return this.chance(100, 100);
	}

	<T> RecipeBuilder add(boolean input, IRecipeElementType<T> type, T obj);

	//region IN
	default <T> RecipeBuilder in(final IRecipeElementType<T> type, final T obj) {
		return this.add(true, type, obj);
	}

	//region Item IN
	default RecipeBuilder in(final SizedIngredient item) {
		return this.in(NCRecipeElementTypes.ITEM, item);
	}

	default RecipeBuilder in(final Ingredient item, final int count) {
		return this.in(new SizedIngredient(item, count));
	}

	default RecipeBuilder in(final ItemStack item) {
		return this.in(new SizedIngredient(Ingredient.of(item), item.getCount()));
	}

	default RecipeBuilder in(final ItemLike item, final int count) {
		return this.in(SizedIngredient.of(item, count));
	}

	default RecipeBuilder in(final ItemLike item) {
		return this.in(SizedIngredient.of(item, 1));
	}
	//endregion

	//region Fluid IN
	default RecipeBuilder in(final SizedFluidIngredient fluid) {
		return this.in(NCRecipeElementTypes.FLUID, fluid);
	}

	default RecipeBuilder in(final FluidIngredient fluid, final int amount) {
		return this.in(new SizedFluidIngredient(fluid, amount));
	}

	default RecipeBuilder in(final FluidStack fluid) {
		return this.in(SizedFluidIngredient.of(fluid));
	}

	default RecipeBuilder in(final Fluid fluid, final int amount) {
		return this.in(SizedFluidIngredient.of(fluid, amount));
	}

	default RecipeBuilder in(final Fluid fluid) {
		return this.in(fluid, FluidType.BUCKET_VOLUME);
	}
	//endregion

	@SuppressWarnings("unchecked")
	default RecipeBuilder in(final TagKey<?> tag, final int count) {
		if (tag.registry() == Registries.ITEM) {
			return this.in(SizedIngredient.of((TagKey<Item>) tag, Math.abs(count)));
		} else if (tag.registry() == Registries.FLUID) {
			return this.in(SizedFluidIngredient.of((TagKey<Fluid>) tag, Math.abs(count) * (count < 0 ? FluidType.BUCKET_VOLUME : 1)));
		}
		return this;
	}

	default RecipeBuilder in(final TagKey<?> tagKey) {
		return this.in(tagKey, -1);
	}

	default RecipeBuilder in(final TaggedMaterialSet taggedSet, final Material material, final int count) {
		if (taggedSet.isItemGenerator() || taggedSet.isBlockGenerator()) {
			this.in(MiscUtils.getItemTag(taggedSet, material), count);
		} else if (taggedSet.isFluidGenerator()) {
			this.in(MiscUtils.getFluidTag(taggedSet, material), count);
		}
		return this;
	}

	default RecipeBuilder in(final TaggedMaterialSet taggedSet, final Material material) {
		return this.in(taggedSet, material, 1);
	}
	//endregion

	//region OUT
	default <T> RecipeBuilder out(final IRecipeElementType<T> type, final T obj) {
		return this.add(false, type, obj);
	}

	//region Item OUT
	default RecipeBuilder out(final SizedIngredient item) {
		return this.out(NCRecipeElementTypes.ITEM, item);
	}

	default RecipeBuilder out(final Ingredient item, final int count) {
		return this.out(new SizedIngredient(item, count));
	}

	default RecipeBuilder out(final ItemStack item) {
		return this.out(new SizedIngredient(Ingredient.of(item), item.getCount()));
	}

	default RecipeBuilder out(final ItemLike item, final int count) {
		return this.out(SizedIngredient.of(item, count));
	}

	default RecipeBuilder out(final ItemLike item) {
		return this.out(SizedIngredient.of(item, 1));
	}
	//endregion

	//region Fluid OUT
	default RecipeBuilder out(final SizedFluidIngredient fluid) {
		return this.out(NCRecipeElementTypes.FLUID, fluid);
	}

	default RecipeBuilder out(final FluidIngredient fluid, final int amount) {
		return this.out(new SizedFluidIngredient(fluid, amount));
	}

	default RecipeBuilder out(final FluidStack fluid) {
		return this.out(SizedFluidIngredient.of(fluid));
	}

	default RecipeBuilder out(final Fluid fluid, final int amount) {
		return this.out(SizedFluidIngredient.of(fluid, amount));
	}

	default RecipeBuilder out(final Fluid fluid) {
		return this.out(fluid, FluidType.BUCKET_VOLUME);
	}
	//endregion

	@SuppressWarnings("unchecked")
	default RecipeBuilder out(final TagKey<?> tag, final int count) {
		if (tag.registry() == Registries.ITEM) {
			return this.out(SizedIngredient.of((TagKey<Item>) tag, Math.abs(count)));
		} else if (tag.registry() == Registries.FLUID) {
			return this.out(SizedFluidIngredient.of((TagKey<Fluid>) tag, Math.abs(count) * (count < 0 ? FluidType.BUCKET_VOLUME : 1)));
		}
		return this;
	}

	default RecipeBuilder out(final TagKey<?> tagKey) {
		return this.out(tagKey, -1);
	}

	default RecipeBuilder out(final TaggedMaterialSet taggedSet, final Material material, final int count) {
		if (taggedSet.isItemGenerator() || taggedSet.isBlockGenerator()) {
			this.out(MiscUtils.getItemTag(taggedSet, material), count);
		} else if (taggedSet.isFluidGenerator()) {
			this.out(MiscUtils.getFluidTag(taggedSet, material), count);
		}
		return this;
	}

	default RecipeBuilder out(final TaggedMaterialSet taggedSet, final Material material) {
		return this.out(taggedSet, material, 1);
	}
	//endregion

	IRecipe build();

	void save(RecipeOutput output);
}
