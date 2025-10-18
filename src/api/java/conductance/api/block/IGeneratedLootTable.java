package conductance.api.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public interface IGeneratedLootTable {

	default ItemLike getDroppedItem() {
		return (ItemLike) this;
	}

	default NumberProvider getDroppedAmount() {
		return ConstantValue.exactly(1);
	}

	default LootTable generateLootTable(final HolderLookup.Provider registries, final VanillaBlockLoot blockLoot) {
		return blockLoot.createSingleItemTable(this.getDroppedItem(), this.getDroppedAmount()).setParamSet(LootContextParamSets.BLOCK).build();
	}
}
