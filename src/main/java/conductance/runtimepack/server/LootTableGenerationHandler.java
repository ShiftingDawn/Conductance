package conductance.runtimepack.server;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import com.mojang.serialization.JsonOps;
import conductance.api.CAPI;
import conductance.core.register.MaterialRegistryImpl;

final class LootTableGenerationHandler {

	static void generate(final HolderLookup.Provider provider) {
		final VanillaBlockLoot loot = new VanillaBlockLoot(provider);
		LootTableGenerationHandler.generateMaterialBlocks(loot, provider);
		CAPI.regs().machines().forEach(machineType -> LootTableGenerationHandler.dropSelf(machineType.getBlock().get(), loot, provider));
	}

	private static void generateMaterialBlocks(final VanillaBlockLoot loot, final HolderLookup.Provider provider) {
		MaterialRegistryImpl.INSTANCE.getBlockTable().rowMap().forEach((taggedSet, map) -> map.forEach((material, blocks) -> blocks.forEach(block -> {
			LootTableGenerationHandler.dropSelf(block, loot, provider);
		})));
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
