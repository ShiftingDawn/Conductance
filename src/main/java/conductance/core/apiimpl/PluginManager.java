package conductance.core.apiimpl;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;
import org.objectweb.asm.Type;
import conductance.api.CAPI;
import conductance.api.ConductancePlugin;
import conductance.api.IConductancePlugin;
import conductance.api.material.PeriodicElement;
import conductance.Conductance;

public class PluginManager {

	protected static final HashMap<IConductancePlugin, String> PLUGINS = new HashMap<>();
	private static IConductancePlugin ROOT_PLUGIN;

	public static void init() {
		PluginManager.findPlugins();
		for (final Map.Entry<IConductancePlugin, String> entry : PluginManager.PLUGINS.entrySet()) {
			if (entry.getValue().equals(Conductance.MODID)) {
				PluginManager.ROOT_PLUGIN = entry.getKey();
				break;
			}
		}
		if (PluginManager.ROOT_PLUGIN == null) {
			throw new IllegalStateException(
					"Could not find " + CAPI.MOD_ID + " root plugin! Something is seriously wrong!");
		}
		PluginManager.PLUGINS.remove(PluginManager.ROOT_PLUGIN);
	}

	private static void findPlugins() {
		final HashSet<String> pluginClasses = new HashSet<>();
		ModList.get().getAllScanData().forEach(scanData -> scanData.getAnnotations().stream()
				.filter(annotationData -> Objects.equals(annotationData.annotationType(),
						Type.getType(ConductancePlugin.class)))
				.forEach(annotationData -> pluginClasses.add(annotationData.memberName())));
		for (final String className : pluginClasses) {
			try {
				final Class<?> clazz = Class.forName(className);
				final Class<? extends IConductancePlugin> clazz2 = clazz.asSubclass(IConductancePlugin.class);
				final Constructor<? extends IConductancePlugin> constructor = clazz2.getDeclaredConstructor();
				final IConductancePlugin instance = constructor.newInstance();
				PluginManager.PLUGINS.put(instance, clazz2.getAnnotation(ConductancePlugin.class).modid());
			} catch (final ReflectiveOperationException | LinkageError e) {
				Conductance.LOGGER.error("Could not register Plugin {}", className, e);
			}
		}
	}

	public static void dispatchPeriodicElements() {
		PluginManager.execute((plugin, modid) -> {
			plugin.registerPeriodicElements((protons, neutrons, registryName, name, symbol, parent) -> {
				final PeriodicElement result = new PeriodicElement(ResourceLocation.fromNamespaceAndPath(modid, registryName), protons, neutrons, name, symbol, parent != null ? parent.getRegistryKey() : null);
				CAPI.REGS.periodicElements().register(result.getRegistryKey(), result);
				return result;
			});
		});
		// TODO KubeJS
	}

	private static void execute(final BiConsumer<IConductancePlugin, String> executor) {
		executor.accept(PluginManager.ROOT_PLUGIN, Conductance.MODID);
		PluginManager.PLUGINS.forEach(executor);
	}
}
