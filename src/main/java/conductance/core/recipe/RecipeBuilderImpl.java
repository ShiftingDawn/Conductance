package conductance.core.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import lombok.Getter;
import conductance.api.NCRecipeElementTypes;
import conductance.api.machine.recipe.IRecipe;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.machine.recipe.RecipeBuilder;
import conductance.api.machine.recipe.RecipeElement;

public class RecipeBuilderImpl implements RecipeBuilder {

	@Getter
	private final Map<IRecipeElementType<?>, List<RecipeElement>> inputs = new HashMap<>();
	@Getter
	private final Map<IRecipeElementType<?>, List<RecipeElement>> outputs = new HashMap<>();
	@Getter
	private final Map<IRecipeElementType<?>, List<RecipeElement>> inputsPerTick = new HashMap<>();
	@Getter
	private final Map<IRecipeElementType<?>, List<RecipeElement>> outputsPerTick = new HashMap<>();
	private final NCRecipeType recipeType;
	@Getter
	private final ResourceLocation recipeId;
	private boolean perTick = false;
	private int chance = 100;
	private int maxChance = 100;
	private int tieredChanceBoost = 0;
	@Getter
	private int processTime = 200;

	public RecipeBuilderImpl(final NCRecipeType recipeType, final ResourceLocation recipeId) {
		this.recipeType = recipeType;
		this.recipeId = recipeId.withPrefix(recipeType.getRegistryKey().getPath() + "/");
	}

	@Override
	public RecipeBuilder perTick(final boolean newPerTick) {
		this.perTick = newPerTick;
		return this;
	}

	@Override
	public RecipeBuilder tieredChanceBoost(final int chanceBoost) {
		this.tieredChanceBoost = chanceBoost;
		return this;
	}

	@Override
	public RecipeBuilder chance(final int newChance, final int newMaxChance) {
		this.chance = newChance;
		this.maxChance = newMaxChance;
		return this;
	}

	@Override
	public RecipeBuilder processTime(final int time) {
		this.processTime = time;
		return this;
	}

	@Override
	public <T> RecipeBuilder add(final boolean input, final IRecipeElementType<T> type, final T obj) {
		final Map<IRecipeElementType<?>, List<RecipeElement>> map;
		if (this.perTick) {
			map = input ? this.inputsPerTick : this.outputsPerTick;
		} else {
			map = input ? this.inputs : this.outputs;
		}
		map.computeIfAbsent(type, k -> new ArrayList<>()).add(new RecipeElement(obj, this.chance, this.maxChance, this.tieredChanceBoost));
		return this;
	}

	@Override
	public RecipeBuilder outEnergy(final long energy) {
		final boolean wasPerTick = this.perTick;
		this.perTick = true;
		this.out(NCRecipeElementTypes.ENERGY, energy);
		this.perTick = wasPerTick;
		return this;
	}

	@Override
	public IRecipe build() {
		return new RecipeImpl(
				this.recipeType,
				this.recipeId,
				this.inputs,
				this.outputs,
				this.inputsPerTick,
				this.outputsPerTick,
				this.processTime
		);
	}

	@Override
	public void save(final RecipeOutput output) {
		output.accept(this.recipeId, this.build(), null);
	}

	@Override
	public RecipeBuilder copy(final NCRecipeType type, final ResourceLocation newId) {
		return Util.make(new RecipeBuilderImpl(type, newId), copy -> {
			this.inputs.forEach((k, v) -> copy.inputs.put(k, new ArrayList<>(v)));
			this.outputs.forEach((k, v) -> copy.outputs.put(k, new ArrayList<>(v)));
			this.inputsPerTick.forEach((k, v) -> copy.inputsPerTick.put(k, new ArrayList<>(v)));
			this.outputsPerTick.forEach((k, v) -> copy.outputsPerTick.put(k, new ArrayList<>(v)));
			copy.perTick = this.perTick;
			copy.chance = this.chance;
			copy.maxChance = this.maxChance;
			copy.tieredChanceBoost = this.tieredChanceBoost;
			copy.processTime = this.processTime;
		});
	}

	@Override
	public RecipeBuilder copy(final NCRecipeType type, final String newId) {
		return this.copy(type, ResourceLocation.fromNamespaceAndPath(this.recipeId.getNamespace(), newId));
	}

	@Override
	public RecipeBuilder copy(final ResourceLocation newId) {
		return this.copy(this.recipeType, newId);
	}

	@Override
	public RecipeBuilder copy(final String newId) {
		return this.copy(this.recipeType, newId);
	}

	@Override
	public long getEnergyPerTick() {
		final List<RecipeElement> contents = this.inputsPerTick.getOrDefault(NCRecipeElementTypes.ENERGY, List.of());
		return contents.isEmpty() ? 0 : (long) contents.getFirst().data();
	}
}
