package conductance.runtimepack.server.recipe;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import conductance.api.CAPI;
import conductance.api.NCMaterialFlags;
import conductance.api.NCMaterialTaggedSets;
import conductance.api.NCMaterialTraits;
import conductance.api.NCRecipeTypes;
import conductance.api.NCTiers;
import conductance.api.machine.recipe.AutoRecipeData;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.material.traits.MaterialTraitOre;
import conductance.api.plugin.RecipeBuilderFactory;
import conductance.api.util.MiscUtils;
import conductance.core.register.MaterialOverrideRegister;
import static conductance.Conductance.id;
import static conductance.runtimepack.server.recipe.RecipeLoader.blasting;
import static conductance.runtimepack.server.recipe.RecipeLoader.matRecipe;
import static conductance.runtimepack.server.recipe.RecipeLoader.shaped;
import static conductance.runtimepack.server.recipe.RecipeLoader.shapeless;
import static conductance.runtimepack.server.recipe.RecipeLoader.smelting;

final class MaterialRecipes {

	public static void add(final RecipeOutput output, final RecipeBuilderFactory builderFactory) {
		CAPI.regs().materials().forEach(material -> {
			MaterialRecipes.addAllRecipes(output, builderFactory, material);
			material.executeIf(NCMaterialTraits.ORE, trait -> MaterialRecipes.addOreRecipes(output, builderFactory, material, trait));
			material.executeIf(NCMaterialTraits.INGOT, () -> MaterialRecipes.addIngotRecipes(output, builderFactory, material));
			material.executeIf(NCMaterialTraits.GEM, () -> MaterialRecipes.addGemRecipes(output, builderFactory, material));
			material.executeIf(NCMaterialTraits.WOOD, () -> MaterialRecipes.addWoodRecipes(output, builderFactory, material));

			if (material.hasTrait(NCMaterialTraits.DUST) && !material.hasTrait(NCMaterialTraits.INGOT) && !material.hasTrait(NCMaterialTraits.GEM)) {
				MaterialRecipes.addDustRecipes(output, builderFactory, material);
			}
		});
	}

	public static void remove(final Consumer<ResourceLocation> remover) {
		MaterialRecipes.removeOreRecipes(remover);
	}

	private static void addOreRecipes(final RecipeOutput output, final RecipeBuilderFactory builderFactory, final Material material, final MaterialTraitOre trait) {
		if (!MaterialOverrideRegister.has(NCMaterialTaggedSets.RAW_ORE_BLOCK, material)) {
			shapeless(output, "raw_%s_ore_block".formatted(material.getName()), CAPI.materials().getBlock(NCMaterialTaggedSets.RAW_ORE_BLOCK, material, 1),
					CAPI.materials().getItem(NCMaterialTaggedSets.RAW_ORE, material, 9));
		}
		if (!MaterialOverrideRegister.has(NCMaterialTaggedSets.RAW_ORE, material)) {
			shapeless(output, "raw_%s_ore".formatted(material.getName()), CAPI.materials().getItem(NCMaterialTaggedSets.RAW_ORE, material, 9),
					CAPI.materials().getBlock(NCMaterialTaggedSets.RAW_ORE_BLOCK, material, 1));
		}
		final Material pulverizeMaterial = trait.getPulverizeResult() != null ? trait.getPulverizeResult().get() : material;
		final Material smeltMaterial = trait.getSmeltResult() != null ? trait.getSmeltResult().get() : material;
		final TaggedMaterialSet smeltType = material.hasTrait(NCMaterialTraits.INGOT) ? NCMaterialTaggedSets.INGOT : material.hasTrait(NCMaterialTraits.GEM) ? NCMaterialTaggedSets.GEM : NCMaterialTaggedSets.DUST;
		final BiConsumer<TaggedMaterialSet, Integer> pulverizeMaker = (set, multiplier) ->
				builderFactory.build(NCRecipeTypes.PULVERIZER, id("%s_from_%s".formatted(NCMaterialTaggedSets.DUST.getUnlocalizedName(pulverizeMaterial), set.getUnlocalizedName(material))))
						.in(CAPI.materials().getItem(set, material, 1)).out(NCMaterialTaggedSets.DUST, pulverizeMaterial, 2 * multiplier).inEnergy(4).processTime(100)
						.save(output);
		final BiConsumer<TaggedMaterialSet, Integer> smeltMaker = (set, multiplier) ->
				smelting(output, "%s_from_%s".formatted(smeltType.getUnlocalizedName(smeltMaterial), set.getUnlocalizedName(material)),
						CAPI.materials().getItem(smeltType, smeltMaterial, multiplier), Ingredient.of(CAPI.materials().getItem(set, material, 1)),
						builder -> builder.setExperience(0.3f * multiplier));
		final BiConsumer<TaggedMaterialSet, Integer> blastMaker = (set, multiplier) ->
				blasting(output, "%s_from_%s".formatted(smeltType.getUnlocalizedName(smeltMaterial), set.getUnlocalizedName(material)),
						CAPI.materials().getItem(smeltType, smeltMaterial, multiplier), Ingredient.of(CAPI.materials().getItem(set, material, 1)),
						builder -> builder.setExperience(0.3f * multiplier));
		CAPI.regs().materialTaggedSets().values().stream().filter(set -> set.getOreType() != null).forEach(set -> {
			final int multiplier = (set.getOreType().hasDoubleOutput() ? 2 : 1) * trait.getDropMultiplier();
			pulverizeMaker.accept(set, multiplier);
			smeltMaker.accept(set, multiplier);
			blastMaker.accept(set, multiplier);
		});
		//Double output for raw ore is handled by the ore block drop
		pulverizeMaker.accept(NCMaterialTaggedSets.RAW_ORE, trait.getDropMultiplier());
		smeltMaker.accept(NCMaterialTaggedSets.RAW_ORE, trait.getDropMultiplier());
		blastMaker.accept(NCMaterialTaggedSets.RAW_ORE, trait.getDropMultiplier());
		pulverizeMaker.accept(NCMaterialTaggedSets.RAW_ORE_BLOCK, 9 * trait.getDropMultiplier());
		smeltMaker.accept(NCMaterialTaggedSets.RAW_ORE_BLOCK, 9 * trait.getDropMultiplier());
		blastMaker.accept(NCMaterialTaggedSets.RAW_ORE_BLOCK, 9 * trait.getDropMultiplier());
	}

	private static void removeOreRecipes(final Consumer<ResourceLocation> remover) {
		Stream.of(
				//Ore blocks
				"coal_from_blasting_coal_ore", "coal_from_blasting_deepslate_coal_ore", "coal_from_smelting_coal_ore", "coal_from_smelting_deepslate_coal_ore",
				"copper_ingot_from_blasting_copper_ore", "copper_ingot_from_blasting_deepslate_copper_ore", "copper_ingot_from_smelting_copper_ore", "copper_ingot_from_smelting_deepslate_copper_ore",
				"diamond_from_blasting_deepslate_diamond_ore", "diamond_from_blasting_diamond_ore", "diamond_from_smelting_deepslate_diamond_ore", "diamond_from_smelting_diamond_ore",
				"emerald_from_blasting_deepslate_emerald_ore", "emerald_from_blasting_emerald_ore", "emerald_from_smelting_deepslate_emerald_ore", "emerald_from_smelting_emerald_ore",
				"gold_ingot_from_blasting_deepslate_gold_ore", "gold_ingot_from_blasting_gold_ore", "gold_ingot_from_blasting_nether_gold_ore", "gold_ingot_from_smelting_deepslate_gold_ore",
				"gold_ingot_from_smelting_gold_ore", "gold_ingot_from_smelting_nether_gold_ore",
				"iron_ingot_from_blasting_deepslate_iron_ore", "iron_ingot_from_blasting_iron_ore", "iron_ingot_from_smelting_deepslate_iron_ore", "iron_ingot_from_smelting_iron_ore",
				"lapis_lazuli_from_blasting_deepslate_lapis_ore", "lapis_lazuli_from_blasting_lapis_ore", "lapis_lazuli_from_smelting_deepslate_lapis_ore", "lapis_lazuli_from_smelting_lapis_ore",
				"redstone_from_blasting_deepslate_redstone_ore", "redstone_from_blasting_redstone_ore", "redstone_from_smelting_deepslate_redstone_ore", "redstone_from_smelting_redstone_ore",
				//Raw ores
				"copper_ingot_from_blasting_raw_copper",
				"copper_ingot_from_smelting_raw_copper",
				"gold_ingot_from_blasting_raw_gold",
				"gold_ingot_from_smelting_raw_gold",
				"iron_ingot_from_blasting_raw_iron",
				"iron_ingot_from_smelting_raw_iron"
		).map(ResourceLocation::withDefaultNamespace).forEach(remover);
	}

	private static void addAllRecipes(final RecipeOutput output, final RecipeBuilderFactory builderFactory, final Material material) {
		material.executeIf(NCMaterialFlags.GENERATE_BOLT_AND_SCREW, () -> {
			material.executeIf(NCMaterialFlags.GENERATE_ROD, () -> {
				matRecipe(output, builderFactory, "%s_bolt", material, NCRecipeTypes.CUTTING_MACHINE, NCMaterialTaggedSets.ROD, NCMaterialTaggedSets.BOLT, null);
			});
			matRecipe(output, builderFactory, "%s_screw", material, NCRecipeTypes.LATHE, NCMaterialTaggedSets.SCREW, NCMaterialTaggedSets.BOLT, null);
		});
		material.executeIf(NCMaterialFlags.GENERATE_GEAR, () -> {
			if (material.hasFlag(NCMaterialFlags.GENERATE_PLATE) && material.hasFlag(NCMaterialFlags.GENERATE_ROD)) {
				shaped(output, "%s_gear".formatted(material.getName()), CAPI.materials().getItem(NCMaterialTaggedSets.GEAR, material, 1),
						"#$#", "$W$", "#$#", '#', MiscUtils.getItemTag(NCMaterialTaggedSets.ROD, material), '$', MiscUtils.getItemTag(NCMaterialTaggedSets.PLATE, material));
			}
		});
		material.executeIf(NCMaterialFlags.GENERATE_SMALL_GEAR, () -> {
			if (material.hasFlag(NCMaterialFlags.GENERATE_PLATE) && material.hasFlag(NCMaterialFlags.GENERATE_ROD)) {
				shaped(output, "%s_gear".formatted(material.getName()), CAPI.materials().getItem(NCMaterialTaggedSets.GEAR_SMALL, material, 1),
						" # ", "W$X", " # ", '#', MiscUtils.getItemTag(NCMaterialTaggedSets.ROD, material), '$', MiscUtils.getItemTag(NCMaterialTaggedSets.PLATE, material));
			}
		});
		material.executeIf(NCMaterialFlags.GENERATE_FRAME_BOX, () -> {
			shaped(output, "%s_frame_box".formatted(material.getName()), CAPI.materials().getItem(NCMaterialTaggedSets.FRAME_BOX, material, 2),
					"###", "#W#", "###", '#', MiscUtils.getItemTag(NCMaterialTaggedSets.ROD, material));
		});

		material.executeIf(NCMaterialTraits.WIRE, trait -> {
			shapeless(output, "2x_%s_wire_from_1x_wire".formatted(material.getName()), CAPI.materials().getItem(NCMaterialTaggedSets.WIRE_2X, material, 1),
					MiscUtils.getItemTag(NCMaterialTaggedSets.WIRE_1X, material), 2);
			shapeless(output, "4x_%s_wire_from_1x_wire".formatted(material.getName()), CAPI.materials().getItem(NCMaterialTaggedSets.WIRE_4X, material, 1),
					MiscUtils.getItemTag(NCMaterialTaggedSets.WIRE_1X, material), 4);
			shapeless(output, "8x_%s_wire_from_1x_wire".formatted(material.getName()), CAPI.materials().getItem(NCMaterialTaggedSets.WIRE_8X, material, 1),
					MiscUtils.getItemTag(NCMaterialTaggedSets.WIRE_1X, material), 8);
			shapeless(output, "1x_%s_wire_from_2x_wire".formatted(material.getName()), CAPI.materials().getItem(NCMaterialTaggedSets.WIRE_1X, material, 2),
					MiscUtils.getItemTag(NCMaterialTaggedSets.WIRE_2X, material));
			shapeless(output, "4x_%s_wire_from_2x_wire".formatted(material.getName()), CAPI.materials().getItem(NCMaterialTaggedSets.WIRE_4X, material, 1),
					MiscUtils.getItemTag(NCMaterialTaggedSets.WIRE_2X, material), 2);
			shapeless(output, "8x_%s_wire_from_2x_wire".formatted(material.getName()), CAPI.materials().getItem(NCMaterialTaggedSets.WIRE_8X, material, 1),
					MiscUtils.getItemTag(NCMaterialTaggedSets.WIRE_2X, material), 4);
			shapeless(output, "12x_%s_wire_from_2x_wire".formatted(material.getName()), CAPI.materials().getItem(NCMaterialTaggedSets.WIRE_12X, material, 1),
					MiscUtils.getItemTag(NCMaterialTaggedSets.WIRE_2X, material), 6);
			shapeless(output, "16x_%s_wire_from_2x_wire".formatted(material.getName()), CAPI.materials().getItem(NCMaterialTaggedSets.WIRE_16X, material, 1),
					MiscUtils.getItemTag(NCMaterialTaggedSets.WIRE_2X, material), 8);
			shapeless(output, "1x_%s_wire_from_4x_wire".formatted(material.getName()), CAPI.materials().getItem(NCMaterialTaggedSets.WIRE_1X, material, 4),
					MiscUtils.getItemTag(NCMaterialTaggedSets.WIRE_4X, material));
			shapeless(output, "8x_%s_wire_from_4x_wire".formatted(material.getName()), CAPI.materials().getItem(NCMaterialTaggedSets.WIRE_8X, material, 1),
					MiscUtils.getItemTag(NCMaterialTaggedSets.WIRE_4X, material), 2);
			shapeless(output, "12x_%s_wire_from_4x_wire".formatted(material.getName()), CAPI.materials().getItem(NCMaterialTaggedSets.WIRE_12X, material, 1),
					MiscUtils.getItemTag(NCMaterialTaggedSets.WIRE_4X, material), 3);
			shapeless(output, "16x_%s_wire_from_4x_wire".formatted(material.getName()), CAPI.materials().getItem(NCMaterialTaggedSets.WIRE_16X, material, 1),
					MiscUtils.getItemTag(NCMaterialTaggedSets.WIRE_4X, material), 4);
			shapeless(output, "1x_%s_wire_from_8x_wire".formatted(material.getName()), CAPI.materials().getItem(NCMaterialTaggedSets.WIRE_1X, material, 8),
					MiscUtils.getItemTag(NCMaterialTaggedSets.WIRE_8X, material));
			shapeless(output, "16x_%s_wire_from_8x_wire".formatted(material.getName()), CAPI.materials().getItem(NCMaterialTaggedSets.WIRE_16X, material, 1),
					MiscUtils.getItemTag(NCMaterialTaggedSets.WIRE_8X, material), 2);
			shapeless(output, "1x_%s_wire_from_12x_wire".formatted(material.getName()), CAPI.materials().getItem(NCMaterialTaggedSets.WIRE_1X, material, 12),
					MiscUtils.getItemTag(NCMaterialTaggedSets.WIRE_12X, material));
			shapeless(output, "12x_%s_wire_from_8x_wire_and_4x_wire".formatted(material.getName()), CAPI.materials().getItem(NCMaterialTaggedSets.WIRE_12X, material, 1),
					MiscUtils.getItemTag(NCMaterialTaggedSets.WIRE_8X, material), MiscUtils.getItemTag(NCMaterialTaggedSets.WIRE_4X, material));
			shapeless(output, "1x_%s_wire_from_16x_wire".formatted(material.getName()), CAPI.materials().getItem(NCMaterialTaggedSets.WIRE_1X, material, 16),
					MiscUtils.getItemTag(NCMaterialTaggedSets.WIRE_16X, material));
		});
	}

	private static void addIngotRecipes(final RecipeOutput output, final RecipeBuilderFactory builderFactory, final Material material) {
		material.executeIf(NCMaterialFlags.GENERATE_PLATE, () -> {
			shapeless(output, "%s_plate".formatted(material.getName()), CAPI.materials().getItem(NCMaterialTaggedSets.PLATE, material, 1),
					'H', MiscUtils.getItemTag(NCMaterialTaggedSets.INGOT, material), 2);
			shapeless(output, "double_%s_plate".formatted(material.getName()), CAPI.materials().getItem(NCMaterialTaggedSets.PLATE_DOUBLE, material, 1),
					'H', MiscUtils.getItemTag(NCMaterialTaggedSets.PLATE, material), 2);
			matRecipe(output, builderFactory, "%s_plate", material, NCRecipeTypes.BENDING_MACHINE, NCMaterialTaggedSets.INGOT, NCMaterialTaggedSets.PLATE, b -> b.program(1));
			matRecipe(output, builderFactory, "%s_double_plate", material, NCRecipeTypes.BENDING_MACHINE, NCMaterialTaggedSets.INGOT, NCMaterialTaggedSets.PLATE_DOUBLE, b -> b.program(2));
			matRecipe(output, builderFactory, "%s_dense_plate", material, NCRecipeTypes.BENDING_MACHINE, NCMaterialTaggedSets.INGOT, NCMaterialTaggedSets.PLATE_DENSE, b -> b.program(9));
			matRecipe(output, builderFactory, "%s_double_plate_from_plate", material, NCRecipeTypes.BENDING_MACHINE, NCMaterialTaggedSets.PLATE, NCMaterialTaggedSets.PLATE_DOUBLE, b -> b.program(2));
			matRecipe(output, builderFactory, "%s_dense_plate_from_plate", material, NCRecipeTypes.BENDING_MACHINE, NCMaterialTaggedSets.PLATE, NCMaterialTaggedSets.PLATE_DENSE, b -> b.program(9));
		});
		material.executeIf(NCMaterialFlags.GENERATE_ROD, () -> {
			matRecipe(output, builderFactory, "%s_rod", material, NCRecipeTypes.LATHE, NCMaterialTaggedSets.INGOT, NCMaterialTaggedSets.ROD, null);
		});
		material.executeIf(NCMaterialTraits.WIRE, trait -> {
			final AutoRecipeData pair = CAPI.recipeHelper().calculateRecipeData(material, NCMaterialTaggedSets.INGOT, NCMaterialTaggedSets.WIRE_1X, (int) material.getMass(), NCTiers.LV.getRecipeVoltage());
			matRecipe(output, builderFactory, "1x_%s_wire", material, NCRecipeTypes.WIREMILL, NCMaterialTaggedSets.INGOT, NCMaterialTaggedSets.WIRE_1X, b -> b.processTime(pair.processTime()));
			matRecipe(output, builderFactory, "2x_%s_wire", material, NCRecipeTypes.WIREMILL, NCMaterialTaggedSets.INGOT, NCMaterialTaggedSets.WIRE_2X, b -> b.processTime(pair.processTime()));
			matRecipe(output, builderFactory, "4x_%s_wire", material, NCRecipeTypes.WIREMILL, NCMaterialTaggedSets.INGOT, NCMaterialTaggedSets.WIRE_4X, b -> b.processTime(pair.processTime() * 2));
			matRecipe(output, builderFactory, "8x_%s_wire", material, NCRecipeTypes.WIREMILL, NCMaterialTaggedSets.INGOT, NCMaterialTaggedSets.WIRE_8X, b -> b.processTime(pair.processTime() * 2));
			matRecipe(output, builderFactory, "12x_%s_wire", material, NCRecipeTypes.WIREMILL, NCMaterialTaggedSets.INGOT, NCMaterialTaggedSets.WIRE_12X, b -> b.processTime(pair.processTime() * 4));
			matRecipe(output, builderFactory, "16x_%s_wire", material, NCRecipeTypes.WIREMILL, NCMaterialTaggedSets.INGOT, NCMaterialTaggedSets.WIRE_16X, b -> b.processTime(pair.processTime() * 4));
		});
	}

	private static void addGemRecipes(final RecipeOutput output, final RecipeBuilderFactory builderFactory, final Material material) {
		material.executeIf(NCMaterialFlags.GENERATE_ROD, () -> {
			matRecipe(output, builderFactory, "%s_rod", material, NCRecipeTypes.LATHE, NCMaterialTaggedSets.GEM, NCMaterialTaggedSets.ROD, null);
		});
		material.executeIf(NCMaterialFlags.GENERATE_PLATE, () -> {
			matRecipe(output, builderFactory, "%s_plate", material, NCRecipeTypes.COMPRESSOR, NCMaterialTaggedSets.DUST, NCMaterialTaggedSets.PLATE, b -> b.program(1));
			matRecipe(output, builderFactory, "%s_double_plate", material, NCRecipeTypes.COMPRESSOR, NCMaterialTaggedSets.DUST, NCMaterialTaggedSets.PLATE_DOUBLE, b -> b.program(2));
			matRecipe(output, builderFactory, "%s_dense_plate", material, NCRecipeTypes.COMPRESSOR, NCMaterialTaggedSets.DUST, NCMaterialTaggedSets.PLATE_DENSE, b -> b.program(9));
		});
	}

	private static void addWoodRecipes(final RecipeOutput output, final RecipeBuilderFactory builderFactory, final Material material) {
		material.executeIf(NCMaterialFlags.GENERATE_PLATE, () -> {
			matRecipe(output, builderFactory, "%s_plank", material, NCRecipeTypes.COMPRESSOR, NCMaterialTaggedSets.DUST, NCMaterialTaggedSets.PLATE, null);
			matRecipe(output, builderFactory, "%s_double_plank", material, NCRecipeTypes.COMPRESSOR, NCMaterialTaggedSets.DUST, NCMaterialTaggedSets.PLATE_DOUBLE, b -> b.program(2));
			matRecipe(output, builderFactory, "%s_dense_plank", material, NCRecipeTypes.COMPRESSOR, NCMaterialTaggedSets.DUST, NCMaterialTaggedSets.PLATE_DENSE, b -> b.program(9));
			matRecipe(output, builderFactory, "%s_double_plank_from_plank", material, NCRecipeTypes.COMPRESSOR, NCMaterialTaggedSets.PLATE, NCMaterialTaggedSets.PLATE_DOUBLE, b -> b.program(2));
			matRecipe(output, builderFactory, "%s_dense_plank_from_plank", material, NCRecipeTypes.COMPRESSOR, NCMaterialTaggedSets.PLATE, NCMaterialTaggedSets.PLATE_DENSE, b -> b.program(9));
		});
	}

	private static void addDustRecipes(final RecipeOutput output, final RecipeBuilderFactory builderFactory, final Material material) {
		material.executeIf(NCMaterialFlags.GENERATE_PLATE, () -> {
			matRecipe(output, builderFactory, "%s_plate", material, NCRecipeTypes.COMPRESSOR, NCMaterialTaggedSets.DUST, NCMaterialTaggedSets.PLATE, b -> b.program(1));
			matRecipe(output, builderFactory, "%s_double_plate", material, NCRecipeTypes.COMPRESSOR, NCMaterialTaggedSets.DUST, NCMaterialTaggedSets.PLATE_DOUBLE, b -> b.program(2));
			matRecipe(output, builderFactory, "%s_dense_plate", material, NCRecipeTypes.COMPRESSOR, NCMaterialTaggedSets.DUST, NCMaterialTaggedSets.PLATE_DENSE, b -> b.program(9));
		});
	}

	private MaterialRecipes() {
	}
}
