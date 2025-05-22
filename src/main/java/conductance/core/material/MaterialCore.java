package conductance.core.material;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegisterEvent;
import conductance.api.material.IMaterialTrait;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialOreType;
import conductance.api.material.MaterialTextureSet;
import conductance.api.material.MaterialTextureType;
import conductance.api.material.MaterialTraitKey;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.plugin.RegisterMaterialEvent;
import conductance.api.plugin.RegisterMaterialFlagEvent;
import conductance.api.plugin.RegisterMaterialOreTypeEvent;
import conductance.api.plugin.RegisterMaterialOverrideEvent;
import conductance.api.plugin.RegisterMaterialTaggedSetEvent;
import conductance.api.plugin.RegisterMaterialTextureSetEvent;
import conductance.api.plugin.RegisterMaterialTextureTypeEvent;
import conductance.api.plugin.RegisterMaterialTraitEvent;
import conductance.api.plugin.RegisterMaterialUnitOverrideEvent;
import conductance.api.registry.MaterialRegistry;
import conductance.Conductance;
import conductance.core.apiimpl.ApiBridge;
import conductance.core.apiimpl.MaterialBuilderImpl;
import conductance.core.apiimpl.MaterialOreTypeBuilderImpl;
import conductance.core.apiimpl.MaterialTaggedSetBuilder;
import conductance.core.register.MaterialRegistryImpl;
import conductance.loader.PluginEventBus;

public final class MaterialCore {

	public static void initialize(final IEventBus modEventBus) {
		Conductance.setApiValue(MaterialRegistry.class, MaterialRegistryImpl.INSTANCE);
		MaterialCore.initTextureTypes();
		MaterialCore.initTextureSets();
		MaterialCore.initOreTypes();
		MaterialCore.initTraits();
		MaterialCore.initFlags();
		MaterialCore.initTaggedSets();
		MaterialCore.initMaterials();
		MaterialCore.initOverrides();
		MaterialCore.initUnitOverrides();
		modEventBus.addListener(EventPriority.LOWEST, RegisterEvent.class, $ -> MaterialCore.validateMaterials());
	}

	private static void initOreTypes() {
		PluginEventBus.post(RegisterMaterialOreTypeEvent.class, modid -> new RegisterMaterialOreTypeEventImpl(((registryName, bearingBlockModel, mapColor, soundType, builder) -> {
			final ResourceLocation registryKey = ResourceLocation.fromNamespaceAndPath(modid, registryName);
			final MaterialOreType result = Util.make(new MaterialOreTypeBuilderImpl(registryKey, bearingBlockModel, mapColor, soundType), builder).build();
			ApiBridge.getRegs().materialOreTypes().register(result);
			return result;
		})));
	}

	private static void initTextureTypes() {
		PluginEventBus.post(RegisterMaterialTextureTypeEvent.class, modid -> new RegisterMaterialTextureTypeEventImpl(registryName -> {
			final MaterialTextureType result = new MaterialTextureType(ResourceLocation.fromNamespaceAndPath(modid, registryName));
			ApiBridge.getRegs().materialTextureTypes().register(result);
			return result;
		}));
	}

	private static void initTextureSets() {
		PluginEventBus.postAll(RegisterMaterialTextureSetEvent.class, new RegisterMaterialTextureSetEventImpl((registryName, parentSetName) -> {
			final MaterialTextureSet result = new MaterialTextureSet(registryName, parentSetName);
			ApiBridge.getRegs().materialTextureSets().register(result);
			return result;
		}));
	}

	private static void initTraits() {
		PluginEventBus.post(RegisterMaterialTraitEvent.class, modid -> new RegisterMaterialTraitEventImpl(new RegisterMaterialTraitEventImpl.MaterialTraitRegister() {

			@Override
			public <T extends IMaterialTrait<T>> MaterialTraitKey<T> apply(final String registryName, final Class<T> typeClass) {
				final MaterialTraitKey<T> result = new MaterialTraitKey<>(ResourceLocation.fromNamespaceAndPath(modid, registryName), typeClass);
				ApiBridge.getRegs().materialTraits().register(result);
				return result;
			}
		}));
	}

	private static void initFlags() {
		PluginEventBus.post(RegisterMaterialFlagEvent.class, modid -> new RegisterMaterialFlagEventImpl((registryName, reqFlags, reqTraits) -> {
			final MaterialFlag result = new MaterialFlag(ResourceLocation.fromNamespaceAndPath(modid, registryName), reqFlags, reqTraits);
			ApiBridge.getRegs().materialFlags().register(result);
			return result;
		}));
	}

	private static void initTaggedSets() {
		PluginEventBus.post(RegisterMaterialTaggedSetEvent.class, modid -> new RegisterMaterialTaggedSetEventImpl((registryName, unlocalizedNameFactory, oreType, builder) -> {
			final TaggedMaterialSet result = Util.make(new MaterialTaggedSetBuilder(registryName, unlocalizedNameFactory, oreType), builder).build();
			ApiBridge.getRegs().materialTaggedSets().register(result);
			return result;
		}));
	}

	private static void initMaterials() {
		PluginEventBus.post(RegisterMaterialEvent.class, modid -> new RegisterMaterialEventImpl((registryName, builder) -> {
			final Material result = Util.make(new MaterialBuilderImpl(ResourceLocation.fromNamespaceAndPath(modid, registryName)), builder).build();
			ApiBridge.getRegs().materials().register(result);
			return result;
		}));
	}

	private static void initOverrides() {
		//TODO clean this up
		PluginEventBus.postAll(RegisterMaterialOverrideEvent.class, new RegisterMaterialOverrideEventImpl(MaterialRegistryImpl.INSTANCE.getOverrideMap()::put));
	}

	private static void initUnitOverrides() {
		//TODO clean this up
		PluginEventBus.postAll(RegisterMaterialUnitOverrideEvent.class, new RegisterMaterialUnitOverrideEventImpl(MaterialRegistryImpl.INSTANCE.getUnitOverrideMap()::put));
	}

	private static void validateMaterials() {
		//		CAPI.regs().materials().forEach(material ->);
	}

	private MaterialCore() {
	}
}
