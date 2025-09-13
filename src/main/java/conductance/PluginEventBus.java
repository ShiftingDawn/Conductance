package conductance;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.objectweb.asm.Type;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.plugin.IConductancePluginEvent;

@SuppressWarnings("unchecked")
final class PluginEventBus {

	public static final Map<Integer, List<EventMethod>> LISTENERS = Collections.synchronizedSortedMap(new TreeMap<>());
	private static final AtomicBoolean INITIALIZED = new AtomicBoolean(false);

	public record EventMethod(String modid, Class<IConductancePluginEvent> eventType, Consumer<IConductancePluginEvent> listener) {

	}

	public static void initialize() {
		if (PluginEventBus.INITIALIZED.getAndSet(true)) {
			throw new IllegalStateException("Cannot initialize %s twice!".formatted(PluginEventBus.class.getSimpleName()));
		}
		final Type annotationType = Type.getType(ConductancePluginListener.class);
		for (final ModFileScanData scanData : ModList.get().getAllScanData()) {
			for (final ModFileScanData.AnnotationData annotationData : scanData.getAnnotations()) {
				if (Objects.equals(annotationType, annotationData.annotationType())) {
					try {
						final Class<?> cls = Class.forName(annotationData.memberName());
						final ConductancePluginListener annotation = cls.getAnnotation(ConductancePluginListener.class);
						PluginEventBus.registerClass(cls, annotation);
						Conductance.LOGGER.debug("Registered PluginListener {}", annotationData.memberName());
					} catch (final ClassNotFoundException e) {
						Conductance.LOGGER.error("Could not register PluginListener {}", annotationData.memberName(), e);
					}
				}
			}
		}
	}

	private static void registerClass(final Class<?> clazz, final ConductancePluginListener info) {
		for (final Method method : clazz.getDeclaredMethods()) {
			if (method.isAnnotationPresent(EventListener.class)) {
				if (!Modifier.isStatic(method.getModifiers())) {
					throw new IllegalArgumentException("Plugin listener method %s must be static.".formatted(method));
				}
				method.setAccessible(true);
				if (method.getParameterCount() != 1 || !IConductancePluginEvent.class.isAssignableFrom(method.getParameterTypes()[0])) {
					throw new IllegalArgumentException("Plugin listener method %s must have exactly 1 parameter of type %s.".formatted(method, IConductancePluginEvent.class.getName()));
				}
				final EventMethod wrapper = new EventMethod(info.modid(), (Class<IConductancePluginEvent>) method.getParameterTypes()[0], event -> {
					try {
						method.invoke(null, event);
					} catch (final IllegalAccessException e) {
						throw new RuntimeException(e);
					} catch (final InvocationTargetException e) {
						Conductance.LOGGER.error("An error occurred during the invocation of plugin event listener {} owned by {}", method, info.modid(), e.getCause());
						throw new RuntimeException(e.getCause());
					}
				});
				final EventListener listenerInfo = method.getAnnotation(EventListener.class);
				PluginEventBus.LISTENERS.computeIfAbsent(listenerInfo.priority(), k -> Collections.synchronizedList(new ArrayList<>())).add(wrapper);
			}
		}
	}

	private PluginEventBus() {
	}
}
