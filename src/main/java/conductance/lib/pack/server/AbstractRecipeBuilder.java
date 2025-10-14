package conductance.lib.pack.server;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import conductance.api.recipe.event.RecipeBuilder;

@RequiredArgsConstructor
abstract class AbstractRecipeBuilder<BUILDER extends RecipeBuilder<BUILDER>> implements RecipeBuilder<BUILDER> {

	public static final String ADV_NAME = "dummy";
	public static final Criterion<ImpossibleTrigger.TriggerInstance> ADV = CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance());

	@Getter
	private final HolderLookup.Provider registries;

	protected final HolderGetter<Item> getHolderGetter() {
		return this.registries.lookupOrThrow(Registries.ITEM);
	}

	protected abstract void build(ResourceKey<Recipe<?>> recipeId, RecipeOutput output);
}
