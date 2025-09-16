package conductance;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;
import conductance.api.CAPI;
import conductance.api.plugin.IConductancePluginEvent;
import conductance.api.registry.RegistryProvider;
import conductance.api.resource.ResourceFinder;
import conductance.core.material.MaterialCore;
import conductance.core.material.MaterialRegistryImpl;
import conductance.core.periodicelement.PeriodicElementCore;
import conductance.init.ConductanceBlocks;
import conductance.init.ConductanceCreativeTabs;
import conductance.init.ConductanceItems;
import conductance.lib.ResourceFinderImpl;
import conductance.lib.registry.RegistryProviderImpl;

@Mod(value = Conductance.MODID)
@SuppressWarnings("NotNullFieldNotInitialized")
public final class Conductance {

	public static final String MODID = "conductance";
	public static final Logger LOGGER = LogUtils.getLogger();
	public static RegistryProviderImpl REGISTRIES;
	public static MaterialRegistryImpl MATERIALS;
	private static final AtomicBoolean HAS_REGISTERED = new AtomicBoolean(false);
	private static IEventBus MODBUS;

	public Conductance(final IEventBus modEventBus, final ModContainer modContainer) {
		Conductance.LOGGER.info("Conductance is initializing on platform: NeoForge");
		Conductance.MODBUS = modEventBus;
		modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, ModConfig.SPEC);
		modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

		PluginEventBus.initialize();

		Conductance.REGISTRIES = Util.make(new RegistryProviderImpl(modEventBus), regs -> Conductance.setApiValue(RegistryProvider.class, regs));
		Conductance.setApiValue(ResourceFinder.class, new ResourceFinderImpl());

		modEventBus.addListener(RegisterEvent.class, this::onRegister);

		ConductanceCreativeTabs.initialize(modEventBus);
	}

	private void onRegister(final RegisterEvent event) {
		if (Conductance.HAS_REGISTERED.getAndSet(true)) {
			return;
		}
		PeriodicElementCore.initialize();
		MaterialCore.initialize(Conductance.MODBUS);

		ConductanceBlocks.initialize();

		ConductanceItems.initialize(Conductance.MODBUS);
	}

	public static ResourceLocation id(final String path) {
		if (path.contains(":")) {
			return ResourceLocation.parse(path);
		} else {
			return ResourceLocation.fromNamespaceAndPath(Conductance.MODID, path);
		}
	}

	public static <T> void setApiValue(final Class<T> variableType, final T value) {
		final Field field = Arrays.stream(CAPI.class.getDeclaredFields())
				.filter(f -> variableType.isAssignableFrom(f.getType()) && !Modifier.isFinal(f.getModifiers()))
				.findFirst().orElseThrow();
		try {
			field.setAccessible(true);
			field.set(null, value);
			field.setAccessible(false);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Cannot set API value", e);
		}
	}

	public static Set<String> getKnownPluginNamespaces() {
		return PluginEventBus.LISTENERS.values().stream().flatMap(Collection::stream).map(PluginEventBus.EventMethod::modid).collect(Collectors.toSet());
	}

	public static <T extends IConductancePluginEvent> void dispatch(final Class<T> eventClass, final Function<String, T> eventFactory) {
		PluginEventBus.LISTENERS.values().forEach(listeners -> {
			for (final PluginEventBus.EventMethod listener : listeners) {
				if (listener.eventType().isAssignableFrom(eventClass)) {
					listener.listener().accept(eventFactory.apply(listener.modid()));
				}
			}
		});
	}

	public static <T extends IConductancePluginEvent> void dispatchAll(final Class<T> eventClass, final T event) {
		Conductance.dispatch(eventClass, ignored -> event);
	}
}
