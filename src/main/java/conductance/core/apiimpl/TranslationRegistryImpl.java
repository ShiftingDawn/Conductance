package conductance.core.apiimpl;

import java.util.HashMap;
import java.util.function.Supplier;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import conductance.api.CAPI;
import conductance.api.material.Material;
import conductance.api.material.MaterialOreType;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.registry.TranslationRegistry;
import conductance.api.util.TextHelper;
import conductance.Conductance;
import conductance.Config;

public final class TranslationRegistryImpl implements TranslationRegistry {

	public static final TranslationRegistryImpl INSTANCE = new TranslationRegistryImpl();
	private final HashMap<String, String> cache = new HashMap<>();
	private final HashMap<String, MutableComponent> componentCache = new HashMap<>();

	@Override
	public String translate(final String key, final Supplier<String> fallback, final Object... format) {
		return this.cache.computeIfAbsent(key, k -> {
			if (Config.debug_translationRegistryDebugLogging.get() > 0) {
				Conductance.LOGGER.info("[TRANSLATIONS]Processing un-cached translation key: {}", key);
			}
			if (CAPI.isClient() && I18n.exists(key)) {
				final String result = TranslationRegistryImpl.format(key, format);
				if (Config.debug_translationRegistryDebugLogging.get() > 0) {
					Conductance.LOGGER.info("[TRANSLATIONS]\tFound custom translation for key: {}", key);
					Conductance.LOGGER.info("[TRANSLATIONS]\t\tResult: {}", result);
				}
				return result;
			} else {
				final String result = TranslationRegistryImpl.format(fallback.get(), format);
				if (Config.debug_translationRegistryDebugLogging.get() == 2) {
					Conductance.LOGGER.info("[TRANSLATIONS]\tFalling back to generated translation for key: {}", key);
					Conductance.LOGGER.info("[TRANSLATIONS]\t\tResult: {}", result);
				}
				return result;
			}
		});
	}

	private static String format(final String key, final Object... format) {
		if (!CAPI.isClient()) {
			return String.format(key, format);
		} else {
			return I18n.get(key, format);
		}
	}

	@Override
	public MutableComponent makeLocalizedName(final String key, final Supplier<String> fallback, final Object... format) {
		return this.componentCache.computeIfAbsent(key, k -> Component.literal(this.translate(key, fallback, format)));
	}

	@Override
	public MutableComponent makeLocalizedName(final Block block) {
		return this.makeLocalizedName(block.getDescriptionId(), () -> TextHelper.lowerUnderscoreToEnglish(BuiltInRegistries.BLOCK.getKey(block).getPath()));
	}

	@Override
	public MutableComponent makeLocalizedName(final Block block, final Supplier<MutableComponent> override) {
		return this.componentCache.computeIfAbsent(block.getDescriptionId(), k -> override.get());
	}

	@Override
	public MutableComponent makeLocalizedName(final Item item) {
		return this.makeLocalizedName(item.getDescriptionId(), () -> TextHelper.lowerUnderscoreToEnglish(BuiltInRegistries.ITEM.getKey(item).getPath()));
	}

	@Override
	public MutableComponent makeLocalizedName(final Item item, final Supplier<MutableComponent> override) {
		return this.componentCache.computeIfAbsent(item.getDescriptionId(), k -> override.get());
	}

	@Override
	public MutableComponent makeLocalizedName(final String key, final TaggedMaterialSet taggedSet, final Material material) {
		return this.componentCache.computeIfAbsent(key, k -> {
			final String materialName = this.translate(material.getUnlocalizedName(), () -> TextHelper.lowerUnderscoreToEnglish(material.getName()));
			final String translation = this.translate(key, () -> TextHelper.lowerUnderscoreToEnglish(taggedSet.getUnlocalizedNameFactory().apply(material)), materialName);
			return Component.literal(translation);
		});
	}

	@Override
	public MutableComponent makeLocalizedName(final String key, final MaterialOreType oreType, final Material material) {
		return this.componentCache.computeIfAbsent(key, k -> {
			final String materialName = this.translate(material.getUnlocalizedName(), () -> TextHelper.lowerUnderscoreToEnglish(material.getName()));
			final String translation = this.translate(key, () -> TextHelper.lowerUnderscoreToEnglish(oreType.getUnlocalizedNameFactory()), materialName);
			return Component.literal(translation);
		});
	}

	public void reset() {
		this.cache.clear();
		this.componentCache.clear();
	}
}
