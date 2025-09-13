package conductance.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import conductance.api.CAPI;
import conductance.Conductance;
import conductance.core.CreativeTabHelper;

public final class ConductanceItems {

	private static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(Conductance.MODID);

	public static void initialize(final IEventBus modEventBus) {
		ConductanceItems.REGISTRY.register(modEventBus);
		CAPI.regs().materials().forEach(material -> CAPI.regs().materialFlags().forEach(flag -> {
			if (material.hasFlag(flag)) {
				final ResourceLocation materialKey = CAPI.regs().materials().getKey(material);
				final ResourceLocation flagKey = CAPI.regs().materialFlags().getKey(flag);
				ConductanceItems.REGISTRY.registerItem("%s_%s".formatted(materialKey.getPath(), flagKey.getPath()), props -> {
					final Item item = new Item(props);
					CreativeTabHelper.addToTab(item, CreativeTabHelper.Tabs.GENERAL);
					return item;
				});
			}
		}));
	}

	private ConductanceItems() {
	}
}
