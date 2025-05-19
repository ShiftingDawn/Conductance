package conductance.core.apiimpl;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import com.mojang.serialization.Codec;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.objectweb.asm.Type;
import conductance.api.CAPI;
import conductance.api.ConductancePlugin;
import conductance.api.IConductancePlugin;
import conductance.api.capability.cover.CoverEntity;
import conductance.api.capability.cover.CoverEntityConstructor;
import conductance.api.capability.cover.CoverRenderer;
import conductance.api.capability.cover.CoverType;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.RecipeElementCloner;
import conductance.api.machine.recipe.RecipeTypeBuilder;
import conductance.api.material.IMaterialTrait;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialTextureSet;
import conductance.api.material.MaterialTextureType;
import conductance.api.material.MaterialTraitKey;
import conductance.api.material.PeriodicElement;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.MaterialTraitRegister;
import conductance.api.plugin.RegisterCoverEvent;
import conductance.api.plugin.RegisterFieldSerializerEvent;
import conductance.api.plugin.RegisterMaterialOverrideEvent;
import conductance.api.plugin.RegisterMaterialTextureSetEvent;
import conductance.api.plugin.RegisterMaterialTextureTypeEvent;
import conductance.api.plugin.RegisterMaterialUnitOverrideEvent;
import conductance.api.plugin.RegisterPeriodicElementEvent;
import conductance.api.plugin.RegisterRecipeElementTypeEvent;
import conductance.api.plugin.RegisterRecipeEvent;
import conductance.api.plugin.RegisterRecipeTypeEvent;
import conductance.api.plugin.RegisterTagEvent;
import conductance.api.plugin.RegisterTierEvent;
import conductance.api.plugin.RemoveRecipeEvent;
import conductance.api.util.tier.Tier;
import conductance.Conductance;
import conductance.core.cover.CoverTypeImpl;
import conductance.core.machine.MachineBuilderImpl;
import conductance.core.recipe.RecipeElementTypeSerializer;
import conductance.core.recipe.RecipeTypeBuilderImpl;
import conductance.core.register.MaterialOverrideRegister;
import conductance.core.register.MaterialUnitOverrideRegister;
import conductance.core.sync.SyncFieldSerializerRegisterImpl;

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

	public static void dispatchPeriodicElements() {
		PluginEventBus.post(RegisterPeriodicElementEvent.class, modid -> {
			final RegisterPeriodicElementEvent.PeriodicElementRegister register = (protons, neutrons, registryName, name, symbol, parent) -> Util.make(
					new PeriodicElement(ResourceLocation.fromNamespaceAndPath(modid, registryName), protons, neutrons, name, symbol, parent != null ? parent.getRegistryKey() : null),
					result -> CAPI.regs().periodicElements().register(result.getRegistryKey(), result)
			);
			return PluginEventBus.instantiateEvent(RegisterPeriodicElementEvent.class, register);
		});
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

	public static void dispatchMaterialOreTypes() {
		PluginManager.execute((plugin, modid) -> plugin.registerMaterialOreTypes(
				(registryName, bearingBlockModel, mapColor, soundType) ->
						new MaterialOreTypeBuilderImpl(ResourceLocation.fromNamespaceAndPath(modid, registryName), bearingBlockModel, mapColor, soundType))
		);
	}

	public static void dispatchMaterialTaggedSets() {
		PluginManager.execute((plugin, modid) -> plugin.registerMaterialTaggedSets(MaterialTaggedSetBuilder::new));
	}

	public static void dispatchMaterials() {
		PluginManager.execute((plugin, modid) -> plugin.registerMaterials(registryName -> new MaterialBuilderImpl(ResourceLocation.fromNamespaceAndPath(modid, registryName))));
	}

	public static void dispatchTiers() {
		//TODO clean this up
		final RegisterTierEvent event = PluginEventBus.instantiateEvent(RegisterTierEvent.class, new RegisterTierEvent.TierRegister() {

			@Override
			public Tier register(final String registryName, final String displayName, final int tierColor, final Tier previousTier) {
				return new TierImpl.Builder(registryName, displayName, tierColor).previous(previousTier).build();
			}

			@Override
			public Tier register(final String registryName, final String displayName, final int tierColor) {
				return new TierImpl.Builder(registryName, displayName, tierColor).build();
			}
		});
		PluginEventBus.post(RegisterTierEvent.class, modid -> event);
	}

	public static void dispatchRecipeElementTypes() {
		PluginEventBus.post(RegisterRecipeElementTypeEvent.class, modid -> PluginEventBus.instantiateEvent(RegisterRecipeElementTypeEvent.class, modid, new RegisterRecipeElementTypeEvent.RecipeElementTypeRegister() {

			@Override
			public <T> IRecipeElementType<T> register(final ResourceLocation registryKey, final Codec<T> dataCodec, final StreamCodec<RegistryFriendlyByteBuf, T> dataStreamCodec, final RecipeElementCloner<T> cloner) {
				return Util.make(new RecipeElementTypeSerializer<>(registryKey, dataCodec, dataStreamCodec, cloner), result -> {
					CAPI.regs().recipeElementTypes().register(result.getRegistryKey(), result);
				});
			}
		}));
	}

	public static void dispatchRecipeTypes() {
		PluginEventBus.post(RegisterRecipeTypeEvent.class, modid -> PluginEventBus.instantiateEvent(RegisterRecipeTypeEvent.class, modid,
				(Function<ResourceLocation, RecipeTypeBuilder>) RecipeTypeBuilderImpl::new));
	}

	public static void dispatchRegisterMachines() {
		PluginManager.execute((plugin, modid) -> plugin.registerMachines(MachineBuilderImpl::new));
	}

	public static void dispatchRegisterCovers() {
		PluginEventBus.post(RegisterCoverEvent.class, modid -> PluginEventBus.instantiateEvent(RegisterCoverEvent.class, modid, (RegisterCoverEvent.CoverRegister) CoverTypeImpl::new));
	}

	public static void dispatchMaterialOverrides() {
		final RegisterMaterialOverrideEvent event = PluginEventBus.instantiateEvent(RegisterMaterialOverrideEvent.class, new MaterialOverrideRegister());
		PluginEventBus.post(RegisterMaterialOverrideEvent.class, modid -> event);
	}

	public static void dispatchMaterialUnitOverrides() {
		final RegisterMaterialUnitOverrideEvent event = PluginEventBus.instantiateEvent(RegisterMaterialUnitOverrideEvent.class, new MaterialUnitOverrideRegister());
		PluginEventBus.post(RegisterMaterialUnitOverrideEvent.class, modid -> event);
	}

	public static void dispatchTagRegister() {
		final RegisterTagEvent event = PluginEventBus.instantiateEvent(RegisterTagEvent.class, TagRegisterImpl.INSTANCE);
		PluginEventBus.post(RegisterTagEvent.class, modid -> event);
	}

	public static void dispatchRegisterRecipes(final RecipeOutput recipeOutput, final RegisterRecipeEvent.RecipeBuilderFactory builderFactory) {
		PluginEventBus.post(RegisterRecipeEvent.class, modid -> PluginEventBus.instantiateEvent(RegisterRecipeEvent.class, modid, recipeOutput, builderFactory));
	}

	public static void dispatchRemoveRecipes(final Consumer<ResourceLocation> remover) {
		final RemoveRecipeEvent event = PluginEventBus.instantiateEvent(RemoveRecipeEvent.class, remover);
		PluginEventBus.post(RemoveRecipeEvent.class, modid -> event);
	}

	public static void dispatchSyncFieldSerializers() {
		final RegisterFieldSerializerEvent event = PluginEventBus.instantiateEvent(RegisterFieldSerializerEvent.class, SyncFieldSerializerRegisterImpl.INSTANCE);
		PluginEventBus.post(RegisterFieldSerializerEvent.class, modid -> event);
	}

	private static void execute(final BiConsumer<IConductancePlugin, String> executor) {
		executor.accept(PluginManager.rootPlugin, Conductance.MODID);
		PluginManager.PLUGINS.forEach(executor);
	}

	private PluginManager() {
	}
}
