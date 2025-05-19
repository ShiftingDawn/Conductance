package conductance.core.apiimpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Function;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.plugin.IConductancePluginEvent;

@SuppressWarnings("unchecked")
public final class PluginEventBus {

	private static final Map<Integer, List<EventMethod>> LISTENERS = Collections.synchronizedSortedMap(new TreeMap<>());

	private record EventMethod(String modid, Class<IConductancePluginEvent> eventType, Consumer<IConductancePluginEvent> listener) {

	}

	static void registerClass(final Class<?> clazz, final ConductancePluginListener info) {
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
					} catch (final IllegalAccessException | InvocationTargetException e) {
						throw new RuntimeException(e);
					}
				});
				final EventListener listenerInfo = method.getAnnotation(EventListener.class);
				PluginEventBus.LISTENERS.computeIfAbsent(listenerInfo.priority(), k -> Collections.synchronizedList(new ArrayList<>())).add(wrapper);
			}
		}
	}

	public static <T extends IConductancePluginEvent> void post(final Class<T> eventClass, final Function<String, T> eventFactory) {
		PluginEventBus.LISTENERS.values().forEach(listeners -> {
			for (final EventMethod listener : listeners) {
				if (listener.eventType.isAssignableFrom(eventClass)) {
					listener.listener.accept(eventFactory.apply(listener.modid));
				}
			}
		});
	}

	public static <T extends IConductancePluginEvent> void postAll(final Class<T> eventClass, final T event) {
		PluginEventBus.post(eventClass, modid -> event);
	}

	static <T extends IConductancePluginEvent> T instantiateEvent(final Class<T> clazz, final Object... args) {
		try {
			final Constructor<T> constructor = (Constructor<T>) clazz.getDeclaredConstructors()[0];
			constructor.setAccessible(true);
			return constructor.newInstance(args);
		} catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	private PluginEventBus() {
	}
}
