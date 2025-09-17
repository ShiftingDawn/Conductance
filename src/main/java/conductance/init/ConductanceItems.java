package conductance.init;

import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import conductance.api.CAPI;
import conductance.api.material.Material;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.api.resource.event.AddTranslationEvent;
import conductance.api.resource.event.RegisterTagEvent;
import conductance.Conductance;
import conductance.core.CreativeTabHelper;
import conductance.init.item.CraftingToolItem;
import conductance.init.item.MaterialItem;

@ConductancePluginListener(modid = Conductance.MODID)
public final class ConductanceItems {

	private static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(Conductance.MODID);
	private static Supplier<Item> WRENCH;
	private static Supplier<Item> HAMMER;
	private static Supplier<Item> WIRE_CUTTERS;

	public static void initialize(final IEventBus modEventBus) {
		ConductanceItems.REGISTRY.register(modEventBus);
		CAPI.regs().materials().forEach(ConductanceItems::generateMaterial);
		ConductanceItems.WRENCH = ConductanceItems.REGISTRY.registerItem("wrench", props -> Util.make(new CraftingToolItem(props), item -> {
			CreativeTabHelper.addToTab(item, CreativeTabHelper.Tabs.GENERAL);
		}));
		ConductanceItems.HAMMER = ConductanceItems.REGISTRY.registerItem("hammer", props -> Util.make(new CraftingToolItem(props), item -> {
			CreativeTabHelper.addToTab(item, CreativeTabHelper.Tabs.GENERAL);
		}));
		ConductanceItems.WIRE_CUTTERS = ConductanceItems.REGISTRY.registerItem("wire_cutters", props -> Util.make(new CraftingToolItem(props), item -> {
			CreativeTabHelper.addToTab(item, CreativeTabHelper.Tabs.GENERAL);
		}));
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
	private static void addItemTranslations(final AddTranslationEvent event) {
		Conductance.MATERIALS.getItemTable().rowMap().forEach((material, map) -> map.forEach((handler, item) -> {
			handler.getGroupTagsAndTranslators(BuiltInRegistries.ITEM, material).forEach((tagKey, translator) -> {
				if (translator != null) {
					final String translation = translator.translate(material);
					if (translation != null) {
						event.add(tagKey, translation.formatted(material.getName()));
					}
				}
			});
			handler.getEntryTagsAndTranslators(BuiltInRegistries.ITEM, material).forEach((tagKey, translator) -> {
				if (translator != null) {
					final String translation = translator.translate(material);
					if (translation != null) {
						event.add(tagKey, translation.formatted(material.getName()));
					}
				}
			});
		}));
		event.add(CAPI.TAG_HAMMERS, "Hammers");
		event.add(CAPI.TAG_WIRE_CUTTERS, "Wire Cutters");
	}

	@EventListener(priority = -100)
	private static void addItemModels(final AddRuntimeModelEvent event) {
		ConductanceItems.REGISTRY.getEntries().stream().map(DeferredHolder::get).filter(item -> item instanceof MaterialItem).forEach(item -> {
			final MaterialItem materialItem = (MaterialItem) item;
			final ResourceLocation model = CAPI.resourceFinder().getMaterialItemModel(materialItem.getMaterial().getTextureSet(), materialItem.getHandler().getTextureType(), null, null).value();
			event.addItemsModel(materialItem, b -> b.model(model, b2 -> {
				b2.tints(tints -> tints.constant(materialItem.getMaterial().getColor()));
			}));
		});
		event.addSimpleItem(ConductanceItems.WRENCH.get());
		event.addSimpleItem(ConductanceItems.HAMMER.get());
		event.addSimpleItem(ConductanceItems.WIRE_CUTTERS.get());
	}

	@EventListener(priority = -100)
	private static void addItemTags(final RegisterTagEvent event) {
		event.tag(Tags.Items.TOOLS, CAPI.TAG_HAMMERS.location(), CAPI.TAG_WIRE_CUTTERS.location());
		event.item(CAPI.TAG_WRENCHES, ConductanceItems.WRENCH.get());
		event.item(CAPI.TAG_HAMMERS, ConductanceItems.HAMMER.get());
		event.item(CAPI.TAG_WIRE_CUTTERS, ConductanceItems.WIRE_CUTTERS.get());
	}

	private ConductanceItems() {
	}
}
