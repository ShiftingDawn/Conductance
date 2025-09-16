package conductance.init;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import conductance.api.CAPI;
import conductance.api.material.Material;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.api.resource.event.AddTranslationEvent;
import conductance.api.util.TextHelper;
import conductance.Conductance;
import conductance.init.item.MaterialItem;

@ConductancePluginListener(modid = Conductance.MODID)
public final class ConductanceItems {

	private static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(Conductance.MODID);

	public static void initialize(final IEventBus modEventBus) {
		ConductanceItems.REGISTRY.register(modEventBus);
		CAPI.regs().materials().forEach(ConductanceItems::generateMaterial);
	}

	private static void generateMaterial(final Material material) {
		CAPI.regs().materialGenerationHandlers().stream()
				.filter(handler -> handler.hasItem() && handler.autoGenerateItem() && handler.test(material) && !Conductance.MATERIALS.hasItemOverride(material, handler))
				.forEach(handler -> {
					ConductanceItems.REGISTRY.registerItem(handler.getUnlocalizedName(material), props -> Util.make(new MaterialItem(props, material, handler), item -> {
						Conductance.MATERIALS.register(material, handler, item);
					}));
				});
	}

	@EventListener(priority = -100)
	private static void addMaterialItemTranslations(final AddTranslationEvent event) {
		ConductanceItems.REGISTRY.getEntries().stream().map(DeferredHolder::get).filter(item -> item instanceof MaterialItem).forEach(item -> {
			final MaterialItem materialItem = (MaterialItem) item;
			final String name = materialItem.getHandler().getUnlocalizedName(materialItem.getMaterial());
			event.add(materialItem.getDescriptionId(), TextHelper.lowerUnderscoreToEnglish(name));
		});
	}

	@EventListener(priority = -100)
	private static void addMaterialItemModels(final AddRuntimeModelEvent event) {
		ConductanceItems.REGISTRY.getEntries().stream().map(DeferredHolder::get).filter(item -> item instanceof MaterialItem).forEach(item -> {
			final MaterialItem materialItem = (MaterialItem) item;
			final ResourceLocation model = CAPI.resourceFinder().getMaterialItemModel(materialItem.getMaterial().getTextureSet(), materialItem.getHandler().getTextureType(), null, null).value();
			event.addItemsModel(materialItem, b -> b.model(model, b2 -> {
				b2.tints(tints -> tints.constant(materialItem.getMaterial().getColor()));
			}));
		});
	}

	private ConductanceItems() {
	}
}
