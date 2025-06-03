package conductance.init;

import java.util.EnumMap;
import java.util.HashMap;
import net.minecraft.Util;
import net.minecraft.world.item.Item;
import com.google.common.collect.Tables;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import conductance.api.CAPI;
import conductance.api.NCCovers;
import conductance.api.util.TieredItemType;
import conductance.api.tier.Tier;
import conductance.core.apiimpl.ApiBridge;
import conductance.core.material.TaggedMaterialSetImpl;
import conductance.core.material.MaterialRegistryImpl;
import conductance.init.item.CraftingToolItem;
import conductance.init.item.MaterialItem;
import conductance.init.item.TieredCoverItem;
import conductance.init.item.TieredItem;
import static conductance.api.NCItems.TIERED;

@SuppressWarnings("NotNullFieldNotInitialized")
public final class ConductanceItems {

	public static ItemEntry<? extends Item> CRAFTING_TOOL_WRENCH;
	public static ItemEntry<? extends Item> CRAFTING_TOOL_HAMMER;
	public static ItemEntry<? extends Item> CRAFTING_TOOL_WIRE_CUTTERS;

	public static void init() {
		ConductanceItems.generateMaterialItems();
		ConductanceItems.generateCraftingTools();

		ConductanceItems.generateTieredItems();
	}

	private static void generateMaterialItems() {
		CAPI.regs().materials().forEach(material -> CAPI.regs().materialTaggedSets().values().stream().filter(set -> set.canGenerateItem(material)).forEach(set -> {
			final String name = set.getUnlocalizedName(material);
			final ItemBuilder<MaterialItem, Registrate> itemBuilder = ApiBridge.getRegistrate().item(name, props -> new MaterialItem(props, material, set)).model(NonNullBiConsumer.noop())
					.properties(p -> p.stacksTo(set.getMaxStackSize())).color(() -> MaterialItem::handleColorTint);
			if (((TaggedMaterialSetImpl) set).getItemGeneratorCallback() != null) {
				((TaggedMaterialSetImpl) set).getItemGeneratorCallback().accept(material, itemBuilder);
			}
			MaterialRegistryImpl.INSTANCE.register(set, material, itemBuilder.register());
		}));
	}

	private static void generateCraftingTools() {
		ConductanceItems.CRAFTING_TOOL_WRENCH = ApiBridge.getRegistrate().item("wrench", CraftingToolItem::new)
				.defaultModel().register();
		ConductanceItems.CRAFTING_TOOL_HAMMER = ApiBridge.getRegistrate().item("hammer", CraftingToolItem::new)
				.defaultModel().register();
		ConductanceItems.CRAFTING_TOOL_WIRE_CUTTERS = ApiBridge.getRegistrate().item("wire_cutters", CraftingToolItem::new)
				.defaultModel().register();
	}

	private static void generateTieredItems() {
		TIERED = Tables.unmodifiableTable(Util.make(Tables.newCustomTable(new EnumMap<>(TieredItemType.class), HashMap::new), table -> {
			for (final TieredItemType tieredItemType : TieredItemType.values()) {
				for (final Tier tier : CAPI.tiers().getTiers()) {
					final String name = tieredItemType.getUnlocalizedNameFactory().formatted(tier.getRegistryKey());
					final ItemEntry<? extends Item> item = ApiBridge.getRegistrate().item(name, props -> switch (tieredItemType) {
								case CONVEYOR_MODULE -> new TieredCoverItem<>(props, tieredItemType, tier, NCCovers.CONVEYORS.get(tier));
								default -> new TieredItem(props, tieredItemType, tier);
							})
							.model(NonNullBiConsumer.noop())
							.color(() -> TieredItem::handleColorTint)
							.register();
					table.put(tieredItemType, tier, item);
				}
			}
		}));
	}

	private ConductanceItems() {
	}
}
