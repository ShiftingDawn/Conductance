package conductance.core.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import lombok.RequiredArgsConstructor;
import conductance.api.recipe.MachineRecipe;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.recipe.RecipeElementType;
import conductance.api.recipe.RecipeObject;
import conductance.api.recipe.event.MachineRecipeBuilder;
import conductance.api.util.IO;

@RequiredArgsConstructor
public final class MachineRecipeBuilderImpl implements MachineRecipeBuilder {

	private final Map<RecipeElementType<?>, List<RecipeObject>> inputs = new HashMap<>();
	private final Map<RecipeElementType<?>, List<RecipeObject>> outputs = new HashMap<>();
	private int recipeDuration = 200;
	private int recipeProgram = -1;
	private final MachineRecipeType recipeType;
	private final HolderLookup.Provider registries;

	@Override
	public <T> MachineRecipeBuilder add(final IO io, final RecipeElementType<T> elementType, final T obj) {
		switch (io) {
			case IN -> this.inputs.computeIfAbsent(elementType, k -> new ArrayList<>()).add(new RecipeObject(obj));
			case OUT -> this.outputs.computeIfAbsent(elementType, k -> new ArrayList<>()).add(new RecipeObject(obj));
		}
		return this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public MachineRecipeBuilder in(final TagKey<?> tag, final int count) {
		if (tag.registry() == Registries.ITEM) {
			return this.in(Ingredient.of(this.registries.lookupOrThrow(Registries.ITEM).getOrThrow((TagKey<Item>) tag)), Math.abs(count));
		} else if (tag.registry() == Registries.FLUID) {
			return this.in(FluidIngredient.of(BuiltInRegistries.FLUID.getOrThrow((TagKey<Fluid>) tag)), Math.abs(count) * (count < 0 ? FluidType.BUCKET_VOLUME : 1));
		}
		return this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public MachineRecipeBuilder out(final TagKey<?> tag, final int count) {
		if (tag.registry() == Registries.ITEM) {
			return this.out(Ingredient.of(this.registries.lookupOrThrow(Registries.ITEM).getOrThrow((TagKey<Item>) tag)), Math.abs(count));
		} else if (tag.registry() == Registries.FLUID) {
			return this.out(FluidIngredient.of(BuiltInRegistries.FLUID.getOrThrow((TagKey<Fluid>) tag)), Math.abs(count) * (count < 0 ? FluidType.BUCKET_VOLUME : 1));
		}
		return this;
	}

	@Override
	public MachineRecipeBuilder duration(final int recipeDuration) {
		this.recipeDuration = recipeDuration;
		return this;
	}

	@Override
	public MachineRecipeBuilder program(final int program) {
		if (program < -1 || program > 24) {
			throw new IllegalArgumentException("Recipe program must adhere to -1 <= program <= 24");
		}
		this.recipeProgram = program;
		return this;
	}

	@Override
	public MachineRecipe build() {
		return new MachineRecipeImpl(this.recipeType, this.inputs, this.outputs, this.recipeDuration, this.recipeProgram);
	}

	public void save(final ResourceLocation recipeId, final RecipeOutput output) {
		output.accept(ResourceKey.create(Registries.RECIPE, recipeId), this.build(), null);
	}
}
