package conductance.core.recipe;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.gui.ProgressProvider;
import conductance.api.recipe.MachineRecipe;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.recipe.RecipeDataToken;
import conductance.api.recipe.RecipeElementType;
import conductance.api.recipe.event.RecipeBuilderCallback;
import conductance.api.recipe.event.RecipeTestCallback;
import conductance.api.util.IO;
import conductance.api.util.Lazy;
import conductance.init.ConductanceRecipeBookCategories;

final class MachineRecipeTypeImpl implements MachineRecipeType {

	static final Map<MachineRecipeType, List<RecipeHolder<MachineRecipe>>> ALL_RECIPES = new ConcurrentHashMap<>();
	private final Object2IntMap<RecipeElementType<?>> inputLimits;
	private final Object2IntMap<RecipeElementType<?>> outputLimits;
	private final @Getter Map<String, RecipeDataToken<?>> additionalDataTokens;
	private final Map<RecipeTestCallback.When, List<RecipeTestCallback>> testCallbacks;
	private final Map<RecipeTestCallback.When, List<RecipeTestCallback>> perTickTestCallbacks;
	private final @Getter ResourceLocation guiArrow;
	private final @Getter ProgressProvider.Direction guiArrowDirection;
	private final @Getter boolean hidden;
	private final @Nullable RecipeBuilderCallback recipeBuilderCallback;
	private final Lazy<String> descriptionId = Lazy.of(() -> Util.makeDescriptionId("recipeType", this.getId()));
	private final Lazy<Component> name = Lazy.of(() -> Component.translatable(this.getDescriptionId()));

	MachineRecipeTypeImpl(
		final Object2IntMap<RecipeElementType<?>> inputLimits, final Object2IntMap<RecipeElementType<?>> outputLimits,
		final Map<String, RecipeDataToken<?>> additionalDataTokens,
		final Map<RecipeTestCallback.When, List<RecipeTestCallback>> testCallbacks, final Map<RecipeTestCallback.When, List<RecipeTestCallback>> perTickTestCallbacks,
		final ResourceLocation guiArrow, final ProgressProvider.Direction guiArrowDirection, final boolean hidden,
		@Nullable final RecipeBuilderCallback recipeBuilderCallback
	) {
		this.inputLimits = Object2IntMaps.unmodifiable(inputLimits);
		this.outputLimits = Object2IntMaps.unmodifiable(outputLimits);
		this.additionalDataTokens = Collections.unmodifiableMap(additionalDataTokens);
		this.testCallbacks = Collections.unmodifiableMap(CAPI.make(new HashMap<>(), map -> testCallbacks.forEach((when, callbacks) -> map.put(when, Collections.unmodifiableList(callbacks)))));
		this.perTickTestCallbacks = Collections.unmodifiableMap(CAPI.make(new HashMap<>(), map -> perTickTestCallbacks.forEach((when, callbacks) -> map.put(when, Collections.unmodifiableList(callbacks)))));
		this.guiArrow = guiArrow;
		this.guiArrowDirection = guiArrowDirection;
		this.hidden = hidden;
		this.recipeBuilderCallback = recipeBuilderCallback;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RecipeSerializer<MachineRecipe> getRecipeSerializer() {
		return (RecipeSerializer<MachineRecipe>) BuiltInRegistries.RECIPE_SERIALIZER.getValue(this.getId());
	}

	@Override
	public RecipeBookCategory getRecipeBookCategory() {
		return ConductanceRecipeBookCategories.ALL.get();
	}

	@Override
	public List<RecipeHolder<MachineRecipe>> getRecipes() {
		return MachineRecipeTypeImpl.ALL_RECIPES.getOrDefault(this, List.of());
	}

	@Override
	public int getLimit(final IO io, final RecipeElementType<?> elementType) {
		return switch (io) {
			case IN -> this.inputLimits.getInt(elementType);
			case OUT -> this.outputLimits.getInt(elementType);
		};
	}

	@Override
	public boolean testRecipe(final boolean perTick, final RecipeTestCallback.When when, final MachineBlockEntity<?> machine, final MachineRecipe recipe) {
		final List<RecipeTestCallback> callbacks = (perTick ? this.perTickTestCallbacks : this.testCallbacks).get(when);
		for (final RecipeTestCallback callback : callbacks) {
			if (!callback.test(machine, recipe)) {
				return false;
			}
		}
		return true;
	}

	public @Nullable RecipeBuilderCallback getRecipeBuilderCallback() {
		return this.recipeBuilderCallback;
	}

	@Override
	public String getDescriptionId() {
		return this.descriptionId.get();
	}

	@Override
	public Component getName() {
		return this.name.get();
	}
}
