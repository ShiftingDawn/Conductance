package conductance.runtimepack.server.recipe;

import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.NCTiers;
import conductance.api.machine.recipe.AutoRecipeData;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.machine.recipe.RecipeBuilder;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.plugin.RecipeBuilderFactory;
import conductance.Conductance;

public final class RecipeLoader {

	public static final char WRENCH = 'W';
	public static final char HAMMER = 'H';
	public static final char WIRE_CUTTERS = 'X';
	private static final Char2ObjectMap<TagKey<Item>> TOOL_LOOKUP = new Char2ObjectArrayMap<>(
			Map.of(RecipeLoader.WRENCH, CAPI.Tags.TAG_WRENCH, RecipeLoader.HAMMER, CAPI.Tags.TAG_HAMMER, RecipeLoader.WIRE_CUTTERS, CAPI.Tags.TAG_WIRE_CUTTERS));

	public static void init(final RecipeOutput output, final RecipeBuilderFactory builderFactory) {
		MaterialRecipes.add(output, builderFactory);
		FuelAndEnergyRecipes.add(output, builderFactory);
	}

	@SuppressWarnings("unchecked")
	public static void shaped(final RecipeOutput output, final String name, final ItemStack result, final Object... recipe) {
		final VanillaRecipeBuilders.CraftingShaped builder = new VanillaRecipeBuilders.CraftingShaped(Conductance.id("crafting/" + name), result);
		for (int i = 0; i < recipe.length; ++i) {
			if (recipe[i] instanceof final String str) {
				builder.row(str);
				for (final char c : str.toCharArray()) {
					final TagKey<Item> tool = RecipeLoader.TOOL_LOOKUP.get(c);
					if (tool != null) {
						builder.define(c, tool);
					}
				}
			} else if (recipe[i++] instanceof final Character character) {
				if (recipe.length <= i) {
					throw new IllegalStateException("Unexpected end of recipe inputs");
				}
				switch (recipe[i]) {
					case final Ingredient ingredient -> builder.define(character, ingredient);
					case final ItemStack ingredient -> builder.define(character, ingredient);
					case final TagKey<?> itemTag -> builder.define(character, (TagKey<Item>) itemTag);
					case final ItemLike itemLike -> builder.define(character, itemLike);
					case null, default -> throw new IllegalArgumentException("Invalid recipe input " + (recipe[i] == null ? "null" : recipe[i].getClass().getName()) + " at position " + i);
				}
			}
		}
		builder.save(output);
	}

	@SuppressWarnings("unchecked")
	public static void shapeless(final RecipeOutput output, final String name, final ItemStack result, final Object... recipe) {
		final VanillaRecipeBuilders.CraftingShapeless builder = new VanillaRecipeBuilders.CraftingShapeless(Conductance.id("crafting/" + name), result);
		for (int i = 0; i < recipe.length; ++i) {
			switch (recipe[i]) {
				case final Character character -> {
					final TagKey<Item> tool = RecipeLoader.TOOL_LOOKUP.get(character.charValue());
					if (tool == null) {
						throw new IllegalArgumentException("Invalid recipe input " + (recipe[i] == null ? "null" : recipe[i].getClass().getName()) + " at position " + i);
					}
					builder.add(tool);
				}
				case final Ingredient ingredient -> builder.add(ingredient);
				case final ItemStack itemStack -> {
					for (int j = 0; j < itemStack.getCount(); ++j) {
						builder.add(itemStack.copyWithCount(1));
					}
				}
				case final TagKey<?> itemTag -> {
					final int inputAmount = i + 1 < recipe.length && recipe[i + 1] instanceof Integer ? (int) recipe[++i] : 1;
					for (int j = 0; j < inputAmount; ++j) {
						builder.add((TagKey<Item>) itemTag);
					}
				}
				case final ItemLike itemLike -> {
					final int inputAmount = i + 1 < recipe.length && recipe[i + 1] instanceof Integer ? (int) recipe[++i] : 1;
					for (int j = 0; j < inputAmount; ++j) {
						builder.add(itemLike);
					}
				}
				case null, default -> throw new IllegalArgumentException("Invalid recipe input " + (recipe[i] == null ? "null" : recipe[i].getClass().getName()) + " at position " + i);
			}
		}
		builder.save(output);
	}

	public static void matRecipe(
			final RecipeOutput recipeOutput, final RecipeBuilderFactory factory, final String name, final Material material, final NCRecipeType recipeType,
			final TaggedMaterialSet input, final TaggedMaterialSet output, @Nullable final Consumer<RecipeBuilder> consumer
	) {
		final RecipeBuilder builder = factory.build(recipeType, Conductance.id(name.formatted(material.getName(), material.getName())));
		final AutoRecipeData pair = CAPI.recipeHelper().calculateRecipeData(material, input, output, (int) material.getMass(), NCTiers.LV.getRecipeVoltage());
		builder
				.in(input, material, pair.inputCount())
				.out(output, material, pair.outputCount())
				.processTime(pair.processTime())
				.inEnergy(pair.totalEnergy());
		if (consumer != null) {
			consumer.accept(builder);
		}
		builder.save(recipeOutput);
	}

	private RecipeLoader() {
	}
}
