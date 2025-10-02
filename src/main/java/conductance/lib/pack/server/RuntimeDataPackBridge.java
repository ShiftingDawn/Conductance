package conductance.lib.pack.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagLoader;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import conductance.api.recipe.event.RegisterRecipeEvent;
import conductance.api.recipe.event.RemoveRecipeEvent;
import conductance.api.resource.event.RegisterTagEvent;
import conductance.Conductance;
import conductance.core.recipe.MachineRecipeBuilderImpl;

public final class RuntimeDataPackBridge {

	private static final RegisterTagEventImpl.TagRegister TAG_REGISTER;

	public static void reload(final HolderLookup.Provider provider) {
		final long sysTime = System.currentTimeMillis();

		LootTableGenerationHandler.reload(provider);

		Conductance.LOGGER.info("Conductance reloaded RuntimeDataPack in {}ms", System.currentTimeMillis() - sysTime);
	}

	public static void removeRecipes(final Map<ResourceLocation, ?> recipeMap) {
		final long sysTime = System.currentTimeMillis();
		Conductance.dispatchAll(RemoveRecipeEvent.class, new RemoveRecipeEventImpl(id -> {
			if (recipeMap.remove(id) == null) {
				Conductance.LOGGER.warn("Trying to remove non-existing recipe: {}", id);
			}
		}));
		Conductance.LOGGER.info("Conductance reloaded RuntimeDataPack recipe removal in {}ms", System.currentTimeMillis() - sysTime);
	}

	public static void insertRecipes(final HolderLookup.Provider registries, final Map<ResourceLocation, Recipe<?>> recipeMap) {
		final long sysTime = System.currentTimeMillis();
		final RecipeOutput recipeOutput = new RuntimeRecipeOutput(registries, recipeMap);
		Conductance.dispatch(RegisterRecipeEvent.class, modid -> new RegisterRecipeEventImpl(modid, registries, recipeOutput, (recipeId, recipeType, builder, self) -> {
			Util.make(new MachineRecipeBuilderImpl(recipeType, registries), builder).save(recipeId, recipeOutput);
		}));
		Conductance.LOGGER.info("Conductance reloaded RuntimeDataPack recipe generation in {}ms", System.currentTimeMillis() - sysTime);
	}

	public static void generateTags(final ResourceKey<? extends Registry<?>> registry, final Map<ResourceLocation, List<TagLoader.EntryWithSource>> tagMap) {
		TagGenerationHandler.CUSTOM_ITEM_TAGS.clear();
		Conductance.dispatchAll(RegisterTagEvent.class, new RegisterTagEventImpl(RuntimeDataPackBridge.TAG_REGISTER));
		TagGenerationHandler.addEntriesToTagMap(registry, tagMap);
	}

	static {
		TAG_REGISTER = new RegisterTagEventImpl.TagRegister() {

			@Override
			public void item(final TagKey<Item> tag, final ItemLike value, final ItemLike... moreValues) {
				final List<ItemLike> list = TagGenerationHandler.CUSTOM_ITEM_TAGS.computeIfAbsent(tag, k -> Collections.synchronizedList(new ArrayList<>()));
				list.add(value);
				list.addAll(Arrays.asList(moreValues));
			}

			@Override
			public void tag(final TagKey<Item> tag, final ResourceLocation value, final ResourceLocation... moreValues) {
				final List<ResourceLocation> list = TagGenerationHandler.CUSTOM_REQUIRED_TAGS.computeIfAbsent(tag, k -> Collections.synchronizedList(new ArrayList<>()));
				list.add(value);
				list.addAll(Arrays.asList(moreValues));
			}

			@Override
			public void optionalTag(final TagKey<Item> tag, final ResourceLocation value, final ResourceLocation... moreValues) {
				final List<ResourceLocation> list = TagGenerationHandler.CUSTOM_OPTIONAL_TAGS.computeIfAbsent(tag, k -> Collections.synchronizedList(new ArrayList<>()));
				list.add(value);
				list.addAll(Arrays.asList(moreValues));
			}
		};
	}

	private RuntimeDataPackBridge() {
	}
}
