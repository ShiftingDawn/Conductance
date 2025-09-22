package conductance.init;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import conductance.api.material.event.RegisterMaterialOverridesEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.Conductance;
import static conductance.api.NCMaterialGenerationHandlers.DUST;
import static conductance.api.NCMaterialGenerationHandlers.GEM;
import static conductance.api.NCMaterialGenerationHandlers.INGOT;
import static conductance.api.NCMaterialGenerationHandlers.LIQUID;
import static conductance.api.NCMaterialGenerationHandlers.NUGGET;
import static conductance.api.NCMaterialGenerationHandlers.ORE_DEEPSLATE;
import static conductance.api.NCMaterialGenerationHandlers.ORE_NETHERRACK;
import static conductance.api.NCMaterialGenerationHandlers.ORE_STONE;
import static conductance.api.NCMaterialGenerationHandlers.RAW_ORE;
import static conductance.api.NCMaterialGenerationHandlers.RAW_ORE_BLOCK;
import static conductance.api.NCMaterialGenerationHandlers.ROD;
import static conductance.api.NCMaterialGenerationHandlers.STORAGE_BLOCK;
import static conductance.api.NCMaterials.AMETHYST;
import static conductance.api.NCMaterials.BLAZE;
import static conductance.api.NCMaterials.BONE;
import static conductance.api.NCMaterials.BRICK;
import static conductance.api.NCMaterials.CALCITE;
import static conductance.api.NCMaterials.CHARCOAL;
import static conductance.api.NCMaterials.CLAY;
import static conductance.api.NCMaterials.COAL;
import static conductance.api.NCMaterials.COPPER;
import static conductance.api.NCMaterials.DIAMOND;
import static conductance.api.NCMaterials.EMERALD;
import static conductance.api.NCMaterials.ENDER_PEARL;
import static conductance.api.NCMaterials.EYE_OF_ENDER;
import static conductance.api.NCMaterials.FLINT;
import static conductance.api.NCMaterials.GLASS;
import static conductance.api.NCMaterials.GOLD;
import static conductance.api.NCMaterials.ICE;
import static conductance.api.NCMaterials.IRON;
import static conductance.api.NCMaterials.LAPIS_LAZULI;
import static conductance.api.NCMaterials.NETHER_QUARTZ;
import static conductance.api.NCMaterials.NETHER_STAR;
import static conductance.api.NCMaterials.OBSIDIAN;
import static conductance.api.NCMaterials.REDSTONE;
import static conductance.api.NCMaterials.WATER;
import static conductance.api.NCMaterials.WOOD;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceMaterialOverrides {

	@EventListener(priority = -100)
	private static void init(final RegisterMaterialOverridesEvent event) {
		event.add(STORAGE_BLOCK, IRON, Blocks.IRON_BLOCK);
		event.add(STORAGE_BLOCK, GOLD, Blocks.GOLD_BLOCK);
		event.add(STORAGE_BLOCK, COPPER, Blocks.COPPER_BLOCK);
		event.add(STORAGE_BLOCK, REDSTONE, Blocks.REDSTONE_BLOCK);
		event.add(STORAGE_BLOCK, WOOD, (Block) null);
		event.add(STORAGE_BLOCK, CLAY, Blocks.CLAY);
		event.add(STORAGE_BLOCK, BRICK, Blocks.BRICKS);
		event.add(STORAGE_BLOCK, ICE, Blocks.ICE);
		event.add(STORAGE_BLOCK, OBSIDIAN, Blocks.OBSIDIAN);
		event.add(STORAGE_BLOCK, DIAMOND, Blocks.DIAMOND_BLOCK);
		event.add(STORAGE_BLOCK, EMERALD, Blocks.EMERALD_BLOCK);
		event.add(STORAGE_BLOCK, COAL, Blocks.COAL_BLOCK);
		event.add(STORAGE_BLOCK, LAPIS_LAZULI, Blocks.LAPIS_BLOCK);
		event.add(STORAGE_BLOCK, CALCITE, Blocks.CALCITE);
		event.add(STORAGE_BLOCK, GLASS, Blocks.GLASS);
		event.add(STORAGE_BLOCK, AMETHYST, Blocks.AMETHYST_BLOCK);
		event.add(STORAGE_BLOCK, BONE, Blocks.BONE_BLOCK);

		event.add(INGOT, IRON, Items.IRON_INGOT);
		event.add(INGOT, GOLD, Items.GOLD_INGOT);
		event.add(INGOT, COPPER, Items.COPPER_INGOT);
		event.add(INGOT, BRICK, Items.BRICK);
		event.add(INGOT, CLAY, Items.CLAY_BALL);

		event.add(NUGGET, IRON, Items.IRON_NUGGET);
		event.add(NUGGET, GOLD, Items.GOLD_NUGGET);

		event.add(GEM, COAL, Items.COAL);
		event.add(GEM, CHARCOAL, Items.CHARCOAL);
		event.add(GEM, FLINT, Items.FLINT);
		event.add(GEM, LAPIS_LAZULI, Items.LAPIS_LAZULI);
		event.add(GEM, ENDER_PEARL, Items.ENDER_PEARL);
		event.add(GEM, EYE_OF_ENDER, Items.ENDER_EYE);
		event.add(GEM, AMETHYST, Items.AMETHYST_SHARD);
		event.add(GEM, NETHER_STAR, Items.NETHER_STAR);
		event.add(GEM, DIAMOND, Items.DIAMOND);
		event.add(GEM, EMERALD, Items.EMERALD);

		event.add(DUST, REDSTONE, Items.REDSTONE);
		event.add(DUST, BLAZE, Items.BLAZE_POWDER);
		event.add(DUST, BONE, Items.BONE_MEAL);

		event.add(ROD, WOOD, Items.STICK);
		event.add(ROD, BLAZE, Items.BLAZE_ROD);
		event.add(ROD, BONE, Items.BONE);

		event.add(ORE_STONE, COAL, Blocks.COAL_ORE);
		event.add(ORE_STONE, IRON, Blocks.IRON_ORE);
		event.add(ORE_STONE, GOLD, Blocks.GOLD_ORE);
		event.add(ORE_STONE, COPPER, Blocks.COPPER_ORE);
		event.add(ORE_STONE, LAPIS_LAZULI, Blocks.LAPIS_ORE);
		event.add(ORE_STONE, REDSTONE, Blocks.REDSTONE_ORE);
		event.add(ORE_STONE, EMERALD, Blocks.EMERALD_ORE);
		event.add(ORE_STONE, DIAMOND, Blocks.DIAMOND_ORE);

		event.add(ORE_DEEPSLATE, COAL, Blocks.DEEPSLATE_COAL_ORE);
		event.add(ORE_DEEPSLATE, IRON, Blocks.DEEPSLATE_IRON_ORE);
		event.add(ORE_DEEPSLATE, GOLD, Blocks.DEEPSLATE_GOLD_ORE);
		event.add(ORE_DEEPSLATE, COPPER, Blocks.DEEPSLATE_COPPER_ORE);
		event.add(ORE_DEEPSLATE, LAPIS_LAZULI, Blocks.DEEPSLATE_LAPIS_ORE);
		event.add(ORE_DEEPSLATE, REDSTONE, Blocks.DEEPSLATE_REDSTONE_ORE);
		event.add(ORE_DEEPSLATE, EMERALD, Blocks.DEEPSLATE_EMERALD_ORE);
		event.add(ORE_DEEPSLATE, DIAMOND, Blocks.DEEPSLATE_DIAMOND_ORE);

		event.add(ORE_NETHERRACK, GOLD, Blocks.NETHER_GOLD_ORE);
		event.add(ORE_NETHERRACK, NETHER_QUARTZ, Blocks.NETHER_QUARTZ_ORE);

		event.add(RAW_ORE, IRON, Items.RAW_IRON);
		event.add(RAW_ORE, GOLD, Items.RAW_GOLD);
		event.add(RAW_ORE, COPPER, Items.RAW_COPPER);

		event.add(RAW_ORE_BLOCK, IRON, Blocks.RAW_IRON_BLOCK);
		event.add(RAW_ORE_BLOCK, GOLD, Blocks.RAW_GOLD_BLOCK);
		event.add(RAW_ORE_BLOCK, COPPER, Blocks.RAW_COPPER_BLOCK);

		event.add(LIQUID, WATER, Items.WATER_BUCKET);
		event.add(LIQUID, WATER, Fluids.WATER);
	}

	private ConductanceMaterialOverrides() {
	}
}
