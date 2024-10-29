package conductance;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import conductance.api.CAPI;
import conductance.api.ConductancePlugin;
import conductance.api.IConductancePlugin;
import conductance.api.NCMaterialTaggedSets;
import conductance.api.plugin.MachineRegister;
import conductance.api.plugin.MaterialFlagRegister;
import conductance.api.plugin.MaterialOreTypeRegister;
import conductance.api.plugin.MaterialOverrideMap;
import conductance.api.plugin.MaterialRegister;
import conductance.api.plugin.MaterialTaggedSetRegister;
import conductance.api.plugin.MaterialTextureSetRegister;
import conductance.api.plugin.MaterialTextureTypeRegister;
import conductance.api.plugin.MaterialTraitRegister;
import conductance.api.plugin.MaterialUnitOverrideMap;
import conductance.api.plugin.PeriodicElementBuilder;
import conductance.api.plugin.RecipeBuilderFactory;
import conductance.api.plugin.RecipeElementTypeRegister;
import conductance.api.plugin.RecipeTypeRegister;
import conductance.api.plugin.TagRegister;
import conductance.runtimepack.server.recipe.RecipeLoader;
import conductance.init.ConductanceItems;
import conductance.init.ConductanceMachines;
import conductance.init.ConductanceMaterialFlags;
import conductance.init.ConductanceMaterialOreTypes;
import conductance.init.ConductanceMaterialTaggedSets;
import conductance.init.ConductanceMaterialTextureSets;
import conductance.init.ConductanceMaterialTextureTypes;
import conductance.init.ConductanceMaterialTraits;
import conductance.init.ConductanceMaterials;
import conductance.init.ConductancePeriodicElements;
import conductance.init.ConductanceRecipeElementTypes;
import conductance.init.ConductanceRecipeTypes;
import static conductance.api.CAPI.UNIT;
import static conductance.api.NCMaterials.AMETHYST;
import static conductance.api.NCMaterials.BLAZE;
import static conductance.api.NCMaterials.BONE;
import static conductance.api.NCMaterials.BRICK;
import static conductance.api.NCMaterials.CALCITE;
import static conductance.api.NCMaterials.CERTUS_QUARTZ;
import static conductance.api.NCMaterials.CHARCOAL;
import static conductance.api.NCMaterials.CLAY;
import static conductance.api.NCMaterials.COAL;
import static conductance.api.NCMaterials.COPPER;
import static conductance.api.NCMaterials.DIAMOND;
import static conductance.api.NCMaterials.EMERALD;
import static conductance.api.NCMaterials.ENDER_EYE;
import static conductance.api.NCMaterials.ENDER_PEARL;
import static conductance.api.NCMaterials.FLINT;
import static conductance.api.NCMaterials.GLASS;
import static conductance.api.NCMaterials.GOLD;
import static conductance.api.NCMaterials.ICE;
import static conductance.api.NCMaterials.IRON;
import static conductance.api.NCMaterials.LAPIS;
import static conductance.api.NCMaterials.NETHER_STAR;
import static conductance.api.NCMaterials.OBSIDIAN;
import static conductance.api.NCMaterials.REDSTONE;
import static conductance.api.NCMaterials.WOOD;

@ConductancePlugin(modid = CAPI.MOD_ID)
public final class ConductanceRootPlugin implements IConductancePlugin {

	@Override
	public void registerPeriodicElements(final PeriodicElementBuilder builder) {
		ConductancePeriodicElements.init(builder);
	}

	@Override
	public void registerMaterialTextureTypes(final MaterialTextureTypeRegister register) {
		ConductanceMaterialTextureTypes.init(register);
	}

	@Override
	public void registerMaterialTextureSets(final MaterialTextureSetRegister register) {
		ConductanceMaterialTextureSets.init(register);
	}

	@Override
	public void registerMaterialTraits(final MaterialTraitRegister register) {
		ConductanceMaterialTraits.init(register);
	}

	@Override
	public void registerMaterialFlags(final MaterialFlagRegister register) {
		ConductanceMaterialFlags.init(register);
	}

	@Override
	public void registerMaterialOreTypes(final MaterialOreTypeRegister register) {
		ConductanceMaterialOreTypes.init(register);
	}

	@Override
	public void registerMaterialTaggedSets(final MaterialTaggedSetRegister register) {
		ConductanceMaterialTaggedSets.init(register);
	}

	@Override
	public void registerMaterials(final MaterialRegister register) {
		ConductanceMaterials.init(register);
	}

	@Override
	public void registerRecipeElementTypes(final RecipeElementTypeRegister register) {
		ConductanceRecipeElementTypes.init(register);
	}

	@Override
	public void registerRecipeTypes(final RecipeTypeRegister register) {
		ConductanceRecipeTypes.init(register);
	}

	@Override
	public void registerMachines(final MachineRegister register) {
		ConductanceMachines.init(register);
	}

	@Override
	public void registerMaterialOverrides(final MaterialOverrideMap overrides) {
		overrides.add(NCMaterialTaggedSets.STORAGE_BLOCK, IRON, Blocks.IRON_BLOCK);
		overrides.add(NCMaterialTaggedSets.STORAGE_BLOCK, GOLD, Blocks.GOLD_BLOCK);
		overrides.add(NCMaterialTaggedSets.STORAGE_BLOCK, COPPER, Blocks.COPPER_BLOCK);
		overrides.add(NCMaterialTaggedSets.STORAGE_BLOCK, REDSTONE, Blocks.REDSTONE_BLOCK);
		overrides.add(NCMaterialTaggedSets.STORAGE_BLOCK, WOOD);
		overrides.add(NCMaterialTaggedSets.STORAGE_BLOCK, CLAY, Blocks.CLAY);
		overrides.add(NCMaterialTaggedSets.STORAGE_BLOCK, BRICK, Blocks.BRICKS);
		overrides.add(NCMaterialTaggedSets.STORAGE_BLOCK, FLINT);
		overrides.add(NCMaterialTaggedSets.STORAGE_BLOCK, ICE, Blocks.ICE);
		overrides.add(NCMaterialTaggedSets.STORAGE_BLOCK, OBSIDIAN, Blocks.OBSIDIAN);
		overrides.add(NCMaterialTaggedSets.STORAGE_BLOCK, DIAMOND, Blocks.DIAMOND_BLOCK);
		overrides.add(NCMaterialTaggedSets.STORAGE_BLOCK, EMERALD, Blocks.EMERALD_BLOCK);
		overrides.add(NCMaterialTaggedSets.STORAGE_BLOCK, COAL, Blocks.COAL_BLOCK);
		overrides.add(NCMaterialTaggedSets.STORAGE_BLOCK, LAPIS, Blocks.LAPIS_BLOCK);
		overrides.add(NCMaterialTaggedSets.STORAGE_BLOCK, CALCITE, Blocks.CALCITE);
		overrides.add(NCMaterialTaggedSets.STORAGE_BLOCK, GLASS, Blocks.GLASS);
		overrides.add(NCMaterialTaggedSets.STORAGE_BLOCK, AMETHYST, Blocks.AMETHYST_BLOCK);
		overrides.add(NCMaterialTaggedSets.STORAGE_BLOCK, BONE, Blocks.BONE_BLOCK);
		overrides.add(NCMaterialTaggedSets.INGOT, IRON, Items.IRON_INGOT);
		overrides.add(NCMaterialTaggedSets.INGOT, GOLD, Items.GOLD_INGOT);
		overrides.add(NCMaterialTaggedSets.INGOT, COPPER, Items.COPPER_INGOT);
		overrides.add(NCMaterialTaggedSets.INGOT, BRICK, Items.BRICK);
		overrides.add(NCMaterialTaggedSets.INGOT, CLAY, Items.CLAY_BALL);
		overrides.add(NCMaterialTaggedSets.NUGGET, IRON, Items.IRON_NUGGET);
		overrides.add(NCMaterialTaggedSets.NUGGET, GOLD, Items.GOLD_NUGGET);
		overrides.gemOnly(FLINT, Items.FLINT);
		overrides.gemOnly(COAL, Items.COAL);
		overrides.gemOnly(CHARCOAL, Items.CHARCOAL);
		overrides.gemOnly(LAPIS, Items.LAPIS_LAZULI);
		overrides.gemOnly(ENDER_PEARL, Items.ENDER_PEARL);
		overrides.gemOnly(ENDER_EYE, Items.ENDER_EYE);
		overrides.gemOnly(AMETHYST, Items.AMETHYST_SHARD);
		overrides.gemOnly(NETHER_STAR, Items.NETHER_STAR);
		overrides.add(NCMaterialTaggedSets.GEM, DIAMOND, Items.DIAMOND);
		overrides.add(NCMaterialTaggedSets.GEM, EMERALD, Items.EMERALD);
		overrides.add(NCMaterialTaggedSets.DUST, REDSTONE, Items.REDSTONE);
		overrides.add(NCMaterialTaggedSets.DUST, BLAZE, Items.BLAZE_POWDER);
		overrides.add(NCMaterialTaggedSets.DUST, BONE, Items.BONE_MEAL);
		overrides.add(NCMaterialTaggedSets.ROD, WOOD, Items.STICK);
		overrides.add(NCMaterialTaggedSets.ROD, BLAZE, Items.BLAZE_ROD);
		overrides.add(NCMaterialTaggedSets.ROD, BONE, Items.BONE);
		overrides.add(NCMaterialTaggedSets.RAW_ORE, IRON, Items.RAW_IRON);
		overrides.add(NCMaterialTaggedSets.RAW_ORE, GOLD, Items.RAW_GOLD);
		overrides.add(NCMaterialTaggedSets.RAW_ORE, COPPER, Items.RAW_COPPER);
		overrides.add(NCMaterialTaggedSets.RAW_ORE_BLOCK, IRON, Blocks.RAW_IRON_BLOCK);
		overrides.add(NCMaterialTaggedSets.RAW_ORE_BLOCK, GOLD, Blocks.RAW_GOLD_BLOCK);
		overrides.add(NCMaterialTaggedSets.RAW_ORE_BLOCK, COPPER, Blocks.RAW_COPPER_BLOCK);
	}

	@Override
	public void registerMaterialUnitOverrides(final MaterialUnitOverrideMap overrides) {
		overrides.add(NCMaterialTaggedSets.STORAGE_BLOCK, CLAY, UNIT * 4);
		overrides.add(NCMaterialTaggedSets.STORAGE_BLOCK, BRICK, UNIT * 4);
		overrides.add(NCMaterialTaggedSets.STORAGE_BLOCK, ICE, UNIT);
		overrides.add(NCMaterialTaggedSets.STORAGE_BLOCK, CALCITE, UNIT * 4);
		overrides.add(NCMaterialTaggedSets.STORAGE_BLOCK, GLASS, UNIT);
		overrides.add(NCMaterialTaggedSets.STORAGE_BLOCK, CERTUS_QUARTZ, UNIT * 4);
		overrides.add(NCMaterialTaggedSets.STORAGE_BLOCK, AMETHYST, UNIT * 4);
		overrides.add(NCMaterialTaggedSets.ROD, BLAZE, UNIT * 4);
		overrides.add(NCMaterialTaggedSets.ROD, BONE, UNIT * 5);
	}

	@Override
	public void registerTags(final TagRegister tags) {
		tags.item(CAPI.Tags.TAG_WRENCH, ConductanceItems.CRAFTING_TOOL_WRENCH);
		tags.item(CAPI.Tags.TAG_HAMMER, ConductanceItems.CRAFTING_TOOL_HAMMER);
		tags.item(CAPI.Tags.TAG_WIRE_CUTTERS, ConductanceItems.CRAFTING_TOOL_WIRE_CUTTERS);
	}

	@Override
	public void registerRecipes(final RecipeOutput recipeOutput, final RecipeBuilderFactory builderFactory) {
		RecipeLoader.init(recipeOutput, builderFactory);
	}
}
