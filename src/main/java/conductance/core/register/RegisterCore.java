package conductance.core.register;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.jetbrains.annotations.NotNull;
import conductance.api.registry.RegistryProvider;
import conductance.api.resource.ResourceFinder;
import conductance.Conductance;

public final class RegisterCore {

	static final ConductanceRegistryImpl<ResourceLocation, ConductanceRegistryImpl<?, ?>> REGISTRIES = new ConductanceRegistryImpl.ResourceKeyed<>(Conductance.id("root"));
	private static RegistryProviderImpl regs;
	private static ConductanceRegistrate registrate;

	public static void initialize(final IEventBus modEventBus) {
		RegisterCore.regs = new RegistryProviderImpl(modEventBus);
		RegisterCore.registrate = ConductanceRegistrate.create(modEventBus);

		modEventBus.addListener(FMLLoadCompleteEvent.class, ignored -> {
			RegisterCore.REGISTRIES.freeze();
			RegisterCore.REGISTRIES.forEach(ConductanceRegistryImpl::freeze);
		});

		Conductance.setApiValue(RegistryProvider.class, RegisterCore.regs);
		Conductance.setApiValue(ResourceFinder.class, new ResourceFinderImpl());
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

	@NotNull
	public static ConductanceRegistrate getRegistrate() {
		return RegisterCore.registrate;
	}

	@NotNull
	public static RegistryProviderImpl getRegs() {
		return RegisterCore.regs;
	}

	private RegisterCore() {
	}
}
