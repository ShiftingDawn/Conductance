package conductance.core.apiimpl;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.objectweb.asm.Type;
import conductance.api.CAPI;
import conductance.api.ConductancePlugin;
import conductance.api.IConductancePlugin;
import conductance.api.material.IMaterialTrait;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialTextureSet;
import conductance.api.material.MaterialTextureType;
import conductance.api.material.MaterialTraitKey;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.MaterialTraitRegister;
import conductance.api.plugin.RegisterMaterialTextureSetEvent;
import conductance.api.plugin.RegisterMaterialTextureTypeEvent;
import conductance.Conductance;
import conductance.core.machine.MachineBuilderImpl;

//TODO add KubeJS event dispatches to plugin dispatches
public final class PluginManager {

	private static final Type ANNOTATION_TYPE = Type.getType(ConductancePluginListener.class);
	private static final HashMap<IConductancePlugin, String> PLUGINS = new HashMap<>();
	@SuppressWarnings("NotNullFieldNotInitialized")
	private static IConductancePlugin rootPlugin;

	public static void init() {
		PluginManager.findPlugins();
		for (final Map.Entry<IConductancePlugin, String> entry : PluginManager.PLUGINS.entrySet()) {
			if (entry.getValue().equals(Conductance.MODID)) {
				PluginManager.rootPlugin = entry.getKey();
				break;
			}
		}
		if (PluginManager.rootPlugin == null) {
			throw new IllegalStateException("Could not find " + CAPI.MOD_ID + " root plugin! Something is seriously wrong!");
		}
		PluginManager.PLUGINS.remove(PluginManager.rootPlugin);
	}

	private static void findPlugins() {
		for (final ModFileScanData scanData : ModList.get().getAllScanData()) {
			for (final ModFileScanData.AnnotationData annotationData : scanData.getAnnotations()) {
				if (Objects.equals(PluginManager.ANNOTATION_TYPE, annotationData.annotationType())) {
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
		final HashSet<String> pluginClasses = new HashSet<>();
		ModList.get().getAllScanData().forEach(scanData -> scanData.getAnnotations().stream().filter(annotationData -> Objects.equals(annotationData.annotationType(), Type.getType(ConductancePlugin.class)))
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

	public static void dispatchMaterialTextureTypes() {
		PluginEventBus.post(RegisterMaterialTextureTypeEvent.class, modid -> {
			final Function<String, MaterialTextureType> register = registryName -> Util.make(
					new MaterialTextureType(ResourceLocation.fromNamespaceAndPath(modid, registryName)),
					result -> CAPI.regs().materialTextureTypes().register(result.getRegistryKey(), result));
			return PluginEventBus.instantiateEvent(RegisterMaterialTextureTypeEvent.class, register);
		});
	}

	public static void dispatchMaterialTextureSets() {
		PluginEventBus.post(RegisterMaterialTextureSetEvent.class, modid -> {
			final RegisterMaterialTextureSetEvent.MaterialTextureSetRegister register = (registryName, parentSetName) -> Util.make(
					new MaterialTextureSet(registryName, parentSetName),
					result -> CAPI.regs().materialTextureSets().register(result.getRegistryKey(), result)
			);
			return PluginEventBus.instantiateEvent(RegisterMaterialTextureSetEvent.class, register);
		});
	}

	public static void dispatchMaterialTraits() {
		PluginManager.execute((plugin, modid) -> plugin.registerMaterialTraits(new MaterialTraitRegister() {

			@Override
			public <T extends IMaterialTrait<T>> MaterialTraitKey<T> register(final String name, final Class<T> typeClass) {
				return Util.make(new MaterialTraitKey<>(ResourceLocation.fromNamespaceAndPath(modid, name), typeClass), result -> {
					CAPI.regs().materialTraits().register(result.getRegistryKey(), result);
				});
			}
		}));
	}

	public static void dispatchMaterialFlags() {
		PluginManager.execute((plugin, modid) -> plugin.registerMaterialFlags((registryName, reqFlags, reqTraits) -> {
			final MaterialFlag result = new MaterialFlagImpl.Builder(ResourceLocation.fromNamespaceAndPath(modid, registryName)).requiredFlag(reqFlags).requiredTrait(reqTraits).build();
			CAPI.regs().materialFlags().register(result.getRegistryKey(), result);
			return result;
		}));
	}

	public static void dispatchMaterialTaggedSets() {
		PluginManager.execute((plugin, modid) -> plugin.registerMaterialTaggedSets(MaterialTaggedSetBuilder::new));
	}

	public static void dispatchMaterials() {
		PluginManager.execute((plugin, modid) -> plugin.registerMaterials(registryName -> new MaterialBuilderImpl(ResourceLocation.fromNamespaceAndPath(modid, registryName))));
	}

	public static void dispatchRegisterMachines() {
		PluginManager.execute((plugin, modid) -> plugin.registerMachines(MachineBuilderImpl::new));
	}

	private static void execute(final BiConsumer<IConductancePlugin, String> executor) {
		executor.accept(PluginManager.rootPlugin, Conductance.MODID);
		PluginManager.PLUGINS.forEach(executor);
	}

	private PluginManager() {
	}
}
