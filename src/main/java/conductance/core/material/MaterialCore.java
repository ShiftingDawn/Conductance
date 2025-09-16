package conductance.core.material;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import conductance.api.CAPI;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.material.MaterialRegistry;
import conductance.api.material.event.RegisterMaterialEvent;
import conductance.api.material.event.RegisterMaterialFlagEvent;
import conductance.api.material.event.RegisterMaterialGenerationHandlerEvent;
import conductance.api.material.event.RegisterMaterialOverridesEvent;
import conductance.Conductance;
import conductance.core.CreativeTabHelper;

public final class MaterialCore {

	public static void initialize(final IEventBus modEventBus) {
		Conductance.MATERIALS = Util.make(new MaterialRegistryImpl(), reg -> Conductance.setApiValue(MaterialRegistry.class, reg));

		MaterialCore.initFlags();
		MaterialCore.initMaterials();
		MaterialCore.initGenerationHandlers();
		MaterialCore.initOverrides();

		modEventBus.addListener(EventPriority.HIGHEST, BuildCreativeModeTabContentsEvent.class, event -> {
			if (event.getTabKey().location().equals(Conductance.id(CreativeTabHelper.Tabs.MATERIAL.getName()))) {
				CAPI.regs().materials().forEach(material -> CAPI.regs().materialGenerationHandlers().forEach(handler -> {
					final Item item = CAPI.materials().getItem(material, handler);
					if (item != null) {
						event.accept(item);
					}
				}));
			}
		});
	}

	private static void initFlags() {
		Conductance.dispatch(RegisterMaterialFlagEvent.class, modid -> new RegisterMaterialFlagEventImpl((registryName, materialFlags) -> {
			final ResourceLocation registryKey = ResourceLocation.fromNamespaceAndPath(modid, registryName);
			final MaterialFlagImpl result = new MaterialFlagImpl(materialFlags);
			Conductance.REGISTRIES.register(Conductance.REGISTRIES.materialFlags(), registryKey, result);
			return result;
		}));
	}

	private static void initMaterials() {
		Conductance.dispatch(RegisterMaterialEvent.class, modid -> new RegisterMaterialEventImpl((registryName, periodicElement, builder) -> {
			final ResourceLocation registryKey = ResourceLocation.fromNamespaceAndPath(modid, registryName);
			final Material result = Util.make(new MaterialBuilderImpl(), builder).build();
			Conductance.REGISTRIES.register(Conductance.REGISTRIES.materials(), registryKey, result);
			return result;
		}));
	}

	private static void initGenerationHandlers() {
		Conductance.dispatch(RegisterMaterialGenerationHandlerEvent.class, modid -> new RegisterMaterialGenerationHandlerEventImpl((registryName, unlocalizedNameFactory, builder) -> {
			final ResourceLocation registryKey = ResourceLocation.fromNamespaceAndPath(modid, registryName);
			final MaterialGenerationHandler result = Util.make(new MaterialGenerationHandlerBuilderImpl(unlocalizedNameFactory), builder).build();
			Conductance.REGISTRIES.register(Conductance.REGISTRIES.materialGenerationHandlers(), registryKey, result);
			return result;
		}));
	}

	private static void initOverrides() {
		Conductance.dispatchAll(RegisterMaterialOverridesEvent.class, new RegisterMaterialOverridesEventImpl(
				Conductance.MATERIALS::addOverride, Conductance.MATERIALS::addOverride
		));
	}

	private MaterialCore() {
	}
}
