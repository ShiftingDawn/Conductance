package conductance.core.apiimpl;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;
import org.objectweb.asm.Type;
import conductance.api.CAPI;
import conductance.api.ConductancePlugin;
import conductance.api.IConductancePlugin;
import conductance.api.material.IMaterialTrait;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialOreType;
import conductance.api.material.MaterialTextureSet;
import conductance.api.material.MaterialTextureType;
import conductance.api.material.MaterialTraitKey;
import conductance.api.material.PeriodicElement;
import conductance.api.plugin.MaterialTraitRegister;
import conductance.Conductance;
import conductance.core.register.MaterialOverrideRegister;
import conductance.core.register.MaterialUnitOverrideRegister;

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
			throw new IllegalStateException("Could not find " + CAPI.MOD_ID + " root plugin! Something is seriously wrong!");
		}
		PluginManager.PLUGINS.remove(PluginManager.ROOT_PLUGIN);
	}

	private static void findPlugins() {
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

	public static void dispatchPeriodicElements() {
		PluginManager.execute((plugin, modid) -> plugin.registerPeriodicElements((protons, neutrons, registryName, name, symbol, parent) -> Util.make(
				new PeriodicElement(ResourceLocation.fromNamespaceAndPath(modid, registryName), protons, neutrons, name, symbol, parent != null ? parent.getRegistryKey() : null), result -> {
					CAPI.REGS.periodicElements().register(result.getRegistryKey(), result);
				})));
		// TODO KubeJS
	}

	public static void dispatchMaterialTextureTypes() {
		PluginManager.execute((plugin, modid) -> plugin.registerMaterialTextureTypes(registryName -> Util.make(new MaterialTextureType(ResourceLocation.fromNamespaceAndPath(modid, registryName)), result -> {
			CAPI.REGS.materialTextureTypes().register(result.getRegistryKey(), result);
		})));
		// TODO KubeJS
	}

	public static void dispatchMaterialTextureSets() {
		PluginManager.execute((plugin, modid) -> plugin.registerMaterialTextureSets((registryName, parentSetName) -> Util.make(new MaterialTextureSet(registryName, parentSetName), result -> {
			CAPI.REGS.materialTextureSets().register(result.getRegistryKey(), result);
		})));
		// TODO KubeJS
	}

	public static void dispatchMaterialTraits() {
		PluginManager.execute((plugin, modid) -> plugin.registerMaterialTraits(new MaterialTraitRegister() {

			@Override
			public <T extends IMaterialTrait<T>> MaterialTraitKey<T> register(String name, Class<T> typeClass) {
				return Util.make(new MaterialTraitKey<T>(ResourceLocation.fromNamespaceAndPath(modid, name), typeClass), result -> {
					CAPI.REGS.materialTraits().register(result.getRegistryKey(), result);
				});
			}
		}));
		// TODO KubeJS
	}

	public static void dispatchMaterialFlags() {
		PluginManager.execute((plugin, modid) -> plugin.registerMaterialFlags((registryName, reqFlags, reqTraits) -> {
			MaterialFlag result = new MaterialFlagImpl.Builder(ResourceLocation.fromNamespaceAndPath(modid, registryName)).requiredFlag(reqFlags).requiredTrait(reqTraits).build();
			CAPI.REGS.materialFlags().register(result.getRegistryKey(), result);
			return result;
		}));
		// TODO KubeJS
	}

	public static void dispatchMaterialOreTypes() {
		PluginManager.execute(
				(plugin, modid) -> plugin.registerMaterialOreTypes((registryName, oreBlockType, bearingBlockModel, unlocalizedNameFactory, stoneTagName, hasDoubleOutput, hasGravity, mapColor, soundType) -> {
					MaterialOreType result = new MaterialOreTypeImpl(ResourceLocation.fromNamespaceAndPath(modid, registryName), oreBlockType, bearingBlockModel, unlocalizedNameFactory, stoneTagName, hasDoubleOutput,
							hasGravity, mapColor, soundType);
					CAPI.REGS.materialOreTypes().register(result.getRegistryKey(), result);
					return result;
				}));
		// TODO KubeJS
	}

	public static void dispatchMaterialTaggedSets() {
		PluginManager.execute((plugin, modid) -> plugin.registerMaterialTaggedSets(MaterialTaggedSetBuilder::new));
		// TODO KubeJS
	}

	public static void dispatchMaterials() {
		PluginManager.execute((plugin, modid) -> plugin.registerMaterials(registryName -> new MaterialBuilderImpl(ResourceLocation.fromNamespaceAndPath(modid, registryName))));
		// TODO KubeJS
	}

	public static void dispatchMaterialOverrides() {
		PluginManager.execute((plugin, modid) -> plugin.registerMaterialOverrides(new MaterialOverrideRegister()));
		// TODO KubeJS
	}

	public static void dispatchMaterialUnitOverrides() {
		PluginManager.execute((plugin, modid) -> plugin.registerMaterialUnitOverrides(new MaterialUnitOverrideRegister()));
		// TODO KubeJS
	}

	public static void dispatchTagRegister() {
		PluginManager.execute((plugin, modid) -> plugin.registerTags(TagRegisterImpl.INSTANCE));
		// TODO KubeJS
	}

	private static void execute(final BiConsumer<IConductancePlugin, String> executor) {
		executor.accept(PluginManager.ROOT_PLUGIN, Conductance.MODID);
		PluginManager.PLUGINS.forEach(executor);
	}
}
