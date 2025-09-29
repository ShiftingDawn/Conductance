package conductance.core.material;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.apache.commons.lang3.StringUtils;
import conductance.api.CAPI;
import conductance.api.NCMaterialGenerationHandlers;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.material.MaterialOreBearer;
import conductance.api.material.MaterialRegistry;
import conductance.api.material.MaterialTrait;
import conductance.api.material.MaterialTraitKey;
import conductance.api.material.event.RegisterMaterialEvent;
import conductance.api.material.event.RegisterMaterialFlagEvent;
import conductance.api.material.event.RegisterMaterialGenerationHandlerEvent;
import conductance.api.material.event.RegisterMaterialOreBearerEvent;
import conductance.api.material.event.RegisterMaterialOverrideEvent;
import conductance.api.material.event.RegisterMaterialTraitEvent;
import conductance.api.material.event.RegisterMaterialUnitOverrideEvent;
import conductance.Conductance;
import conductance.core.CreativeTabHelper;

public final class MaterialCore {

	public static void initialize(final IEventBus modEventBus) {
		Conductance.MATERIALS = Util.make(new MaterialRegistryImpl(), reg -> Conductance.setApiValue(MaterialRegistry.class, reg));

		MaterialCore.initFlags();
		MaterialCore.initTraits();
		MaterialCore.initOreBearers();
		MaterialCore.initGenerationHandlers();
		MaterialCore.initMaterials();
		MaterialCore.initOverrides();
		MaterialCore.initUnitOverrides();
		modEventBus.addListener(FMLLoadCompleteEvent.class, ignored -> MaterialCore.validateMaterials());

		modEventBus.addListener(EventPriority.HIGHEST, BuildCreativeModeTabContentsEvent.class, event -> {
			if (event.getTabKey().location().equals(Conductance.id(CreativeTabHelper.Tabs.MATERIAL.getName()))) {
				CAPI.regs().materials().forEach(material -> CAPI.regs().materialGenerationHandlers().forEach(handler -> {
					if (handler.getOreBearer() != null || handler == NCMaterialGenerationHandlers.RAW_ORE || handler == NCMaterialGenerationHandlers.RAW_ORE_BLOCK) {
						//Ores, raw ores and raw ore blocks have their own creative tab
						return;
					}
					final Item item = CAPI.materials().getItem(material, handler);
					if (item != null) {
						event.accept(item);
					}
				}));
			} else if (event.getTabKey().location().equals(Conductance.id(CreativeTabHelper.Tabs.ORE.getName()))) {
				CAPI.regs().materials().forEach(material -> CAPI.regs().materialGenerationHandlers().forEach(handler -> {
					if (handler.getOreBearer() == null && handler != NCMaterialGenerationHandlers.RAW_ORE && handler != NCMaterialGenerationHandlers.RAW_ORE_BLOCK) {
						return;
					}
					final Item item = CAPI.materials().getItem(material, handler);
					if (item != null) {
						event.accept(item);
					}
				}));
			}
		});
	}

	private static void initFlags() {
		Conductance.dispatch(RegisterMaterialFlagEvent.class, modid -> new RegisterMaterialFlagEventImpl((registryName, materialFlags, validator) -> {
			final ResourceLocation registryKey = ResourceLocation.fromNamespaceAndPath(modid, registryName);
			final MaterialFlagImpl result = new MaterialFlagImpl(materialFlags, validator);
			Conductance.REGISTRIES.register(Conductance.REGISTRIES.materialFlags(), registryKey, result);
			return result;
		}));
	}

	private static void initTraits() {
		Conductance.dispatch(RegisterMaterialTraitEvent.class, modid -> new RegisterMaterialTraitEventImpl(new RegisterMaterialTraitEventImpl.MaterialTraitRegister() {

			@Override
			public <T extends MaterialTrait<T>> MaterialTraitKey<T> apply(final String registryName, final Class<T> typeClass) {
				final ResourceLocation registryKey = ResourceLocation.fromNamespaceAndPath(modid, registryName);
				final MaterialTraitKey<T> result = new MaterialTraitKey<>(typeClass);
				Conductance.REGISTRIES.register(Conductance.REGISTRIES.materialTraits(), registryKey, result);
				return result;
			}
		}));
	}

	private static void initOreBearers() {
		Conductance.dispatch(RegisterMaterialOreBearerEvent.class, modid -> new RegisterMaterialOreBearerEventImpl((registryName, bearingBlockModel, mapColor, soundType, builder) -> {
			final ResourceLocation registryKey = ResourceLocation.fromNamespaceAndPath(modid, registryName);
			final MaterialOreBearer result = Util.make(new MaterialOreBearerBuilderImpl(bearingBlockModel, mapColor, soundType), b -> {
				if (builder != null) {
					builder.accept(b);
				}
			}).build();
			Conductance.REGISTRIES.register(Conductance.REGISTRIES.materialOreBearers(), registryKey, result);
			return result;
		}));
	}

	private static void initMaterials() {
		Conductance.dispatch(RegisterMaterialEvent.class, modid -> new RegisterMaterialEventImpl((registryName, periodicElement, builder) -> {
			final ResourceLocation registryKey = ResourceLocation.fromNamespaceAndPath(modid, registryName);
			final Material result = Util.make(new MaterialBuilderImpl(periodicElement), builder).build(registryKey);
			Conductance.REGISTRIES.register(Conductance.REGISTRIES.materials(), registryKey, result);
			return result;
		}));
	}

	private static void initGenerationHandlers() {
		Conductance.dispatch(RegisterMaterialGenerationHandlerEvent.class, modid -> new RegisterMaterialGenerationHandlerEventImpl((registryName, unlocalizedNameFactory, oreBearer, builder) -> {
			final ResourceLocation registryKey = ResourceLocation.fromNamespaceAndPath(modid, registryName);
			final MaterialGenerationHandler result = Util.make(new MaterialGenerationHandlerBuilderImpl(unlocalizedNameFactory, oreBearer), builder).build();
			Conductance.REGISTRIES.register(Conductance.REGISTRIES.materialGenerationHandlers(), registryKey, result);
			return result;
		}));
	}

	private static void initOverrides() {
		Conductance.dispatchAll(RegisterMaterialOverrideEvent.class, new RegisterMaterialOverrideEventImpl(
			Conductance.MATERIALS::addOverride, Conductance.MATERIALS::addOverride, Conductance.MATERIALS::addOverride
		));
	}

	private static void initUnitOverrides() {
		Conductance.dispatchAll(RegisterMaterialUnitOverrideEvent.class, new RegisterMaterialUnitOverrideEventImpl(Conductance.MATERIALS::addUnitOverride));
	}

	private static void validateMaterials() {
		final Map<MaterialImpl, List<String>> allErrors = new LinkedHashMap<>();
		for (final Material material : CAPI.regs().materials()) {
			if (material instanceof final MaterialImpl mat) {
				final List<String> errors = mat.validate();
				if (!errors.isEmpty()) {
					allErrors.put(mat, errors);
				}
			}
		}
		if (!allErrors.isEmpty()) {
			if (allErrors.size() == 1) {
				final Map.Entry<MaterialImpl, List<String>> entry = allErrors.entrySet().iterator().next();
				if (entry.getValue().size() == 1) {
					throw new IllegalStateException("Material validation failed for material %s. Reason: %s".formatted(
						entry.getKey().getId(), entry.getValue().getFirst()
					));
				}
			}
			Conductance.LOGGER.error(StringUtils.repeat('=', 32));
			Conductance.LOGGER.error("Material errors");
			Conductance.LOGGER.error(StringUtils.repeat('=', 32));
			allErrors.forEach((material, errors) -> {
				Conductance.LOGGER.error(material.getId().toString());
				errors.forEach(error -> Conductance.LOGGER.error("\t{}", error));
			});
			Conductance.LOGGER.error(StringUtils.repeat('=', 32));
			Conductance.LOGGER.error("End of material errors");
			Conductance.LOGGER.error(StringUtils.repeat('=', 32));
			throw new IllegalStateException("Material validation failed. See the log above for all validation errors");
		}
	}

	private MaterialCore() {
	}
}
