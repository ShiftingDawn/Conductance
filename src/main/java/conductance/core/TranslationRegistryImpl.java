package conductance.core;

import java.util.HashMap;
import java.util.function.Supplier;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import conductance.api.CAPI;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.registry.TranslationRegistry;
import conductance.api.util.TextHelper;

public final class TranslationRegistryImpl implements TranslationRegistry {

	public static final TranslationRegistryImpl INSTANCE = new TranslationRegistryImpl();
	private final HashMap<String, Supplier<MutableComponent>> interceptors = new HashMap<>();
	private final HashMap<String, String> cache = new HashMap<>();
	private final HashMap<String, MutableComponent> componentCache = new HashMap<>();

	@Override
	public String translate(final String key, final Supplier<String> fallback, final Object... format) {
		return this.cache.computeIfAbsent(key, k -> {
			if (CAPI.isClient() && I18n.exists(key)) {
				return TranslationRegistryImpl.format(key, format);
			} else {
				return TranslationRegistryImpl.format(fallback.get(), format);
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
	public void addInterceptor(final String key, final Supplier<MutableComponent> interceptor) {
		this.interceptors.put(key, interceptor);
	}

	private MutableComponent handle(final String key, final Supplier<MutableComponent> fallback) {
		return this.componentCache.computeIfAbsent(key, k -> {
			if (this.interceptors.containsKey(key)) {
				return this.interceptors.get(key).get();
			}
			return fallback.get();
		});
	}

	@Override
	public MutableComponent makeLocalizedName(final String key, final Supplier<String> fallback, final Object... format) {
		return this.handle(key, () -> Component.literal(this.translate(key, fallback, format)));
	}

	@Override
	public MutableComponent makeLocalizedName(final Block block) {
		return this.makeLocalizedName(block.getDescriptionId(), () -> TextHelper.lowerUnderscoreToEnglish(BuiltInRegistries.BLOCK.getKey(block).getPath()));
	}

	@Override
	public MutableComponent makeLocalizedName(final Block block, final Supplier<MutableComponent> override) {
		return this.handle(block.getDescriptionId(), override);
	}

	@Override
	public MutableComponent makeLocalizedName(final Item item) {
		return this.makeLocalizedName(item.getDescriptionId(), () -> TextHelper.lowerUnderscoreToEnglish(BuiltInRegistries.ITEM.getKey(item).getPath()));
	}

	@Override
	public MutableComponent makeLocalizedName(final Item item, final Supplier<MutableComponent> override) {
		return this.handle(item.getDescriptionId(), override);
	}

	@Override
	public MutableComponent makeLocalizedName(final FluidType fluid) {
		return this.makeLocalizedName(fluid.getDescriptionId(), () -> TextHelper.lowerUnderscoreToEnglish(NeoForgeRegistries.FLUID_TYPES.getKey(fluid).getPath()));
	}

	@Override
	public MutableComponent makeLocalizedName(final FluidType fluid, final Supplier<MutableComponent> override) {
		return this.handle(fluid.getDescriptionId(), override);
	}

	@Override
	public MutableComponent makeLocalizedName(final String key, final TaggedMaterialSet taggedSet, final Material material) {
		return this.handle(key, () -> {
			final String materialName = this.translate(material.getDescriptionId(), () -> TextHelper.lowerUnderscoreToEnglish(material.getName()));
			final String translation = this.translate(key, () -> TextHelper.lowerUnderscoreToEnglish(taggedSet.getUnlocalizedNameFactory().apply(material)), materialName);
			return Component.literal(translation);
		});
	}

	public void reset() {
		this.cache.clear();
		this.componentCache.clear();
	}
}
