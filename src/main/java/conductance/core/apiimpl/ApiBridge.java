package conductance.core.apiimpl;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import conductance.api.machine.recipe.RecipeHelper;
import conductance.api.machine.sync.SyncHelper;
import conductance.api.resource.ResourceFinder;
import conductance.api.registry.RegistryProvider;
import conductance.api.registry.TranslationRegistry;
import conductance.Conductance;
import conductance.core.pipenet.WireRegistry;
import conductance.core.recipe.RecipeHelperImpl;
import conductance.core.register.ConductanceRegistrate;
import conductance.core.sync.SyncFieldSerializerRegisterImpl;
import conductance.core.sync.SyncHelperImpl;

@SuppressWarnings("NotNullFieldNotInitialized")
@EventBusSubscriber(modid = Conductance.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class ApiBridge {

	public enum DataPackRegistryLoadStage {
		UNFREEZE, RESET, REFREEZE
	}

	public static final ConductanceRegistryImpl<ResourceLocation, ConductanceRegistryImpl<?, ?>> REGISTRIES = new ConductanceRegistryImpl.ResourceKeyed<>(Conductance.id("root"));
	private static RegistryProviderImpl regs;
	private static ConductanceRegistrate registrate;

	public static void init(final IEventBus modEventBus) {
		ApiBridge.regs = new RegistryProviderImpl(modEventBus);
		ApiBridge.registrate = ConductanceRegistrate.create(modEventBus);
		Conductance.setApiValue(RegistryProvider.class, ApiBridge.regs);
		Conductance.setApiValue(ResourceFinder.class, new ResourceFinderImpl());
		Conductance.setApiValue(TranslationRegistry.class, TranslationRegistryImpl.INSTANCE);
		Conductance.setApiValue(RecipeHelper.class, RecipeHelperImpl.INSTANCE);
		Conductance.setApiValue(SyncHelper.class, SyncHelperImpl.INSTANCE);
	}

	@SubscribeEvent
	private static void onLoadComplete(final FMLLoadCompleteEvent ignored) {
		ApiBridge.REGISTRIES.freeze();
		ApiBridge.REGISTRIES.values().forEach(ConductanceRegistryImpl::freeze);

		WireRegistry.freeze();
		SyncFieldSerializerRegisterImpl.INSTANCE.freeze();
	}

	public static void handleDataPackRegistryStage(final DataPackRegistryLoadStage stage) {
		switch (stage) {
			case UNFREEZE -> ApiBridge.REGISTRIES.values().forEach(reg -> {
				if (reg instanceof final ConductanceDataPackRegistry<?> dataPackRegistry) {
					dataPackRegistry.unfreeze();
				}
			});
			case RESET -> ApiBridge.REGISTRIES.values().forEach(reg -> {
				if (reg instanceof final ConductanceDataPackRegistry<?> dataPackRegistry) {
					dataPackRegistry.reset();
				}
			});
			case REFREEZE -> ApiBridge.REGISTRIES.values().forEach(reg -> {
				if (reg instanceof final ConductanceDataPackRegistry<?> dataPackRegistry) {
					dataPackRegistry.freeze();
				}
			});
		}
	}

	public static ConductanceRegistrate getRegistrate() {
		return ApiBridge.registrate;
	}

	public static RegistryProviderImpl getRegs() {
		return ApiBridge.regs;
	}

	private ApiBridge() {
	}
}
