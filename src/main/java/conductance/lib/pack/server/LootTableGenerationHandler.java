package conductance.lib.pack.server;

import java.util.Map;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import com.mojang.serialization.JsonOps;
import conductance.api.CAPI;
import conductance.api.NCMaterialGenerationHandlers;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.Conductance;

final class LootTableGenerationHandler {

	public static void reload(final HolderLookup.Provider registries) {
		final VanillaBlockLoot blockLoot = new VanillaBlockLoot(registries);
		LootTableGenerationHandler.generateMaterialLootTables(blockLoot, registries);
		CAPI.regs().machines().forEach(machineType -> LootTableGenerationHandler.dropSelf(machineType.getBlock().get(), blockLoot, registries));
	}

	private static void generateMaterialLootTables(final VanillaBlockLoot blockLoot, final HolderLookup.Provider registries) {
		for (final Map.Entry<Material, Map<MaterialGenerationHandler, Block>> rowEntry : Conductance.MATERIALS.getBlockTable().rowMap().entrySet()) {
			final Material material = rowEntry.getKey();
			for (final Map.Entry<MaterialGenerationHandler, Block> entry : rowEntry.getValue().entrySet()) {
				final MaterialGenerationHandler handler = entry.getKey();
				final Block block = entry.getValue();
				if (material == null || handler == null || block == null) {
					continue;
				}
				if (handler.getOreBearer() == null) {
					LootTableGenerationHandler.dropSelf(block, blockLoot, registries);
				} else {
					Item item = CAPI.materials().getItem(material, NCMaterialGenerationHandlers.RAW_ORE);
					if (item == null) {
						item = CAPI.materials().getItem(material, NCMaterialGenerationHandlers.GEM);
					}
					if (item == null) {
						item = CAPI.materials().getItem(material, NCMaterialGenerationHandlers.DUST);
					}
					final LootTable table = blockLoot.createSilkTouchDispatchTable(block,
						blockLoot.applyExplosionDecay(
							block, LootItem.lootTableItem(item)
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(handler.getOreBearer().hasDoubleOutput() ? 2 : 1)))
								.apply(ApplyBonusCount.addOreBonusCount(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE)))
						)
					).build();
					RuntimeDataPack.addBlockLootTable(BuiltInRegistries.BLOCK.getKey(block),
						() -> LootTable.DIRECT_CODEC.encodeStart(registries.createSerializationContext(JsonOps.INSTANCE), table).getOrThrow());
				}
			}
		}
	}

	private static void dropSelf(final Block block, final VanillaBlockLoot loot, final HolderLookup.Provider provider) {
		final LootTable table = loot.createSingleItemTable(block).setParamSet(LootContextParamSets.BLOCK).build();
		RuntimeDataPack.addBlockLootTable(BuiltInRegistries.BLOCK.getKey(block), () ->
			LootTable.DIRECT_CODEC.encodeStart(provider.createSerializationContext(JsonOps.INSTANCE), table).getOrThrow()
		);
	}

	private LootTableGenerationHandler() {
	}
}
