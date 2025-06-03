package conductance;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import conductance.api.CAPI;
import conductance.api.NCMaterialTaggedSets;
import conductance.api.material.event.RegisterMaterialOverrideEvent;
import conductance.api.material.event.RegisterMaterialUnitOverrideEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.plugin.RegisterTagEvent;
import conductance.init.ConductanceItems;
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
import static conductance.api.NCMaterials.LAPIS_LAZULI;
import static conductance.api.NCMaterials.NETHER_QUARTZ;
import static conductance.api.NCMaterials.NETHER_STAR;
import static conductance.api.NCMaterials.OBSIDIAN;
import static conductance.api.NCMaterials.REDSTONE;
import static conductance.api.NCMaterials.WOOD;

@ConductancePluginListener(modid = Conductance.MODID)
public final class ConductanceMiscPluginEventListeners {

	@EventListener(priority = -100)
	private static void onRegisterMaterialOverrides(final RegisterMaterialOverrideEvent event) {
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, IRON, Blocks.IRON_BLOCK);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, GOLD, Blocks.GOLD_BLOCK);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, COPPER, Blocks.COPPER_BLOCK);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, REDSTONE, Blocks.REDSTONE_BLOCK);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, WOOD);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, CLAY, Blocks.CLAY);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, BRICK, Blocks.BRICKS);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, FLINT);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, ICE, Blocks.ICE);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, OBSIDIAN, Blocks.OBSIDIAN);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, DIAMOND, Blocks.DIAMOND_BLOCK);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, EMERALD, Blocks.EMERALD_BLOCK);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, COAL, Blocks.COAL_BLOCK);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, LAPIS_LAZULI, Blocks.LAPIS_BLOCK);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, CALCITE, Blocks.CALCITE);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, GLASS, Blocks.GLASS);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, AMETHYST, Blocks.AMETHYST_BLOCK);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, BONE, Blocks.BONE_BLOCK);
		event.add(NCMaterialTaggedSets.INGOT, IRON, Items.IRON_INGOT);
		event.add(NCMaterialTaggedSets.INGOT, GOLD, Items.GOLD_INGOT);
		event.add(NCMaterialTaggedSets.INGOT, COPPER, Items.COPPER_INGOT);
		event.add(NCMaterialTaggedSets.INGOT, BRICK, Items.BRICK);
		event.add(NCMaterialTaggedSets.INGOT, CLAY, Items.CLAY_BALL);
		event.add(NCMaterialTaggedSets.NUGGET, IRON, Items.IRON_NUGGET);
		event.add(NCMaterialTaggedSets.NUGGET, GOLD, Items.GOLD_NUGGET);
		event.gemOnly(FLINT, Items.FLINT);
		event.gemOnly(COAL, Items.COAL);
		event.gemOnly(CHARCOAL, Items.CHARCOAL);
		event.gemOnly(LAPIS_LAZULI, Items.LAPIS_LAZULI);
		event.gemOnly(ENDER_PEARL, Items.ENDER_PEARL);
		event.gemOnly(ENDER_EYE, Items.ENDER_EYE);
		event.gemOnly(AMETHYST, Items.AMETHYST_SHARD);
		event.gemOnly(NETHER_STAR, Items.NETHER_STAR);
		event.add(NCMaterialTaggedSets.GEM, DIAMOND, Items.DIAMOND);
		event.add(NCMaterialTaggedSets.GEM, EMERALD, Items.EMERALD);
		event.add(NCMaterialTaggedSets.DUST, REDSTONE, Items.REDSTONE);
		event.add(NCMaterialTaggedSets.DUST, BLAZE, Items.BLAZE_POWDER);
		event.add(NCMaterialTaggedSets.DUST, BONE, Items.BONE_MEAL);
		event.add(NCMaterialTaggedSets.ROD, WOOD, Items.STICK);
		event.add(NCMaterialTaggedSets.ROD, BLAZE, Items.BLAZE_ROD);
		event.add(NCMaterialTaggedSets.ROD, BONE, Items.BONE);
		event.add(NCMaterialTaggedSets.ORE_STONE, COAL, Blocks.COAL_ORE);
		event.add(NCMaterialTaggedSets.ORE_STONE, IRON, Blocks.IRON_ORE);
		event.add(NCMaterialTaggedSets.ORE_STONE, GOLD, Blocks.GOLD_ORE);
		event.add(NCMaterialTaggedSets.ORE_STONE, COPPER, Blocks.COPPER_ORE);
		event.add(NCMaterialTaggedSets.ORE_STONE, LAPIS_LAZULI, Blocks.LAPIS_ORE);
		event.add(NCMaterialTaggedSets.ORE_STONE, REDSTONE, Blocks.REDSTONE_ORE);
		event.add(NCMaterialTaggedSets.ORE_STONE, EMERALD, Blocks.EMERALD_ORE);
		event.add(NCMaterialTaggedSets.ORE_STONE, DIAMOND, Blocks.DIAMOND_ORE);
		event.add(NCMaterialTaggedSets.ORE_DEEPSLATE, COAL, Blocks.DEEPSLATE_COAL_ORE);
		event.add(NCMaterialTaggedSets.ORE_DEEPSLATE, IRON, Blocks.DEEPSLATE_IRON_ORE);
		event.add(NCMaterialTaggedSets.ORE_DEEPSLATE, GOLD, Blocks.DEEPSLATE_GOLD_ORE);
		event.add(NCMaterialTaggedSets.ORE_DEEPSLATE, COPPER, Blocks.DEEPSLATE_COPPER_ORE);
		event.add(NCMaterialTaggedSets.ORE_DEEPSLATE, LAPIS_LAZULI, Blocks.DEEPSLATE_LAPIS_ORE);
		event.add(NCMaterialTaggedSets.ORE_DEEPSLATE, REDSTONE, Blocks.DEEPSLATE_REDSTONE_ORE);
		event.add(NCMaterialTaggedSets.ORE_DEEPSLATE, EMERALD, Blocks.DEEPSLATE_EMERALD_ORE);
		event.add(NCMaterialTaggedSets.ORE_DEEPSLATE, DIAMOND, Blocks.DEEPSLATE_DIAMOND_ORE);
		event.add(NCMaterialTaggedSets.ORE_NETHERRACK, GOLD, Blocks.NETHER_GOLD_ORE);
		event.add(NCMaterialTaggedSets.ORE_NETHERRACK, NETHER_QUARTZ, Blocks.NETHER_QUARTZ_ORE);
		event.add(NCMaterialTaggedSets.RAW_ORE, IRON, Items.RAW_IRON);
		event.add(NCMaterialTaggedSets.RAW_ORE, GOLD, Items.RAW_GOLD);
		event.add(NCMaterialTaggedSets.RAW_ORE, COPPER, Items.RAW_COPPER);
		event.add(NCMaterialTaggedSets.RAW_ORE_BLOCK, IRON, Blocks.RAW_IRON_BLOCK);
		event.add(NCMaterialTaggedSets.RAW_ORE_BLOCK, GOLD, Blocks.RAW_GOLD_BLOCK);
		event.add(NCMaterialTaggedSets.RAW_ORE_BLOCK, COPPER, Blocks.RAW_COPPER_BLOCK);
	}

	@EventListener(priority = -100)
	private static void onRegisterMaterialUnitOverrides(final RegisterMaterialUnitOverrideEvent event) {
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, CLAY, UNIT * 4);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, BRICK, UNIT * 4);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, ICE, UNIT);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, CALCITE, UNIT * 4);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, GLASS, UNIT);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, CERTUS_QUARTZ, UNIT * 4);
		event.add(NCMaterialTaggedSets.STORAGE_BLOCK, AMETHYST, UNIT * 4);
		event.add(NCMaterialTaggedSets.ROD, BLAZE, UNIT * 4);
		event.add(NCMaterialTaggedSets.ROD, BONE, UNIT * 5);
	}

	@EventListener(priority = -100)
	private static void onRegisterTags(final RegisterTagEvent event) {
		event.item(CAPI.TAG_WRENCHES, ConductanceItems.CRAFTING_TOOL_WRENCH);
		event.item(CAPI.TAG_HAMMERS, ConductanceItems.CRAFTING_TOOL_HAMMER);
		event.item(CAPI.TAG_WIRE_CUTTERS, ConductanceItems.CRAFTING_TOOL_WIRE_CUTTERS);
	}

	private ConductanceMiscPluginEventListeners() {
	}
}
