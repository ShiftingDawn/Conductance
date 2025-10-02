package conductance.core.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.recipe.RecipeElement;
import conductance.api.recipe.RecipeElementType;
import conductance.api.recipe.event.MachineRecipeBuilder;
import conductance.api.recipe.event.RecipeBuilderCallback;
import conductance.api.util.IO;

@RequiredArgsConstructor
public final class MachineRecipeBuilderImpl implements MachineRecipeBuilder {

	private final @Getter Map<RecipeElementType<?>, List<RecipeElement>> inputs = new HashMap<>();
	private final @Getter Map<RecipeElementType<?>, List<RecipeElement>> outputs = new HashMap<>();
	private final MachineRecipeType recipeType;
	private final HolderLookup.Provider registries;
	private @Getter double currentChance = 1;
	private @Getter int duration = 200;
	private @Getter int program = -1;

	@Override
	public MachineRecipeBuilder chance(final double chance) {
		if (chance > 1) {
			//Assume full percentage when larger than 1.
			this.currentChance = chance / 100.0;
		} else {
			this.currentChance = chance;
		}
		return this;
	}

	@Override
	public <T> MachineRecipeBuilder add(final IO io, final RecipeElementType<T> elementType, final T obj) {
		switch (io) {
			case IN -> this.inputs.computeIfAbsent(elementType, k -> new ArrayList<>()).add(new RecipeElement(obj, this.currentChance));
			case OUT -> this.outputs.computeIfAbsent(elementType, k -> new ArrayList<>()).add(new RecipeElement(obj, this.currentChance));
		}
		return this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public MachineRecipeBuilder in(final TagKey<?> tag, final int count) {
		if (tag.registry() == Registries.ITEM) {
			return this.in(Ingredient.of(this.registries.lookupOrThrow(Registries.ITEM).getOrThrow((TagKey<Item>) tag)), Math.abs(count));
		} else if (tag.registry() == Registries.FLUID) {
			return this.in(FluidIngredient.of(this.registries.lookupOrThrow(Registries.FLUID).getOrThrow((TagKey<Fluid>) tag)), Math.abs(count) * (count < 0 ? FluidType.BUCKET_VOLUME : 1));
		}
		return this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public MachineRecipeBuilder nc(final TagKey<?> tag, final int count) {
		if (tag.registry() == Registries.ITEM) {
			return this.nc(Ingredient.of(this.registries.lookupOrThrow(Registries.ITEM).getOrThrow((TagKey<Item>) tag)), Math.abs(count));
		} else if (tag.registry() == Registries.FLUID) {
			return this.nc(FluidIngredient.of(this.registries.lookupOrThrow(Registries.FLUID).getOrThrow((TagKey<Fluid>) tag)), Math.abs(count) * (count < 0 ? FluidType.BUCKET_VOLUME : 1));
		}
		return this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public MachineRecipeBuilder out(final TagKey<?> tag, final int count) {
		if (tag.registry() == Registries.ITEM) {
			return this.out(Ingredient.of(this.registries.lookupOrThrow(Registries.ITEM).getOrThrow((TagKey<Item>) tag)), Math.abs(count));
		} else if (tag.registry() == Registries.FLUID) {
			return this.out(FluidIngredient.of(this.registries.lookupOrThrow(Registries.FLUID).getOrThrow((TagKey<Fluid>) tag)), Math.abs(count) * (count < 0 ? FluidType.BUCKET_VOLUME : 1));
		}
		return this;
	}

	@Override
	public MachineRecipeBuilder duration(final int recipeDuration) {
		this.duration = recipeDuration;
		return this;
	}

	@Override
	public MachineRecipeBuilder program(final int program) {
		if (program < -1 || program > 24) {
			throw new IllegalArgumentException("Recipe program must adhere to -1 <= program <= 24");
		}
		this.program = program;
		return this;
	}

	public void save(final ResourceLocation recipeId, final RecipeOutput output) {
		final RecipeBuilderCallback callback = RecipeCore.getRecipeBuilderCallback(this.recipeType);
		if (callback != null) {
			callback.accept(recipeId, this, (newRecipeId, builder) -> {
				final MachineRecipeBuilderImpl copy = Util.make(MachineRecipeBuilderImpl.this.copy(), builder);
				copy.save(newRecipeId, output);
			});
		}
		final MachineRecipeImpl recipe = new MachineRecipeImpl(this.recipeType, this.inputs, this.outputs, this.duration, this.program);
		output.accept(ResourceKey.create(Registries.RECIPE, recipeId), recipe, null);
	}

	@Override
	public MachineRecipeBuilderImpl copy() {
		return Util.make(new MachineRecipeBuilderImpl(this.recipeType, this.registries), builder -> {
			this.inputs.forEach((elementType, elements) -> builder.inputs.put(elementType, this.copyContentList(elementType, elements)));
			this.outputs.forEach((elementType, elements) -> builder.outputs.put(elementType, this.copyContentList(elementType, elements)));
			builder.duration = this.duration;
			builder.program = this.program;
			builder.currentChance = this.currentChance;
		});
	}

	private List<RecipeElement> copyContentList(final RecipeElementType<?> type, final List<RecipeElement> list) {
		final ArrayList<RecipeElement> result = new ArrayList<>(list.size());
		for (final RecipeElement obj : list) {
			result.add(obj.copy(type, null));
		}
		return result;
	}
}
