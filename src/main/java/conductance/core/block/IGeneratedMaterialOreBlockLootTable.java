package conductance.core.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import conductance.api.CAPI;
import conductance.api.NCMaterialGenerationHandlers;
import conductance.api.block.IGeneratedLootTable;
import conductance.api.material.MaterialOreBearer;

public interface IGeneratedMaterialOreBlockLootTable extends IGeneratedLootTable, IMaterialBlock {

	MaterialOreBearer getOreBearer();

	@Override
	default ItemLike getDroppedItem() {
		return CAPI.materials().getItem(this.getMaterial(), NCMaterialGenerationHandlers.RAW_ORE);
	}

	@Override
	default NumberProvider getDroppedAmount() {
		return ConstantValue.exactly(this.getOreBearer().hasDoubleOutput() ? 2 : 1);
	}

	@Override
	default LootTable generateLootTable(final HolderLookup.Provider registries, final VanillaBlockLoot blockLoot) {
		final Block self = (Block) this;
		return blockLoot.createSilkTouchDispatchTable(self, blockLoot.applyExplosionDecay(self, LootItem.lootTableItem(this.getDroppedItem())
			.apply(SetItemCountFunction.setCount(this.getDroppedAmount()))
			.apply(ApplyBonusCount.addOreBonusCount(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE)))
		)).build();
	}
}
