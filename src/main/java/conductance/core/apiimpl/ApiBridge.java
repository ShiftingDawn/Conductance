package conductance.core.apiimpl;

import java.lang.reflect.Field;
import java.util.Arrays;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import conductance.api.CAPI;
import conductance.api.material.ResourceFinder;
import conductance.api.plugin.ManagedDataMapRequester;
import conductance.api.registry.RegistryProvider;
import conductance.api.registry.TaggedSetRegistry;
import conductance.api.registry.TranslationRegistry;
import conductance.Conductance;
import conductance.core.register.ConductanceRegistrate;
import conductance.core.data.ManagedDataManager;
import conductance.core.register.MaterialRegistry;

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
		ApiBridge.setApiValue(RegistryProvider.class, ApiBridge.regs);
		ApiBridge.setApiValue(ResourceFinder.class, new ResourceFinderImpl());
		ApiBridge.setApiValue(TaggedSetRegistry.class, MaterialRegistry.INSTANCE);
		ApiBridge.setApiValue(TranslationRegistry.class, TranslationRegistryImpl.INSTANCE);
		ApiBridge.setApiValue(ManagedDataMapRequester.class, ManagedDataManager::requestManagedDataMap);
	}

	private static <T> void setApiValue(final Class<T> variableType, final T value) {
		final Field field = Arrays.stream(CAPI.class.getDeclaredFields()).filter(f -> variableType.isAssignableFrom(f.getType())).findFirst().orElseThrow();
		try {
			field.setAccessible(true);
			field.set(null, value);
			field.setAccessible(false);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Cannot set API value", e);
		}
	}

	@SubscribeEvent
	private static void onLoadComplete(final FMLLoadCompleteEvent ignored) {
		ApiBridge.REGISTRIES.freeze();
		ApiBridge.REGISTRIES.values().forEach(ConductanceRegistryImpl::freeze);

		MaterialRegistry.INSTANCE.freeze();
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
