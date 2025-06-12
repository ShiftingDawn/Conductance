package conductance;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;
import conductance.api.CAPI;
import conductance.api.plugin.IConductancePluginEvent;
import conductance.client.ClientProxy;
import conductance.core.CommonProxy;

@Mod(value = Conductance.MODID)
public final class Conductance {

	public static final String MODID = CAPI.MOD_ID;
	public static final Logger LOGGER = LogUtils.getLogger();

	public Conductance(final IEventBus modEventBus, final ModContainer modContainer) {
		Conductance.LOGGER.info("Conductance is initializing on platform: NeoForge");
		this.modLoad(modEventBus, modContainer);
	}

	private void modLoad(final IEventBus modEventBus, final ModContainer modContainer) {
		modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

		PluginEventBus.initialize();
		CommonProxy.init(modEventBus);
		if (CAPI.isClient()) {
			ClientProxy.init(modEventBus);
			modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		}
	}

	public static ResourceLocation id(final String path) {
		return ResourceLocation.fromNamespaceAndPath(Conductance.MODID, path);
	}

	public static String tooltipText(final String suffix) {
		return "tooltip.%s.%s".formatted(Conductance.MODID, suffix);
	}

	public static Component tooltip(final String suffix, final Object... args) {
		return Component.translatable(Conductance.tooltipText(suffix), args);
	}

	public static <T> void setApiValue(final Class<T> variableType, final T value) {
		final Field field = Arrays.stream(CAPI.class.getDeclaredFields()).filter(f -> variableType.isAssignableFrom(f.getType()) && !Modifier.isFinal(f.getModifiers())).findFirst().orElseThrow();
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
		Conductance.dispatch(eventClass, modid -> event);
	}
}
