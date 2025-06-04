package conductance.core.runtimepack.server;

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
import conductance.api.NCMaterialTaggedSets;
import conductance.core.material.MaterialRegistryImpl;

final class LootTableGenerationHandler {

	static void generate(final HolderLookup.Provider provider) {
		final VanillaBlockLoot loot = new VanillaBlockLoot(provider);
		LootTableGenerationHandler.generateMaterialBlocks(loot, provider);
		CAPI.regs().machines().forEach(machineType -> LootTableGenerationHandler.dropSelf(machineType.getBlock().get(), loot, provider));
	}

	private static void generateMaterialBlocks(final VanillaBlockLoot loot, final HolderLookup.Provider provider) {
		MaterialRegistryImpl.INSTANCE.getBlockTable().rowMap().forEach((taggedSet, map) -> {
			if (taggedSet.getOreType() == null) {
				map.forEach((material, blocks) -> blocks.forEach(block -> {
					LootTableGenerationHandler.dropSelf(block, loot, provider);
				}));
			} else {
				map.forEach((material, blocks) -> blocks.forEach(block -> {
					Item item = CAPI.materials().getItemUnsafe(NCMaterialTaggedSets.RAW_ORE, material);
					if (item == null) {
						item = CAPI.materials().getItemUnsafe(NCMaterialTaggedSets.GEM, material);
					}
					if (item == null) {
						item = CAPI.materials().getItemUnsafe(NCMaterialTaggedSets.DUST, material);
					}
					assert item != null;
					final LootTable table = loot.createSilkTouchDispatchTable(block,
							loot.applyExplosionDecay(
									block,
									LootItem.lootTableItem(item)
											.apply(SetItemCountFunction.setCount(ConstantValue.exactly(taggedSet.getOreType().hasDoubleOutput() ? 2 : 1)))
											.apply(ApplyBonusCount.addOreBonusCount(provider.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE)))
							)
					).build();
					RuntimeDataPack.addBlockLootTable(BuiltInRegistries.BLOCK.getKey(block), () ->
							LootTable.DIRECT_CODEC.encodeStart(provider.createSerializationContext(JsonOps.INSTANCE), table).getOrThrow()
					);
				}));
			}
		});
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
