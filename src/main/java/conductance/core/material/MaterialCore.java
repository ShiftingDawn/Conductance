package conductance.core.material;

import java.util.Arrays;
import java.util.function.IntSupplier;
import java.util.stream.Stream;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import conductance.api.CAPI;
import conductance.api.NCMaterialTraits;
import conductance.api.material.IMaterialTrait;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialOreType;
import conductance.api.material.MaterialTextureSet;
import conductance.api.material.MaterialTextureType;
import conductance.api.material.MaterialTraitKey;
import conductance.api.material.ModifyMaterialEvent;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.material.traits.MaterialTraitDust;
import conductance.api.material.traits.MaterialTraitFluid;
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
import conductance.core.apiimpl.MaterialOreTypeBuilderImpl;
import conductance.core.apiimpl.MaterialTaggedSetBuilder;
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
		MaterialCore.modifyMaterials();
		//Execute after mod construction but before registration has started
		modEventBus.addListener(EventPriority.LOWEST, NewRegistryEvent.class, ignored -> MaterialValidator.validateMaterials());
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
		PluginEventBus.post(RegisterMaterialOverrideEvent.class, ignored -> new RegisterMaterialOverrideEventImpl(MaterialRegistryImpl.INSTANCE::addOverride));
	}

	private static void initUnitOverrides() {
		PluginEventBus.post(RegisterMaterialUnitOverrideEvent.class, ignored -> new RegisterMaterialUnitOverrideEventImpl(MaterialRegistryImpl.INSTANCE::addUnitOverride));
	}

	private static void modifyMaterials() {
		CAPI.regs().materials().forEach(m -> {
			final MaterialImpl material = (MaterialImpl) m;
			MaterialCore.modifyMaterialInternal(material);
			final ModifyMaterialEvent event = new ModifyMaterialEventImpl(material);
			PluginEventBus.post(ModifyMaterialEvent.class, ignored -> event);
		});
	}

	private static void modifyMaterialInternal(final MaterialImpl material) {
		if (material.has(NCMaterialTraits.INGOT)
				|| material.has(NCMaterialTraits.GEM)
				|| material.has(NCMaterialTraits.ORE)
				|| material.has(NCMaterialTraits.WIRE)
				|| material.has(NCMaterialTraits.WOOD)
		) {
			material.getTraits().put(NCMaterialTraits.DUST, new MaterialTraitDust());
		}
		material.executeIf(NCMaterialTraits.LIQUID, trait -> {
			final boolean hasSolidForm = material.has(NCMaterialTraits.DUST);
			material.getTraits().put(NCMaterialTraits.LIQUID, new MaterialTraitFluid.Liquid(
					MaterialCore.orElse(trait.getViscosity(), () -> MaterialTraitFluid.WATER_VISCOSITY),
					MaterialCore.orElse(trait.getTemperature(), () -> hasSolidForm ? MaterialTraitFluid.MOLTEN_TEMP : MaterialTraitFluid.ROOM_TEMP),
					MaterialCore.orElse(trait.getDensity(), () -> hasSolidForm ? MaterialTraitFluid.MOLTEN_DENSITY : MaterialTraitFluid.ROOM_DENSITY)
			));
		});
		material.executeIf(NCMaterialTraits.GAS, trait -> material.getTraits().put(NCMaterialTraits.GAS, new MaterialTraitFluid.Gas(
				MaterialCore.orElse(trait.getViscosity(), () -> MaterialTraitFluid.WATER_VISCOSITY),
				MaterialCore.orElse(trait.getTemperature(), () -> MaterialTraitFluid.ROOM_TEMP),
				MaterialCore.orElse(trait.getDensity(), () -> MaterialTraitFluid.GAS_DENSITY)
		)));
		material.executeIf(NCMaterialTraits.PLASMA, trait -> material.getTraits().put(NCMaterialTraits.PLASMA, new MaterialTraitFluid.Plasma(
				MaterialCore.orElse(trait.getViscosity(), () -> MaterialTraitFluid.WATER_VISCOSITY),
				MaterialCore.orElse(trait.getTemperature(), () -> MaterialTraitFluid.PLASMA_TEMP),
				MaterialCore.orElse(trait.getDensity(), () -> MaterialTraitFluid.PLASMA_DENSITY)
		)));
	}

	private static int orElse(final int value, final IntSupplier fallback) {
		return value != -1 ? value : fallback.getAsInt();
	}

	private MaterialCore() {
	}
}
