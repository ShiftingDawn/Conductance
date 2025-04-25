package conductance.api.machine.recipe;

import java.util.List;
import java.util.Map;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
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

	RecipeBuilder tieredChanceBoost(int tieredChanceBoost);

	RecipeBuilder processTime(int processTime);

	<T> RecipeBuilder add(boolean input, IRecipeElementType<T> type, T obj);

	default <T> RecipeBuilder in(final IRecipeElementType<T> type, final T obj) {
		return this.add(true, type, obj);
	}

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

	RecipeBuilder inEnergy(long energy);

	RecipeBuilder program(int program);

	<T> RecipeBuilder inNc(IRecipeElementType<T> type, T obj);

	default RecipeBuilder inNc(final SizedIngredient item) {
		return this.inNc(NCRecipeElementTypes.ITEM, item);
	}

	default RecipeBuilder inNc(final Ingredient item, final int count) {
		return this.inNc(new SizedIngredient(item, count));
	}

	default RecipeBuilder inNc(final ItemStack item) {
		return this.inNc(new SizedIngredient(Ingredient.of(item), item.getCount()));
	}

	default RecipeBuilder inNc(final ItemLike item, final int count) {
		return this.inNc(SizedIngredient.of(item, count));
	}

	default RecipeBuilder inNc(final ItemLike item) {
		return this.inNc(SizedIngredient.of(item, 1));
	}

	default RecipeBuilder inNc(final SizedFluidIngredient fluid) {
		return this.inNc(NCRecipeElementTypes.FLUID, fluid);
	}

	default RecipeBuilder inNc(final FluidIngredient fluid, final int amount) {
		return this.inNc(new SizedFluidIngredient(fluid, amount));
	}

	default RecipeBuilder inNc(final FluidStack fluid) {
		return this.inNc(SizedFluidIngredient.of(fluid));
	}

	default RecipeBuilder inNc(final Fluid fluid, final int amount) {
		return this.inNc(SizedFluidIngredient.of(fluid, amount));
	}

	default RecipeBuilder inNc(final Fluid fluid) {
		return this.inNc(fluid, FluidType.BUCKET_VOLUME);
	}

	@SuppressWarnings("unchecked")
	default RecipeBuilder inNc(final TagKey<?> tag, final int count) {
		if (tag.registry() == Registries.ITEM) {
			return this.inNc(SizedIngredient.of((TagKey<Item>) tag, Math.abs(count)));
		} else if (tag.registry() == Registries.FLUID) {
			return this.inNc(SizedFluidIngredient.of((TagKey<Fluid>) tag, Math.abs(count) * (count < 0 ? FluidType.BUCKET_VOLUME : 1)));
		}
		return this;
	}

	default RecipeBuilder inNc(final TagKey<?> tagKey) {
		return this.inNc(tagKey, -1);
	}

	default RecipeBuilder inNc(final TaggedMaterialSet taggedSet, final Material material, final int count) {
		if (taggedSet.isItemGenerator() || taggedSet.isBlockGenerator()) {
			this.inNc(MiscUtils.getItemTag(taggedSet, material), count);
		} else if (taggedSet.isFluidGenerator()) {
			this.inNc(MiscUtils.getFluidTag(taggedSet, material), count);
		}
		return this;
	}

	default RecipeBuilder inNc(final TaggedMaterialSet taggedSet, final Material material) {
		return this.inNc(taggedSet, material, 1);
	}

	default <T> RecipeBuilder out(final IRecipeElementType<T> type, final T obj) {
		return this.add(false, type, obj);
	}

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

	RecipeBuilder outEnergy(long energy);

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

	IRecipe build();

	void save(RecipeOutput output);

	RecipeBuilder copy(NCRecipeType recipeType, ResourceLocation newId);

	RecipeBuilder copy(NCRecipeType recipeType, String newId);

	RecipeBuilder copy(ResourceLocation newId);

	RecipeBuilder copy(String newId);

	Map<IRecipeElementType<?>, List<RecipeElement>> getInputs();

	Map<IRecipeElementType<?>, List<RecipeElement>> getOutputs();

	Map<IRecipeElementType<?>, List<RecipeElement>> getInputsPerTick();

	Map<IRecipeElementType<?>, List<RecipeElement>> getOutputsPerTick();

	ResourceLocation getRecipeId();

	int getProcessTime();

	long getEnergyPerTick();
}
