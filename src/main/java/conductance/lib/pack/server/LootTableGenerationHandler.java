package conductance.lib.pack.server;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import com.mojang.serialization.JsonOps;
import conductance.api.block.IGeneratedLootTable;

final class LootTableGenerationHandler {

	public static void reload(final HolderLookup.Provider registries) {
		final VanillaBlockLoot blockLoot = new VanillaBlockLoot(registries);
		for (final Block block : BuiltInRegistries.BLOCK) {
			if (block instanceof final IGeneratedLootTable lootTable) {
				RuntimeDataPack.addBlockLootTable(BuiltInRegistries.BLOCK.getKey(block), () ->
					LootTable.DIRECT_CODEC.encodeStart(registries.createSerializationContext(JsonOps.INSTANCE), lootTable.generateLootTable(registries, blockLoot)).getOrThrow()
				);
			}
		}
	}

	private LootTableGenerationHandler() {
	}
}
