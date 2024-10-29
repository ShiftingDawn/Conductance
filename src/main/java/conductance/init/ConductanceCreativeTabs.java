package conductance.init;

import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import com.tterrag.registrate.util.entry.RegistryEntry;
import conductance.Conductance;
import conductance.core.IConductanceItem;
import static conductance.core.apiimpl.ApiBridge.REGISTRATE;

public final class ConductanceCreativeTabs {

	//@formatter:off
	public static final RegistryEntry<CreativeModeTab, CreativeModeTab> GENERAL = REGISTRATE.defaultCreativeTab("general", builder -> builder
			.displayItems(new TabDisplayGen("general"))
			.icon(() -> new ItemStack(Items.GOLD_INGOT))
			.title(REGISTRATE.addLang("itemGroup", Conductance.id("general"), "Conductance"))
			.build()
	).register();

	public static final RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_ITEMS = REGISTRATE.defaultCreativeTab("material_items", builder -> builder
			.displayItems(new TabDisplayGen("material_items"))
			.icon(() -> new ItemStack(Items.DIAMOND))
			.title(REGISTRATE.addLang("itemGroup", Conductance.id("material_items"), "Conductance Material items"))
			.build()
	).register();
	//@formatter:on

	public static void init() {
		REGISTRATE.addRegisterCallback(Registries.BLOCK, () -> REGISTRATE.getAll(Registries.BLOCK).forEach(entry -> {
			RegistryEntry<CreativeModeTab, CreativeModeTab> tab = ConductanceCreativeTabs.GENERAL;
			if (entry.get() instanceof final IConductanceItem conductanceItem) {
				tab = conductanceItem.getCreativeTab();
			} else if (entry.get().asItem() instanceof final IConductanceItem conductanceItem) {
				tab = conductanceItem.getCreativeTab();
			}
			REGISTRATE.setCreativeTab(entry, tab);
		}));

		REGISTRATE.addRegisterCallback(Registries.ITEM, () -> REGISTRATE.getAll(Registries.ITEM).forEach(entry -> {
			final Item item = entry.get();
			if (item instanceof BlockItem) {
				return;
			}
			RegistryEntry<CreativeModeTab, CreativeModeTab> tab = ConductanceCreativeTabs.GENERAL;
			if (entry.get() instanceof final IConductanceItem conductanceItem) {
				tab = conductanceItem.getCreativeTab();
			}
			REGISTRATE.setCreativeTab(entry, tab);
		}));
		REGISTRATE.defaultCreativeTab(CreativeModeTabs.SEARCH);
	}

	private static final class TabDisplayGen implements CreativeModeTab.DisplayItemsGenerator {

		private final String name;

		private TabDisplayGen(String name) {
			this.name = name;
		}

		@Override
		public void accept(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
			RegistryEntry<CreativeModeTab, CreativeModeTab> tab = REGISTRATE.get(this.name, Registries.CREATIVE_MODE_TAB);
			REGISTRATE.getContentsForTab(tab).forEach(entry -> {
				Object o = entry.get();
				if (o instanceof IConductanceItem conductanceItem) {
					final NonNullList<ItemStack> list = NonNullList.create();
					conductanceItem.fillItemCategory(tab.get(), list);
					list.forEach(output::accept);
				} else {
					output.accept((ItemLike) o);
				}
			});
		}
	}
}
