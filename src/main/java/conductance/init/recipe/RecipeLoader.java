package conductance.init.recipe;

import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.Util;
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
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.plugin.RegisterRecipeEvent;
import conductance.api.plugin.RemoveRecipeEvent;
import conductance.Conductance;

@ConductancePluginListener(modid = Conductance.MODID)
public final class RecipeLoader {

	public static final char WRENCH = 'W';
	public static final char HAMMER = 'H';
	public static final char WIRE_CUTTERS = 'X';
	private static final Char2ObjectMap<TagKey<Item>> TOOL_LOOKUP = new Char2ObjectArrayMap<>(
			Map.of(RecipeLoader.WRENCH, CAPI.TAG_WRENCHES, RecipeLoader.HAMMER, CAPI.TAG_HAMMERS, RecipeLoader.WIRE_CUTTERS, CAPI.TAG_WIRE_CUTTERS));

	@EventListener
	private static void initAddition(final RegisterRecipeEvent event) {
		MaterialRecipes.add(event);
		FuelAndEnergyRecipes.add(event);
		TierRecipes.add(event);
	}

	@EventListener
	private static void initRemoval(final RemoveRecipeEvent event) {
		MaterialRecipes.remove(event::remove);
	}

	@SuppressWarnings("unchecked")
	public static void shaped(final RegisterRecipeEvent event, final String name, final ItemStack result, final Object... recipe) {
		final VanillaRecipeBuilders.CraftingShaped builder = new VanillaRecipeBuilders.CraftingShaped(event.id("crafting", name), result);
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
		builder.save(event.getOutput());
	}

	@SuppressWarnings("unchecked")
	public static void shapeless(final RegisterRecipeEvent event, final String name, final ItemStack result, final Object... recipe) {
		final VanillaRecipeBuilders.CraftingShapeless builder = new VanillaRecipeBuilders.CraftingShapeless(event.id("crafting", name), result);
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
		builder.save(event.getOutput());
	}

	public static void smelting(final RegisterRecipeEvent event, final String name, final ItemStack result, final Ingredient input, @Nullable final Consumer<VanillaRecipeBuilders.Smelting> consumer) {
		Util.make(new VanillaRecipeBuilders.Smelting(event.id("smelting", name), result, input), builder -> {
			if (consumer != null) {
				consumer.accept(builder);
			}
		}).save(event.getOutput());
	}

	public static void blasting(final RegisterRecipeEvent event, final String name, final ItemStack result, final Ingredient input, @Nullable final Consumer<VanillaRecipeBuilders.Blasting> consumer) {
		Util.make(new VanillaRecipeBuilders.Blasting(event.id("blasting", name), result, input), builder -> {
			if (consumer != null) {
				consumer.accept(builder);
			}
		}).save(event.getOutput());
	}

	public static void matRecipe(
			final RegisterRecipeEvent event, final String name, final Material material, final NCRecipeType recipeType,
			final TaggedMaterialSet input, final TaggedMaterialSet output, @Nullable final Consumer<RecipeBuilder> consumer
	) {
		event.create(recipeType, name.formatted(material.getName(), material.getName()), builder -> {
			final AutoRecipeData pair = CAPI.recipeHelper().calculateRecipeData(material, input, output, (int) material.getMass(), NCTiers.LV.getRecipeVoltage());
			builder
					.in(input, material, pair.inputCount())
					.out(output, material, pair.outputCount())
					.processTime(pair.processTime())
					.inEnergy(NCTiers.LV.getRecipeVoltage());
			if (consumer != null) {
				consumer.accept(builder);
			}
		});
	}

	private RecipeLoader() {
	}
}
