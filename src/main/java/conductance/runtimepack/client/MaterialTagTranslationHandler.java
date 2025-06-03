package conductance.runtimepack.client;

import net.minecraft.resources.ResourceLocation;
import conductance.api.CAPI;

final class MaterialTagTranslationHandler {

	public static void reload() {
		CAPI.regs().materialTaggedSets().forEach(set -> CAPI.regs().materials().forEach(material -> set.streamAllItemTagData(material).forEach(tag -> {
			if (!set.hasItems() || !set.canGenerateItem(material)) {
				return;
			}
			final ResourceLocation location = tag.getA().location();
			final String key = "tag.item.%s.%s".formatted(location.getNamespace(), location.getPath().replace('/', '.'));
			final String str = CAPI.translations().makeLocalizedName(material.getDescriptionId(), set, material).getString();
			RuntimeResourcePack.addTranslation(key, String.format(tag.getB().apply(material), str));
		})));
	}

	private MaterialTagTranslationHandler() {
	}
}
