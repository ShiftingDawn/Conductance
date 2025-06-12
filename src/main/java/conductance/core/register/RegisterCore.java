package conductance.core.register;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import conductance.api.registry.RegistryProvider;
import conductance.api.registry.TranslationRegistry;
import conductance.api.resource.ResourceFinder;
import conductance.Conductance;
import conductance.core.TranslationRegistryImpl;

public final class RegisterCore {

	static final ConductanceRegistryImpl<ResourceLocation, ConductanceRegistryImpl<?, ?>> REGISTRIES = new ConductanceRegistryImpl.ResourceKeyed<>(Conductance.id("root"));
	public static final RegistryProviderImpl REGS = new RegistryProviderImpl();
	public static final ConductanceRegistrate REGISTRATE = ConductanceRegistrate.create();

	public static void initialize(final IEventBus modEventBus) {
		RegisterCore.REGISTRATE.registerEventListeners(modEventBus);
		modEventBus.addListener(FMLLoadCompleteEvent.class, ignored -> {
			RegisterCore.REGISTRIES.freeze();
			RegisterCore.REGISTRIES.forEach(ConductanceRegistryImpl::freeze);
		});

		Conductance.setApiValue(RegistryProvider.class, RegisterCore.REGS);
		Conductance.setApiValue(ResourceFinder.class, new ResourceFinderImpl());
		Conductance.setApiValue(TranslationRegistry.class, TranslationRegistryImpl.INSTANCE);
	}

	public enum DataPackRegistryLoadStage {
		UNFREEZE, RESET, REFREEZE
	}

	public static void progressDataPackStage(final DataPackRegistryLoadStage stage) {
		RegisterCore.REGISTRIES.values().stream().filter(reg -> reg instanceof ConductanceDataPackRegistry).forEach(reg -> {
			final ConductanceDataPackRegistry<?> dataReg = (ConductanceDataPackRegistry<?>) reg;
			switch (stage) {
				case UNFREEZE -> dataReg.unfreeze();
				case RESET -> dataReg.reset();
				case REFREEZE -> dataReg.freeze();
			}
		});
	}

	private RegisterCore() {
	}
}
