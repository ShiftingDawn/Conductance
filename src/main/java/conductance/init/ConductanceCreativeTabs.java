package conductance.init;

import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import com.tterrag.registrate.util.entry.RegistryEntry;
import conductance.api.CAPI;
import conductance.api.NCMaterialTaggedSets;
import conductance.api.NCMaterials;
import conductance.content.item.IConductanceItem;
import static conductance.core.apiimpl.ApiBridge.getRegistrate;

public final class ConductanceCreativeTabs {

	//@formatter:off
	public static final RegistryEntry<CreativeModeTab, CreativeModeTab> GENERAL = getRegistrate().defaultCreativeTab("general", builder -> builder
			.displayItems(new TabDisplayGen("general"))
			.icon(() -> new ItemStack(ConductanceItems.CRAFTING_TOOL_WRENCH.asItem()))
			.title(Component.translatable("itemGroup.conductance.general"))
			.build()
	).register();

	public static final RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_ITEMS = getRegistrate().defaultCreativeTab("material_items", builder -> builder
			.displayItems(new TabDisplayGen("material_items"))
			.icon(() -> CAPI.materials().getItem(NCMaterialTaggedSets.INGOT, NCMaterials.ALUMINIUM, 1))
			.title(Component.translatable("itemGroup.conductance.material_items"))
			.build()
	).register();
	public static final RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_BLOCKS = getRegistrate().defaultCreativeTab("material_blocks", builder -> builder
			.displayItems(new TabDisplayGen("material_blocks"))
			.icon(() -> CAPI.materials().getItem(NCMaterialTaggedSets.STORAGE_BLOCK, NCMaterials.ALUMINIUM, 1))
			.title(Component.translatable("itemGroup.conductance.material_blocks"))
			.build()
	).register();
	public static final RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_FLUIDS = getRegistrate().defaultCreativeTab("material_fluids", builder -> builder
			.displayItems(new TabDisplayGen("material_fluids"))
			.icon(() -> CAPI.materials().getBucketUnsafe(NCMaterialTaggedSets.LIQUID, NCMaterials.ALUMINIUM).getDefaultInstance())
			.title(Component.translatable("itemGroup.conductance.material_fluids"))
			.build()
	).register();
	//@formatter:on

	public static void init() {
		getRegistrate().addRegisterCallback(Registries.BLOCK, () -> getRegistrate().getAll(Registries.BLOCK).forEach(entry -> {
			RegistryEntry<CreativeModeTab, CreativeModeTab> tab = ConductanceCreativeTabs.GENERAL;
			if (entry.get() instanceof final IConductanceItem conductanceItem) {
				tab = conductanceItem.getCreativeTab();
			} else if (entry.get().asItem() instanceof final IConductanceItem conductanceItem) {
				tab = conductanceItem.getCreativeTab();
			}
			getRegistrate().setCreativeTab(entry, tab);
		}));

		getRegistrate().addRegisterCallback(Registries.ITEM, () -> getRegistrate().getAll(Registries.ITEM).forEach(entry -> {
			final Item item = entry.get();
			if (item instanceof BlockItem) {
				return;
			}
			RegistryEntry<CreativeModeTab, CreativeModeTab> tab = ConductanceCreativeTabs.GENERAL;
			if (entry.get() instanceof final IConductanceItem conductanceItem) {
				tab = conductanceItem.getCreativeTab();
			}
			getRegistrate().setCreativeTab(entry, tab);
		}));
		getRegistrate().defaultCreativeTab(CreativeModeTabs.SEARCH);
	}

	private static final class TabDisplayGen implements CreativeModeTab.DisplayItemsGenerator {

		private final String name;

		private TabDisplayGen(final String name) {
			this.name = name;
		}

		@Override
		public void accept(final CreativeModeTab.ItemDisplayParameters itemDisplayParameters, final CreativeModeTab.Output output) {
			final RegistryEntry<CreativeModeTab, CreativeModeTab> tab = getRegistrate().get(this.name, Registries.CREATIVE_MODE_TAB);
			getRegistrate().getContentsForTab(tab).forEach(entry -> {
				final Object o = entry.get();
				if (o instanceof final IConductanceItem conductanceItem) {
					final NonNullList<ItemStack> list = NonNullList.create();
					conductanceItem.fillItemCategory(tab.get(), list);
					list.forEach(output::accept);
				} else {
					output.accept((ItemLike) o);
				}
			});
		}
	}

	private ConductanceCreativeTabs() {
	}
}
