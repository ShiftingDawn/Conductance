package conductance.core.datapack.recipe;

import java.util.Optional;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.crafting.Recipe;
import com.mojang.serialization.JsonOps;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.Nullable;
import conductance.core.apiimpl.PluginManager;
import conductance.core.datapack.RuntimeDataPack;
import conductance.core.recipe.RecipeBuilderImpl;

public final class DynamicRecipeHandler {

	public static void addRecipes(final HolderLookup.Provider provider) {
		final RecipeOutput output = new RecipeOutput() {

			@SuppressWarnings("removal")
			@Override
			public Advancement.Builder advancement() {
				return Advancement.Builder.recipeAdvancement().parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
			}

			@Override
			public void accept(final ResourceLocation recipeId, final Recipe<?> recipe, @Nullable final AdvancementHolder advancementHolder, final ICondition... iConditions) {
				RuntimeDataPack.addRecipe(recipeId, () -> Recipe.CODEC.encodeStart(provider.createSerializationContext(JsonOps.INSTANCE), recipe).getOrThrow(), advancementHolder == null ? Optional.empty()
						: Optional.of(() -> new Tuple<>(Advancement.CODEC.encodeStart(provider.createSerializationContext(JsonOps.INSTANCE), advancementHolder.value()).getOrThrow(), advancementHolder.id())));
			}
		};
		PluginManager.dispatchRegisterRecipes(output, RecipeBuilderImpl::new);
	}

	public static void removeRecipes() {
	}

	private DynamicRecipeHandler() {
	}
}
