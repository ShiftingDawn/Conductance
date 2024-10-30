package conductance.core.data;

import java.lang.annotation.ElementType;
import java.util.IdentityHashMap;
import java.util.Map;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.objectweb.asm.Type;
import conductance.api.machine.data.Managed;
import conductance.Conductance;
import conductance.core.apiimpl.PluginManager;

public final class ManagedDataManager {

	private static final Map<Class<?>, ManagedClassWrapper> WRAPPERS = new IdentityHashMap<>();

	public static void init() {
		PluginManager.dispatchRegisterManagedFieldValueHandlers();
		
		final Class<?>[] classes = ManagedDataManager.findClasses();
		for (final Class<?> clazz : classes) {
			ManagedDataManager.WRAPPERS.put(clazz, new ManagedClassWrapper(clazz));
			Conductance.LOGGER.debug("Registered managed class " + clazz.getName());
		}
	}

	private static Class<?>[] findClasses() {
		return ModList.get().getAllScanData().stream()
				.flatMap(scanData -> scanData.getAnnotatedBy(Managed.class, ElementType.TYPE))
				.map(ModFileScanData.AnnotationData::clazz)
				.map(Type::getClassName)
				.map(className -> {
					try {
						return Class.forName(className);
					} catch (final ClassNotFoundException e) {
						throw new RuntimeException(e);
					}
				})
				.toArray(Class<?>[]::new);
	}

	private ManagedDataManager() {
	}
}
