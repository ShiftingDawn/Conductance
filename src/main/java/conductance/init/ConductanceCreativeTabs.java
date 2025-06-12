package conductance.init;

import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import com.tterrag.registrate.util.entry.RegistryEntry;
import conductance.api.CAPI;
import conductance.api.NCDecoration;
import conductance.api.NCMachines;
import conductance.api.NCMaterialTaggedSets;
import conductance.api.NCMaterials;
import conductance.api.NCTiers;
import conductance.init.item.IConductanceItem;
import conductance.lib.pipenet.WireRegistry;
import conductance.lib.pipenet.WireType;
import static conductance.core.register.RegisterCore.REGISTRATE;

public final class ConductanceCreativeTabs {

	//@formatter:off
	public static final RegistryEntry<CreativeModeTab, CreativeModeTab> GENERAL = REGISTRATE.defaultCreativeTab("general", builder -> builder
			.displayItems(new TabDisplayGen("general"))
			.icon(() -> new ItemStack(ConductanceItems.CRAFTING_TOOL_WRENCH.asItem()))
			.title(Component.translatable("itemGroup.conductance.general"))
			.build()
	).register();

	public static final RegistryEntry<CreativeModeTab, CreativeModeTab> MACHINES = REGISTRATE.defaultCreativeTab("machines", builder -> builder
			.displayItems(new TabDisplayGen("machines"))
			.icon(() -> new ItemStack(NCMachines.BENDING_MACHINE.get(NCTiers.LV).getBlock().get()))
			.title(Component.translatable("itemGroup.conductance.machines"))
			.build()
	).register();

	public static final RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_ITEMS = REGISTRATE.defaultCreativeTab("material_items", builder -> builder
			.displayItems(new TabDisplayGen("material_items"))
			.icon(() -> CAPI.materials().getItem(NCMaterialTaggedSets.INGOT, NCMaterials.ALUMINIUM, 1))
			.title(Component.translatable("itemGroup.conductance.material_items"))
			.build()
	).register();
	public static final RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_BLOCKS = REGISTRATE.defaultCreativeTab("material_blocks", builder -> builder
			.displayItems(new TabDisplayGen("material_blocks"))
			.icon(() -> CAPI.materials().getItem(NCMaterialTaggedSets.STORAGE_BLOCK, NCMaterials.ALUMINIUM, 1))
			.title(Component.translatable("itemGroup.conductance.material_blocks"))
			.build()
	).register();
	public static final RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_FLUIDS = REGISTRATE.defaultCreativeTab("material_fluids", builder -> builder
			.displayItems(new TabDisplayGen("material_fluids"))
			.icon(() -> CAPI.materials().getBucketUnsafe(NCMaterialTaggedSets.LIQUID, NCMaterials.ALUMINIUM).getDefaultInstance())
			.title(Component.translatable("itemGroup.conductance.material_fluids"))
			.build()
	).register();
	public static final RegistryEntry<CreativeModeTab, CreativeModeTab> PIPELIKE = REGISTRATE.defaultCreativeTab("pipelike", builder -> builder
			.displayItems(new TabDisplayGen("pipelike"))
			.icon(() -> WireRegistry.getWire(WireType.WIRE_12X, NCMaterials.ALUMINIUM).asStack())
			.title(Component.translatable("itemGroup.conductance.pipelike"))
			.build()
	).register();

	public static final RegistryEntry<CreativeModeTab, CreativeModeTab> DECORATION = REGISTRATE.defaultCreativeTab("decoration", builder -> builder
			.displayItems(new TabDisplayGen("decoration"))
			.icon(() -> NCDecoration.LUX.get(DyeColor.CYAN).asStack())
			.title(Component.translatable("itemGroup.conductance.decoration"))
			.build()
	).register();
	//@formatter:on

	public static void init() {
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

	private ConductanceCreativeTabs() {
	}
}
