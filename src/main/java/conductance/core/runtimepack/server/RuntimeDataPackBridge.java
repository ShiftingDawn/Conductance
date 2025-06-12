package conductance.core.runtimepack.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagLoader;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import com.mojang.serialization.JsonOps;
import net.neoforged.neoforge.common.conditions.ICondition;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.Nullable;
import conductance.api.recipe.event.RegisterRecipeEvent;
import conductance.api.recipe.event.RemoveRecipeEvent;
import conductance.api.resource.RegisterTagEvent;
import conductance.Conductance;
import conductance.core.material.MaterialRegistryImpl;
import conductance.core.recipe.RecipeCore;

public final class RuntimeDataPackBridge {

	public static void reload(final HolderLookup.Provider provider) {
		final long sysTime = System.currentTimeMillis();
		MaterialRegistryImpl.INSTANCE.reload();

		RuntimeDataPackBridge.loadRecipes(provider);
		LootTableGenerationHandler.generate(provider);

		Conductance.LOGGER.info("Conductance reloaded RuntimeDataPack in {}ms", System.currentTimeMillis() - sysTime);
	}

	private static void loadRecipes(final HolderLookup.Provider provider) {
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
		Conductance.dispatch(RegisterRecipeEvent.class, modid -> new RegisterRecipeEventImpl(modid, output, RecipeCore.getRecipeBuilderFactory()));
	}

	public static void removeRecipes(final Map<ResourceLocation, JsonElement> recipeMap) {
		Conductance.dispatchAll(RemoveRecipeEvent.class, new RemoveRecipeEventImpl(id -> {
			if (recipeMap.remove(id) == null) {
				Conductance.LOGGER.warn("Trying to remove non-existing recipe: {}", id);
			}
		}));
	}

	public static void generateTags(final Registry<?> registry, final Map<ResourceLocation, List<TagLoader.EntryWithSource>> tagMap) {
		TagGenerationHandler.CUSTOM_ITEM_TAGS.clear();
		Conductance.dispatchAll(RegisterTagEvent.class, new RegisterTagEventImpl((tag, value, moreValues) -> {
			final List<ItemLike> list = TagGenerationHandler.CUSTOM_ITEM_TAGS.computeIfAbsent(tag, k -> Collections.synchronizedList(new ArrayList<>()));
			list.add(value);
			list.addAll(Arrays.asList(moreValues));
		}));
		TagGenerationHandler.addEntriesToTagMap(registry, tagMap);
	}

	private RuntimeDataPackBridge() {
	}
}
