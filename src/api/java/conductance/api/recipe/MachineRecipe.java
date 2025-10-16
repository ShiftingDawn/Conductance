package conductance.api.recipe;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import com.mojang.serialization.Codec;
import lombok.Getter;

public class MachineRecipe implements Recipe<RecipeInput> {

	public static final Codec<Map<RecipeElementType<?>, List<RecipeElement>>> CONTENT_MAP_CODEC = Codec.dispatchedMap(RecipeElementType.CODEC, elementType -> elementType.getRecipeObjectCodec().listOf());
	private final MachineRecipeType recipeType;
	private final @Getter Map<RecipeElementType<?>, List<RecipeElement>> inputs;
	private final @Getter Map<RecipeElementType<?>, List<RecipeElement>> outputs;
	private final @Getter Map<RecipeElementType<?>, List<RecipeElement>> perTickInputs;
	private final @Getter Map<RecipeElementType<?>, List<RecipeElement>> perTickOutputs;
	private final @Getter int recipeDuration;
	private final @Getter int program;
	private final @Getter RecipeDataMap recipeDataMap;

	public MachineRecipe(
		final MachineRecipeType recipeType,
		final Map<RecipeElementType<?>, List<RecipeElement>> inputs, final Map<RecipeElementType<?>, List<RecipeElement>> outputs,
		final Map<RecipeElementType<?>, List<RecipeElement>> perTickInputs, final Map<RecipeElementType<?>, List<RecipeElement>> perTickOutputs,
		final int recipeDuration, final int program, final RecipeDataMap recipeDataMap
	) {
		this.recipeType = recipeType;
		this.inputs = MachineRecipe.toImmutableMap(inputs);
		this.outputs = MachineRecipe.toImmutableMap(outputs);
		this.perTickInputs = MachineRecipe.toImmutableMap(perTickInputs);
		this.perTickOutputs = MachineRecipe.toImmutableMap(perTickOutputs);
		this.recipeDuration = recipeDuration;
		this.program = program;
		this.recipeDataMap = recipeDataMap;
	}

	public final <T> T getData(final RecipeDataToken<T> token) {
		return this.recipeDataMap.get(token);
	}

	@Override
	public RecipeSerializer<MachineRecipe> getSerializer() {
		return this.recipeType.getRecipeSerializer();
	}

	@Override
	public MachineRecipeType getType() {
		return this.recipeType;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	@Deprecated
	public boolean matches(final RecipeInput recipeInput, final Level level) {
		return false;
	}

	@Override
	@Deprecated
	public ItemStack assemble(final RecipeInput recipeInput, final HolderLookup.Provider provider) {
		return ItemStack.EMPTY;
	}

	@Override
	public PlacementInfo placementInfo() {
		return PlacementInfo.NOT_PLACEABLE;
	}

	@Override
	public RecipeBookCategory recipeBookCategory() {
		return this.recipeType.getRecipeBookCategory();
	}

	private static Map<RecipeElementType<?>, List<RecipeElement>> toImmutableMap(final Map<RecipeElementType<?>, List<RecipeElement>> input) {
		final Map<RecipeElementType<?>, List<RecipeElement>> map = new IdentityHashMap<>();
		for (final Map.Entry<RecipeElementType<?>, List<RecipeElement>> entry : input.entrySet()) {
			map.put(entry.getKey(), Collections.unmodifiableList(entry.getValue()));
		}
		return Collections.unmodifiableMap(map);
	}
}
