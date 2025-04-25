package conductance.core.recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCRecipeElementTypes;
import conductance.api.machine.recipe.IRecipe;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.machine.recipe.RecipeElement;
import conductance.api.machine.recipe.RecipeModifier;
import conductance.api.util.overclock.OverclockResult;

public class RecipeImpl implements IRecipe {

	@Getter
	private final NCRecipeType type;
	@Getter
	private final ResourceLocation id;
	@Getter
	private final Map<IRecipeElementType<?>, List<RecipeElement>> inputs;
	@Getter
	private final Map<IRecipeElementType<?>, List<RecipeElement>> outputs;
	@Getter
	private final Map<IRecipeElementType<?>, List<RecipeElement>> inputsPerTick;
	@Getter
	private final Map<IRecipeElementType<?>, List<RecipeElement>> outputsPerTick;
	@Getter
	private final int processTime;
	@Getter
	private final int program;

	public RecipeImpl(
			final RecipeType<?> recipeType, final ResourceLocation id,
			final Map<IRecipeElementType<?>, List<RecipeElement>> inputs, final Map<IRecipeElementType<?>, List<RecipeElement>> outputs,
			final Map<IRecipeElementType<?>, List<RecipeElement>> inputsPerTick, final Map<IRecipeElementType<?>, List<RecipeElement>> outputsPerTick,
			final int processTime, final int program,
			final boolean mutable
	) {
		if (!(recipeType instanceof NCRecipeType)) {
			throw new RuntimeException("RecipeType %s is not an instance of %s".formatted(recipeType.getClass().getName(), NCRecipeType.class.getName()));
		}
		this.type = (NCRecipeType) recipeType;
		this.id = id;
		this.inputs = mutable ? RecipeImpl.makeMutable(inputs) : RecipeImpl.makeImmutable(inputs);
		this.outputs = mutable ? RecipeImpl.makeMutable(outputs) : RecipeImpl.makeImmutable(outputs);
		this.inputsPerTick = mutable ? RecipeImpl.makeMutable(inputsPerTick) : RecipeImpl.makeImmutable(inputsPerTick);
		this.outputsPerTick = mutable ? RecipeImpl.makeMutable(outputsPerTick) : RecipeImpl.makeImmutable(outputsPerTick);
		this.processTime = processTime;
		this.program = program;
	}

	public RecipeImpl(
			final RecipeType<?> recipeType, final ResourceLocation id,
			final Map<IRecipeElementType<?>, List<RecipeElement>> inputs, final Map<IRecipeElementType<?>, List<RecipeElement>> outputs,
			final Map<IRecipeElementType<?>, List<RecipeElement>> inputsPerTick, final Map<IRecipeElementType<?>, List<RecipeElement>> outputsPerTick,
			final int processTime, final int program
	) {
		this(recipeType, id, inputs, outputs, inputsPerTick, outputsPerTick, processTime, program, false);
	}

	private static Map<IRecipeElementType<?>, List<RecipeElement>> makeImmutable(final Map<IRecipeElementType<?>, List<RecipeElement>> map) {
		return Collections.unmodifiableMap(Util.make(new HashMap<>(map.size()), newMap -> map.forEach((key, value) -> newMap.put(key, Collections.unmodifiableList(value)))));
	}

	private static Map<IRecipeElementType<?>, List<RecipeElement>> makeMutable(final Map<IRecipeElementType<?>, List<RecipeElement>> map) {
		return Util.make(new HashMap<>(map.size()), newMap -> map.forEach((key, value) -> newMap.put(key, value.stream().map(c -> c.copy(key, null)).toList())));
	}

	@Override
	public IRecipe copy(@Nullable final RecipeModifier modifier, final boolean modifyProcessTime) {
		return new RecipeImpl(
				this.type, this.id,
				this.copyContentMap(this.getInputs(), modifier, null), this.copyContentMap(this.getOutputs(), modifier, null),
				this.copyContentMap(this.getInputsPerTick(), modifier, null), this.copyContentMap(this.getOutputsPerTick(), modifier, null),
				modifyProcessTime && modifier != null ? modifier.apply(this.processTime).intValue() : this.processTime,
				this.program
		);
	}

	@Override
	public IRecipe copy(final OverclockResult overclockResult, final boolean modifyOutput) {
		return new RecipeImpl(
				this.type,
				this.id,
				this.copyContentMap(this.inputs, null, null),
				this.copyContentMap(this.outputs, null, null),
				this.copyContentMap(this.inputsPerTick, null, modifyOutput ? null
						: (elementType, content) -> elementType == NCRecipeElementTypes.ENERGY ? this.createOverclock(overclockResult, content) : content),
				this.copyContentMap(this.outputsPerTick, null, !modifyOutput ? null
						: (elementType, content) -> elementType == NCRecipeElementTypes.ENERGY ? this.createOverclock(overclockResult, content) : content),
				(int) overclockResult.newTime(),
				this.program
		);
	}

	private RecipeElement createOverclock(final OverclockResult overclockResult, final RecipeElement old) {
		return new RecipeElement(overclockResult.newEnergy(), old.chance(), old.maxChange(), old.tieredChanceBoost());
	}

	@Override
	public IRecipe copyMutable() {
		return new RecipeImpl(
				this.type, this.id,
				this.copyContentMap(this.getInputs(), null, null), this.copyContentMap(this.getOutputs(), null, null),
				this.copyContentMap(this.getInputsPerTick(), null, null), this.copyContentMap(this.getOutputsPerTick(), null, null),
				this.processTime,
				this.program,
				true
		);
	}

	@Override
	public long getEnergyPerTick() {
		if (this.inputsPerTick.containsKey(NCRecipeElementTypes.ENERGY)) {
			final List<RecipeElement> contents = this.inputsPerTick.get(NCRecipeElementTypes.ENERGY);
			if (!contents.isEmpty()) {
				return (long) contents.getFirst().data();
			}
		} else if (this.outputsPerTick.containsKey(NCRecipeElementTypes.ENERGY)) {
			final List<RecipeElement> contents = this.outputsPerTick.get(NCRecipeElementTypes.ENERGY);
			if (!contents.isEmpty()) {
				return (long) contents.getFirst().data();
			}
		}
		return 0;
	}

	private Map<IRecipeElementType<?>, List<RecipeElement>> copyContentMap(
			final Map<IRecipeElementType<?>, List<RecipeElement>> map, @Nullable final RecipeModifier modifier,
			@Nullable final BiFunction<IRecipeElementType<?>, RecipeElement, RecipeElement> transformer
	) {
		final HashMap<IRecipeElementType<?>, List<RecipeElement>> result = new HashMap<>();
		map.forEach((elementType, elements) -> {
			if (elements != null && !elements.isEmpty()) {
				final List<RecipeElement> listCopy = new ArrayList<>();
				elements.forEach(content -> listCopy.add(transformer != null ? transformer.apply(elementType, content.copy(elementType, modifier)) : content.copy(elementType, modifier)));
				result.put(elementType, Collections.unmodifiableList(listCopy));
			}
		});
		return Collections.unmodifiableMap(result);
	}
}
