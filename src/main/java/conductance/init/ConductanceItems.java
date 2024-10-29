package conductance.init;

import net.minecraft.world.item.Item;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import conductance.api.CAPI;
import conductance.item.CraftingToolItem;
import conductance.item.MaterialItem;
import conductance.core.apiimpl.ApiBridge;
import conductance.core.apiimpl.MaterialTaggedSet;

@SuppressWarnings("NotNullFieldNotInitialized")
public final class ConductanceItems {

	public static ItemEntry<? extends Item> CRAFTING_TOOL_WRENCH;
	public static ItemEntry<? extends Item> CRAFTING_TOOL_HAMMER;
	public static ItemEntry<? extends Item> CRAFTING_TOOL_WIRE_CUTTERS;

	public static void init() {
		ConductanceItems.generateMaterialItems();
		ConductanceItems.generateCraftingTools();
	}

	private static void generateMaterialItems() {
		CAPI.regs().materials().forEach(material -> CAPI.regs().materialTaggedSets().values().stream().filter(set -> set.canGenerateItem(material)).forEach(set -> {
			final String name = set.getUnlocalizedName(material);
			final ItemBuilder<MaterialItem, Registrate> itemBuilder = ApiBridge.getRegistrate().item(name, props -> new MaterialItem(props, material, set)).model(NonNullBiConsumer.noop())
					.properties(p -> p.stacksTo(set.getMaxStackSize())).color(() -> MaterialItem::handleColorTint);
			if (((MaterialTaggedSet) set).getItemGeneratorCallback() != null) {
				((MaterialTaggedSet) set).getItemGeneratorCallback().accept(material, itemBuilder);
			}
			CAPI.materials().register(set, material, itemBuilder.register());
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

	private ConductanceItems() {
	}
}
