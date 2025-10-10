package conductance.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import conductance.Conductance;
import conductance.core.CreativeTabHelper;

public final class ConductanceCreativeTabs {

	private static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, Conductance.MODID);

	public static void initialize(final IEventBus modEventBus) {
		ConductanceCreativeTabs.REGISTRY.register(modEventBus);
		for (final CreativeTabHelper.Tabs tabType : CreativeTabHelper.Tabs.values()) {
			ConductanceCreativeTabs.REGISTRY.register(tabType.getName(), () -> CreativeModeTab.builder()
				.title(Component.translatable("itemGroup.%s.%s".formatted(Conductance.MODID, tabType.getName())))
				.displayItems(new TabDisplayGen(tabType))
				.icon(tabType.getIcon())
				.build()
			);
		}
	}

	private record TabDisplayGen(CreativeTabHelper.Tabs tabType) implements CreativeModeTab.DisplayItemsGenerator {
		@Override
		public void accept(final CreativeModeTab.ItemDisplayParameters itemDisplayParameters, final CreativeModeTab.Output output) {
			//TODO fillItemCategory ported from old mc
			for (final CreativeTabHelper.TabSection section : CreativeTabHelper.TabSection.values()) {
				CreativeTabHelper.getTabContent(this.tabType, section).forEach(entry -> {
					entry.ifLeft(output::accept);
					entry.ifRight(output::accept);
				});
			}
		}
	}

	private ConductanceCreativeTabs() {
	}
}
