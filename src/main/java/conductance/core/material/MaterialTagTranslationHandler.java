package conductance.core.material;

import net.minecraft.resources.ResourceLocation;
import conductance.api.CAPI;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddTranslationEvent;
import conductance.Conductance;

@ConductancePluginListener(modid = Conductance.MODID)
final class MaterialTagTranslationHandler {

	@EventListener(priority = -100)
	private static void onAddTranslations(final AddTranslationEvent event) {
		CAPI.regs().materialTaggedSets().forEach(set -> CAPI.regs().materials().forEach(material -> set.streamAllItemTagData(material).forEach(tag -> {
			if (!set.hasItems() || !set.canGenerateItem(material)) {
				return;
			}
			final ResourceLocation location = tag.getA().location();
			final String key = "tag.item.%s.%s".formatted(location.getNamespace(), location.getPath().replace('/', '.'));
			final String str = CAPI.translations().makeLocalizedName(material.getDescriptionId(), set, material).getString();
			event.add(key, String.format(tag.getB().apply(material), str));
		})));
	}

	private MaterialTagTranslationHandler() {
	}
}
