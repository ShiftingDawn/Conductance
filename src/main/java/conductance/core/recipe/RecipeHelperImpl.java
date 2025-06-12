package conductance.core.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import com.google.common.collect.Table;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.capability.MachineRecipeCapability;
import conductance.api.recipe.AutoRecipeData;
import conductance.api.recipe.IRecipe;
import conductance.api.recipe.IRecipeElementType;
import conductance.api.recipe.NCRecipeType;
import conductance.api.recipe.RecipeCapabilityHolder;
import conductance.api.recipe.RecipeElement;
import conductance.api.recipe.RecipeHelper;
import conductance.api.recipe.RecipeModifier;
import conductance.api.registry.TaggedSet;
import conductance.api.util.IOMode;

final class RecipeHelperImpl implements RecipeHelper {

	public static final RecipeHelperImpl INSTANCE = new RecipeHelperImpl();

	private RecipeHelperImpl() {
	}

	@Override
	public boolean test(final IRecipe recipe, final RecipeCapabilityHolder holder) {
		if (recipe.getInputs().isEmpty() && recipe.getOutputs().isEmpty()) {
			return true;
		}
		if (!holder.hasRecipeCapabilities()) {
			return false;
		}
		final boolean ins = this.testInternal(recipe, IOMode.INPUT, holder, recipe.getInputs());
		final boolean outs = this.testInternal(recipe, IOMode.OUTPUT, holder, recipe.getOutputs());
		return ins && outs;
	}

	@Override
	public boolean testPerTick(final IRecipe recipe, final RecipeCapabilityHolder holder) {
		if (recipe.getInputsPerTick().isEmpty() && recipe.getOutputsPerTick().isEmpty()) {
			return true;
		}
		if (!holder.hasRecipeCapabilities()) {
			return false;
		}
		final boolean ins = this.testInternal(recipe, IOMode.INPUT, holder, recipe.getInputsPerTick());
		final boolean outs = this.testInternal(recipe, IOMode.OUTPUT, holder, recipe.getOutputsPerTick());
		return ins && outs;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private boolean testInternal(final IRecipe recipe, final IOMode ioMode, final RecipeCapabilityHolder holder, final Map<IRecipeElementType<?>, List<RecipeElement>> map) {
		final Table<IOMode, IRecipeElementType<?>, List<MachineRecipeCapability<?>>> capabilityTable = holder.getRecipeCapabilities();
		for (final Map.Entry<IRecipeElementType<?>, List<RecipeElement>> entry : map.entrySet()) {
			final Set<MachineRecipeCapability<?>> visitedCapabilities = new HashSet<>();
			final IRecipeElementType elementType = entry.getKey();
			final List elementValueList = entry.getValue().stream().map(element -> elementType.getCloner().copy(element.data())).toList();
			if (elementValueList.isEmpty()) {
				continue;
			}
			List result = this.handleElementsInternal(recipe, ioMode, ioMode, capabilityTable, elementType, visitedCapabilities, elementValueList, true);
			if (result == null) {
				continue;
			}
			result = this.handleElementsInternal(recipe, IOMode.INPUT_OUTPUT, ioMode, capabilityTable, elementType, visitedCapabilities, elementValueList, true);
			if (result != null) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean handle(final IRecipe recipe, final IOMode ioMode, final RecipeCapabilityHolder holder) {
		if (!holder.hasRecipeCapabilities() || ioMode == IOMode.INPUT_OUTPUT) {
			return false;
		}
		return this.handleInternal(recipe, ioMode, holder, ioMode.isInput() ? recipe.getInputs() : recipe.getOutputs());
	}

	@Override
	public boolean handlePerTick(final IRecipe recipe, final IOMode ioMode, final RecipeCapabilityHolder holder) {
		if (!holder.hasRecipeCapabilities() || ioMode == IOMode.INPUT_OUTPUT) {
			return false;
		}
		return this.handleInternal(recipe, ioMode, holder, ioMode.isInput() ? recipe.getInputsPerTick() : recipe.getOutputsPerTick());
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private boolean handleInternal(final IRecipe recipe, final IOMode ioMode, final RecipeCapabilityHolder holder, final Map<IRecipeElementType<?>, List<RecipeElement>> map) {
		final Table<IOMode, IRecipeElementType<?>, List<MachineRecipeCapability<?>>> capabilityTable = holder.getRecipeCapabilities();
		for (final Map.Entry<IRecipeElementType<?>, List<RecipeElement>> entry : map.entrySet()) {
			final Set<MachineRecipeCapability<?>> visitedCapabilities = new HashSet<>();
			final IRecipeElementType elementType = entry.getKey();
			final List elementValueList = entry.getValue().stream()
					.filter(element -> element.testChance(holder.getChanceTier()))
					.map(element -> elementType.getCloner().copy(element.data()))
					.toList();
			if (elementValueList.isEmpty()) {
				continue;
			}
			List result = this.handleElementsInternal(recipe, ioMode, ioMode, capabilityTable, elementType, visitedCapabilities, elementValueList, false);
			if (result == null) {
				continue;
			}
			result = this.handleElementsInternal(recipe, IOMode.INPUT_OUTPUT, ioMode, capabilityTable, elementType, visitedCapabilities, elementValueList, true);
			if (result != null) {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Nullable
	private List handleElementsInternal(
			final IRecipe recipe,
			final IOMode capabilityIoMode, final IOMode ioMode,
			final Table<IOMode, IRecipeElementType<?>, List<MachineRecipeCapability<?>>> capabilityTable,
			final IRecipeElementType<?> elementType,
			final Set<MachineRecipeCapability<?>> visitedCapabilities,
			final List elementValueList,
			final boolean simulate
	) {
		if (!capabilityTable.contains(capabilityIoMode, elementType)) {
			return elementValueList;
		}
		final List<MachineRecipeCapability<?>> capabilities = capabilityTable.get(capabilityIoMode, elementType);
		assert capabilities != null;
		List leftOver = elementValueList;
		for (final MachineRecipeCapability recipeCapability : capabilities) {
			if (visitedCapabilities.contains(recipeCapability)) {
				continue;
			}
			visitedCapabilities.add(recipeCapability);
			leftOver = recipeCapability.handle(ioMode, recipe, leftOver, simulate);
			if (leftOver == null) {
				break;
			}
		}
		return leftOver;
	}

	@Override
	public List<IRecipe> findRecipes(final NCRecipeType recipeType, final RecipeManager recipeManager, final RecipeCapabilityHolder holder, @Nullable final Map<IRecipeElementType<?>, Integer> recipeOutputLimits) {
		if (!holder.hasRecipeCapabilities()) {
			return List.of();
		}
		return recipeManager.getAllRecipesFor(recipeType).parallelStream()
				.map(RecipeHolder::value)
				.map(recipe -> recipeOutputLimits == null ? recipe : RecipeHelperImpl.trimRecipeOutputs(recipe, recipeOutputLimits))
				.filter(recipe -> this.test(recipe, holder) && this.testPerTick(recipe, holder))
				.collect(Collectors.toList());
	}

	private static IRecipe trimRecipeOutputs(final IRecipe recipe, final Map<IRecipeElementType<?>, Integer> recipeOutputLimits) {
		if (recipeOutputLimits.isEmpty() || recipeOutputLimits.values().stream().allMatch(i -> i == -1)) {
			return recipe;
		}
		final IRecipe copy = recipe.copyMutable();
		copy.getOutputs().clear();
		copy.getOutputsPerTick().clear();
		copy.getOutputs().putAll(RecipeHelperImpl.trimElementList(recipe.getOutputs(), recipeOutputLimits));
		copy.getOutputsPerTick().putAll(RecipeHelperImpl.trimElementList(recipe.getOutputsPerTick(), recipeOutputLimits));
		return copy.copy((RecipeModifier) null, false);
	}

	private static Map<IRecipeElementType<?>, List<RecipeElement>> trimElementList(final Map<IRecipeElementType<?>, List<RecipeElement>> current, final Map<IRecipeElementType<?>, Integer> recipeOutputLimits) {
		final Map<IRecipeElementType<?>, List<RecipeElement>> outputs = new HashMap<>();
		final Set<IRecipeElementType<?>> trimmed = new HashSet<>();
		for (final Map.Entry<IRecipeElementType<?>, Integer> entry : recipeOutputLimits.entrySet()) {
			final IRecipeElementType<?> elementType = entry.getKey();
			if (!current.containsKey(elementType)) {
				continue;
			}
			final List<RecipeElement> nonChanced = new ArrayList<>();
			List<RecipeElement> chanced = new ArrayList<>();
			for (final RecipeElement element : current.getOrDefault(elementType, List.of())) {
				if (element.chance() <= 0 || element.chance() >= 100) {
					nonChanced.add(element);
				} else {
					chanced.add(element);
				}
			}
			final int outputLimit = entry.getValue();
			if (outputLimit == -1) {
				outputs.computeIfAbsent(elementType, $ -> new ArrayList<>())
						.addAll(nonChanced);
			} else if (nonChanced.size() >= outputLimit) {
				outputs.computeIfAbsent(elementType, $ -> new ArrayList<>())
						.addAll(nonChanced.stream().map(elem -> elem.copy(elementType, null)).toList().subList(0, outputLimit));
				chanced.clear();
			} else if (!nonChanced.isEmpty() && (nonChanced.size() + chanced.size()) >= outputLimit) {
				outputs.computeIfAbsent(elementType, $ -> new ArrayList<>())
						.addAll(nonChanced.stream().map(elem -> elem.copy(elementType, null)).toList());
				final int numChanced = outputLimit - nonChanced.size();
				chanced = chanced.subList(0, Math.min(numChanced, chanced.size()));
			} else if (nonChanced.isEmpty()) {
				chanced = chanced.subList(0, Math.min(outputLimit, chanced.size()));
			} else {
				outputs.computeIfAbsent(elementType, $ -> new ArrayList<>())
						.addAll(nonChanced.stream().map(elem -> elem.copy(elementType, null)).toList());
			}
			if (!chanced.isEmpty()) {
				outputs.computeIfAbsent(elementType, $ -> new ArrayList<>())
						.addAll(chanced.stream().map(elem -> elem.copy(elementType, null)).toList());
			}
			trimmed.add(elementType);
		}
		for (final Map.Entry<IRecipeElementType<?>, List<RecipeElement>> entry : current.entrySet()) {
			if (trimmed.contains(entry.getKey())) {
				continue;
			}
			outputs.computeIfAbsent(entry.getKey(), $ -> new ArrayList<>()).addAll(entry.getValue());
		}
		return outputs;
	}

	@Override
	public <T> AutoRecipeData calculateRecipeData(final T obj, final TaggedSet<T> inputType, final TaggedSet<T> outputType, final int baseTime, final long baseEnergy) {
		final long inputValue = inputType.getUnitValue(obj);
		final long outputValue = outputType.getUnitValue(obj);
		if (inputValue == outputValue) {
			return new AutoRecipeData(1, 1, baseTime, baseEnergy);
		} else if (inputValue < outputValue) {
			final int diffAmount = (int) (outputValue / inputValue);
			return new AutoRecipeData(diffAmount, 1, baseTime * diffAmount, baseEnergy * diffAmount);
		} else {
			final int diffAmount = (int) (inputValue / outputValue);
			return new AutoRecipeData(1, diffAmount, baseTime * diffAmount, baseEnergy * diffAmount);
		}
	}
}
