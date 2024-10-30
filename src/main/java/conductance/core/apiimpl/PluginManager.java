package conductance.core.apiimpl;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import net.minecraft.Util;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import com.mojang.serialization.Codec;
import net.neoforged.fml.ModList;
import org.objectweb.asm.Type;
import conductance.api.CAPI;
import conductance.api.ConductancePlugin;
import conductance.api.IConductancePlugin;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.material.IMaterialTrait;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialOreType;
import conductance.api.material.MaterialTextureSet;
import conductance.api.material.MaterialTextureType;
import conductance.api.material.MaterialTraitKey;
import conductance.api.material.PeriodicElement;
import conductance.api.plugin.MaterialTraitRegister;
import conductance.api.plugin.RecipeBuilderFactory;
import conductance.api.plugin.RecipeElementTypeRegister;
import conductance.Conductance;
import conductance.core.data.ManagedFieldValueHandlerRegistry;
import conductance.core.machine.MachineBuilderImpl;
import conductance.core.recipe.RecipeElementTypeSerializer;
import conductance.core.recipe.RecipeTypeBuilderImpl;
import conductance.core.register.MaterialOverrideRegister;
import conductance.core.register.MaterialUnitOverrideRegister;

//TODO add KubeJS event dispatches to plugin dispatches
public final class PluginManager {

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

	public static void dispatchRegisterManagedFieldValueHandlers() {
		PluginManager.execute((plugin, modid) -> plugin.registerManagedFieldValueHandlers(ManagedFieldValueHandlerRegistry.INSTANCE::register));
	}

	public static void dispatchPeriodicElements() {
		PluginManager.execute((plugin, modid) -> plugin.registerPeriodicElements((protons, neutrons, registryName, name, symbol, parent) -> Util.make(
				new PeriodicElement(ResourceLocation.fromNamespaceAndPath(modid, registryName), protons, neutrons, name, symbol, parent != null ? parent.getRegistryKey() : null), result -> {
					CAPI.regs().periodicElements().register(result.getRegistryKey(), result);
				})));
	}

	public static void dispatchMaterialTextureTypes() {
		PluginManager.execute((plugin, modid) -> plugin.registerMaterialTextureTypes(registryName -> Util.make(new MaterialTextureType(ResourceLocation.fromNamespaceAndPath(modid, registryName)), result -> {
			CAPI.regs().materialTextureTypes().register(result.getRegistryKey(), result);
		})));
	}

	public static void dispatchMaterialTextureSets() {
		PluginManager.execute((plugin, modid) -> plugin.registerMaterialTextureSets((registryName, parentSetName) -> Util.make(new MaterialTextureSet(registryName, parentSetName), result -> {
			CAPI.regs().materialTextureSets().register(result.getRegistryKey(), result);
		})));
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

	public static void dispatchMaterialOreTypes() {
		PluginManager.execute(
				(plugin, modid) -> plugin.registerMaterialOreTypes((registryName, oreBlockType, bearingBlockModel, unlocalizedNameFactory, stoneTagName, hasDoubleOutput, hasGravity, mapColor, soundType) -> {
					final MaterialOreType result = new MaterialOreTypeImpl(ResourceLocation.fromNamespaceAndPath(modid, registryName), oreBlockType, bearingBlockModel, unlocalizedNameFactory, stoneTagName,
							hasDoubleOutput, hasGravity, mapColor, soundType);
					CAPI.regs().materialOreTypes().register(result.getRegistryKey(), result);
					return result;
				}));
	}

	public static void dispatchMaterialTaggedSets() {
		PluginManager.execute((plugin, modid) -> plugin.registerMaterialTaggedSets(MaterialTaggedSetBuilder::new));
	}

	public static void dispatchMaterials() {
		PluginManager.execute((plugin, modid) -> plugin.registerMaterials(registryName -> new MaterialBuilderImpl(ResourceLocation.fromNamespaceAndPath(modid, registryName))));
	}

	public static void dispatchRecipeElementTypes() {
		PluginManager.execute((plugin, modid) -> plugin.registerRecipeElementTypes(new RecipeElementTypeRegister() {
			@Override
			public <T> IRecipeElementType<T> register(final String name, final Codec<T> dataCodec, final StreamCodec<RegistryFriendlyByteBuf, T> streamDataCodec) {
				return Util.make(new RecipeElementTypeSerializer<>(ResourceLocation.fromNamespaceAndPath(modid, name), dataCodec, streamDataCodec), result -> {
					CAPI.regs().recipeElementTypes().register(result.getRegistryKey(), result);
				});
			}
		}));
	}

	public static void dispatchRecipeTypes() {
		PluginManager.execute((plugin, modid) -> plugin.registerRecipeTypes(registryKey -> new RecipeTypeBuilderImpl(ResourceLocation.fromNamespaceAndPath(modid, registryKey))));
	}

	public static void dispatchRegisterMachines() {
		PluginManager.execute((plugin, modid) -> plugin.registerMachines(MachineBuilderImpl::new));
	}

	public static void dispatchMaterialOverrides() {
		PluginManager.execute((plugin, modid) -> plugin.registerMaterialOverrides(new MaterialOverrideRegister()));
	}

	public static void dispatchMaterialUnitOverrides() {
		PluginManager.execute((plugin, modid) -> plugin.registerMaterialUnitOverrides(new MaterialUnitOverrideRegister()));
	}

	public static void dispatchTagRegister() {
		PluginManager.execute((plugin, modid) -> plugin.registerTags(TagRegisterImpl.INSTANCE));
	}

	public static void dispatchRegisterRecipes(final RecipeOutput recipeOutput, final RecipeBuilderFactory builderFactory) {
		PluginManager.execute((plugin, modid) -> plugin.registerRecipes(recipeOutput, builderFactory));
	}

	private static void execute(final BiConsumer<IConductancePlugin, String> executor) {
		executor.accept(PluginManager.rootPlugin, Conductance.MODID);
		PluginManager.PLUGINS.forEach(executor);
	}

	private PluginManager() {
	}
}
