package conductance.lib.pack.client;

import conductance.api.resource.event.AddTranslationEvent;
import conductance.Conductance;

public final class RuntimeResourcePackBridge {

	//Called from LanguageManagerMixin, making sure our translations get loaded and frozen before the language manager reloads
	public static void loadTranslations() {
		final long startTime = System.currentTimeMillis();
		RuntimeResourcePack.resetTranslations();
		Conductance.dispatchAll(AddTranslationEvent.class, new AddTranslationEventImpl(RuntimeResourcePack::addTranslation));
		RuntimeResourcePack.freezeTranslations();
		Conductance.LOGGER.info("Conductance loaded RuntimeResourcePack translations in {}ms", System.currentTimeMillis() - startTime);
	}

	private RuntimeResourcePackBridge() {
	}
}
