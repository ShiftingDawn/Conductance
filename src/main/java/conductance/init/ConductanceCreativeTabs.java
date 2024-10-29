package conductance.init;

import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import com.tterrag.registrate.util.entry.RegistryEntry;
import conductance.api.CAPI;
import conductance.api.NCMaterialTaggedSets;
import conductance.api.NCMaterials;
import conductance.content.item.IConductanceItem;
import conductance.core.register.MaterialRegistry;
import static conductance.core.apiimpl.ApiBridge.REGISTRATE;

public final class ConductanceCreativeTabs {

	//@formatter:off
	public static final RegistryEntry<CreativeModeTab, CreativeModeTab> GENERAL = REGISTRATE.defaultCreativeTab("general", builder -> builder
			.displayItems(new TabDisplayGen("general"))
			.icon(() -> new ItemStack(Items.GOLD_INGOT))
			.title(Component.translatable("itemGroup.conductance.general"))
			.build()
	).register();

	public static final RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_ITEMS = REGISTRATE.defaultCreativeTab("material_items", builder -> builder
			.displayItems(new TabDisplayGen("material_items"))
			.icon(() -> CAPI.MATERIALS.getItem(NCMaterialTaggedSets.INGOT, NCMaterials.ALUMINIUM, 1))
			.title(Component.translatable("itemGroup.conductance.material_items"))
			.build()
	).register();
	public static final RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_BLOCKS = REGISTRATE.defaultCreativeTab("material_blocks", builder -> builder
			.displayItems(new TabDisplayGen("material_blocks"))
			.icon(() -> CAPI.MATERIALS.getItem(NCMaterialTaggedSets.STORAGE_BLOCK, NCMaterials.ALUMINIUM, 1))
			.title(Component.translatable("itemGroup.conductance.material_blocks"))
			.build()
	).register();
	public static final RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_FLUIDS = REGISTRATE.defaultCreativeTab("material_fluids", builder -> builder
			.displayItems(new TabDisplayGen("material_fluids"))
			.icon(() -> CAPI.MATERIALS.getBucketUnsafe(NCMaterialTaggedSets.LIQUID, NCMaterials.ALUMINIUM).getDefaultInstance())
			.title(Component.translatable("itemGroup.conductance.material_fluids"))
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

		private TabDisplayGen(final String name) {
			this.name = name;
		}

		@Override
		public void accept(final CreativeModeTab.ItemDisplayParameters itemDisplayParameters, final CreativeModeTab.Output output) {
			final RegistryEntry<CreativeModeTab, CreativeModeTab> tab = REGISTRATE.get(this.name, Registries.CREATIVE_MODE_TAB);
			REGISTRATE.getContentsForTab(tab).forEach(entry -> {
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
}
