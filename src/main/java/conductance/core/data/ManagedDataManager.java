package conductance.core.data;

import java.lang.annotation.ElementType;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;
import conductance.api.machine.data.IManaged;
import conductance.api.machine.data.Managed;
import conductance.api.machine.data.ManagedDataMap;
import conductance.api.machine.data.handler.InstancedField;
import conductance.api.machine.data.serializer.DataSerializer;
import conductance.api.registry.ManagedDataRegistry;
import conductance.Conductance;
import conductance.core.apiimpl.PluginManager;

public final class ManagedDataManager {

	private static final Map<Class<?>, ManagedClassWrapper> WRAPPERS = new IdentityHashMap<>();
	public static final ManagedDataRegistry MANAGED_DATA_REGISTRY;

	public static void init() {
		PluginManager.dispatchRegisterManagedFieldValueHandlers();
		ManagedFieldValueHandlerRegistry.INSTANCE.freeze();

		final Class<?>[] classes = ManagedDataManager.findClasses();
		for (final Class<?> clazz : classes) {
			ManagedDataManager.WRAPPERS.put(clazz, new ManagedClassWrapper(clazz, ManagedDataManager.findNearestWrapper(clazz)));
			Conductance.LOGGER.debug("Registered managed class " + clazz.getName());
		}
	}

	public static ManagedDataMap requestManagedDataMap(final IManaged managed) {
		return new ManagedDataMapImpl(
				managed.getClass(),
				Objects.requireNonNull(ManagedDataManager.findNearestWrapper(managed.getClass()), "Class %s is missing the %s annotation".formatted(managed.getClass().getName(), Managed.class.getName())),
				managed
		);
	}

	@Nullable
	private static ManagedClassWrapper findNearestWrapper(final Class<?> clazz) {
		Class<?> currentClass = clazz;
		while (currentClass != null && currentClass != Object.class) {
			if (ManagedDataManager.WRAPPERS.containsKey(currentClass)) {
				return ManagedDataManager.WRAPPERS.get(currentClass);
			}
			currentClass = currentClass.getSuperclass();
		}
		return null;
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

	static {
		MANAGED_DATA_REGISTRY = new ManagedDataRegistry() {
			@Override
			public ManagedDataMap requestDataMap(final IManaged managed) {
				return ManagedDataManager.requestManagedDataMap(managed);
			}

			@Override
			public int getSerializerId(final DataSerializer<?> serializer) {
				return ManagedFieldValueHandlerRegistry.INSTANCE.getSerializerId(serializer);
			}

			@Override
			public DataSerializer<?> makeSerializer(final int serializerId) {
				return ManagedFieldValueHandlerRegistry.INSTANCE.createSerializer(serializerId);
			}

			@Override
			public <T> InstancedField getInstancedField(final T data) {
				return new DummyInstancedField(data);
			}
		};
	}
}
