package conductance.init;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.google.common.collect.Tables;
import conductance.api.CAPI;
import conductance.api.NCBlocks;
import conductance.api.NCDataComponents;
import conductance.api.NCItems;
import conductance.api.material.Material;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.api.resource.event.AddTranslationEvent;
import conductance.api.resource.event.RegisterTagEvent;
import conductance.api.tier.Tier;
import conductance.api.tier.TieredItemType;
import conductance.api.util.ExtruderShape;
import conductance.Conductance;
import conductance.core.CreativeTabHelper;
import conductance.core.material.MaterialColorTintSource;
import conductance.init.block.TieredBlock;
import conductance.init.block.TieredBlockItem;
import conductance.init.item.CraftingToolItem;
import conductance.init.item.MaterialItem;
import conductance.init.item.ProgramCircuitItem;
import conductance.init.item.ProgramCircuitSetItemPacketC2S;
import conductance.init.item.TieredItem;
import conductance.lib.network.RegisterPacketEvent;

@ConductancePluginListener(modid = Conductance.MODID)
public final class ConductanceItems {

	private static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(Conductance.MODID);
	private static final List<Holder<Item>> SIMPLE_ITEMS = new ArrayList<>();

	public static void initialize(final IEventBus modEventBus) {
		ConductanceItems.REGISTRY.register(modEventBus);
		modEventBus.addListener(RegisterColorHandlersEvent.ItemTintSources.class, ConductanceItems::handleMaterialItemColors);
		CAPI.regs().materials().forEach(ConductanceItems::generateMaterial);
		NCItems.WRENCH = ConductanceItems.REGISTRY.registerItem("wrench", props -> Util.make(new CraftingToolItem(props), item -> {
			CreativeTabHelper.addToTab(item, CreativeTabHelper.Tabs.GENERAL);
		}));
		NCItems.HAMMER = ConductanceItems.REGISTRY.registerItem("hammer", props -> Util.make(new CraftingToolItem(props), item -> {
			CreativeTabHelper.addToTab(item, CreativeTabHelper.Tabs.GENERAL);
		}));
		NCItems.WIRE_CUTTERS = ConductanceItems.REGISTRY.registerItem("wire_cutters", props -> Util.make(new CraftingToolItem(props), item -> {
			CreativeTabHelper.addToTab(item, CreativeTabHelper.Tabs.GENERAL);
		}));
		ConductanceItems.generateTiered();
		NCItems.PROGRAM_CIRCUIT = ConductanceItems.REGISTRY.registerItem("program_circuit", props -> Util.make(new ProgramCircuitItem(props), item -> {
			for (int i = 0; i <= 24; ++i) {
				CreativeTabHelper.addToTab(ProgramCircuitItem.makeStack(item, i), CreativeTabHelper.Tabs.GENERAL);
			}
		}));
		NCItems.EMPTY_EXTRUDER_SHAPE = ConductanceItems.REGISTRY.registerItem("empty_extruder_shape", props -> Util.make(new CraftingToolItem(props), item -> {
			CreativeTabHelper.addToTab(item, CreativeTabHelper.Tabs.GENERAL);
		}));
		NCItems.EXTRUDER_SHAPES = Collections.unmodifiableMap(Util.make(new EnumMap<>(ExtruderShape.class), map -> {
			for (final ExtruderShape shape : ExtruderShape.values()) {
				map.put(shape, ConductanceItems.REGISTRY.registerItem(shape + "_extruder_shape", props -> Util.make(new CraftingToolItem(props), item -> {
					CreativeTabHelper.addToTab(item, CreativeTabHelper.Tabs.GENERAL);
				})));
			}
		}));
		NCItems.RESIN = ConductanceItems.makeSimpleItem("resin");
		NCItems.RESIN_PULP = ConductanceItems.makeSimpleItem("resin_pulp");
		NCItems.WOOD_CIRCUIT_BOARD = ConductanceItems.makeSimpleItem("wood_circuit_board");
		NCItems.WOOD_CIRCUIT_SUBSTRATE = ConductanceItems.makeSimpleItem("wood_circuit_substrate");
		NCItems.DIODE = ConductanceItems.makeSimpleItem("diode");
		NCItems.RESISTOR = ConductanceItems.makeSimpleItem("resistor");
		NCItems.TRANSISTOR = ConductanceItems.makeSimpleItem("transistor");
	}

	private static Holder<Item> makeSimpleItem(final String itemName) {
		final Holder<Item> result = ConductanceItems.REGISTRY.registerItem(itemName, props -> Util.make(new Item(props), item -> {
			CreativeTabHelper.addToTab(item, CreativeTabHelper.Tabs.GENERAL);
		}));
		ConductanceItems.SIMPLE_ITEMS.add(result);
		return result;
	}

	private static void generateMaterial(final Material material) {
		CAPI.regs().materialGenerationHandlers().stream()
			.filter(handler -> handler.hasItem() && handler.autoGenerateItem() && handler.test(material) && !Conductance.MATERIALS.hasItemOverride(material, handler))
			.forEach(handler -> {
				ConductanceItems.REGISTRY.registerItem(handler.getUnlocalizedName(material), props -> {
					if (handler.getItemBuilderCallback() != null) {
						props = handler.getItemBuilderCallback().apply(material, props);
					}
					return Util.make(new MaterialItem(props, material, handler), item -> {
						Conductance.MATERIALS.register(material, handler, item);
					});
				});
			});
	}

	private static void generateTiered() {
		NCItems.TIERED = Tables.unmodifiableTable(Util.make(Tables.newCustomTable(new EnumMap<>(TieredItemType.class), HashMap::new), table -> {
			for (final Tier tier : CAPI.tiers().getTiers()) {
				for (final TieredItemType itemType : TieredItemType.values()) {
					if (!itemType.isItem()) {
						continue;
					}
					final Holder<Item> generatedItem = ConductanceItems.REGISTRY.registerItem(itemType.getUnlocalizedName(tier), props -> Util.make(new TieredItem(props, itemType, tier), item -> {
						CreativeTabHelper.addToTab(item, CreativeTabHelper.Tabs.GENERAL);
					}));
					table.put(itemType, tier, generatedItem);
				}
				table.put(TieredItemType.MACHINE_CASING, tier, ConductanceItems.REGISTRY.registerItem(
					TieredItemType.MACHINE_CASING.getUnlocalizedName(tier),
					props -> new TieredBlockItem((TieredBlock) NCBlocks.MACHINE_CASING.get(tier).value(), props.useBlockDescriptionPrefix()))
				);
			}
		}));
	}

	@EventListener(priority = -100)
	private static void addItemTranslations(final AddTranslationEvent event) {
		Conductance.MATERIALS.getItemTable().rowMap().forEach((material, map) -> map.forEach((handler, item) -> {
			if (material == null || handler == null) {
				return;
			}
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
	}

	@EventListener(priority = -100)
	private static void addItemModels(final AddRuntimeModelEvent event) {
		ConductanceItems.REGISTRY.getEntries().stream().map(DeferredHolder::get).filter(item -> item instanceof MaterialItem).forEach(item -> {
			final MaterialItem materialItem = (MaterialItem) item;
			final ResourceLocation customTexture = CAPI.resourceFinder().getCustomMaterialTexture(materialItem.getMaterial(), materialItem.getHandler().getTextureType());
			if (customTexture != null) {
				event.addItemsModel(materialItem, b -> b.model(materialItem, tints -> {
				}));
				event.addItemModel(materialItem, b -> b.layer0(customTexture));
			} else {
				final ResourceLocation model = CAPI.resourceFinder().getMaterialItemModel(materialItem.getMaterial().getTextureSet(), materialItem.getHandler().getTextureType(), null, null).value();
				event.addItemsModel(materialItem, b -> b.model(model, b2 -> b2.tints(tints -> {
					if (!materialItem.getMaterial().getColor().hasMultipleColors()) {
						tints.constant(materialItem.getMaterial().getColor().getCurrentColor());
					} else {
						tints.custom(MaterialColorTintSource.ID, json -> json.addProperty("default", -1));
					}
				})));
			}
		});
		ConductanceItems.REGISTRY.getEntries().stream().map(DeferredHolder::get).filter(item -> item instanceof TieredItem).forEach(item -> {
			final TieredItem tieredItem = (TieredItem) item;
			event.addItemsModel(tieredItem, b -> b.model(Conductance.id("item/tier/%s".formatted(tieredItem.getType())), b2 -> {
				b2.tints(tints -> tints.constant(tieredItem.getTier().getColor()));
			}));
		});
		event.addSimpleItem(NCItems.WRENCH.value());
		event.addSimpleItem(NCItems.HAMMER.value());
		event.addSimpleItem(NCItems.WIRE_CUTTERS.value());
		event.addItemsModel(NCItems.PROGRAM_CIRCUIT.value(), model -> model.handAnimationOnSwap(false).select(ResourceLocation.withDefaultNamespace("component"), select -> {
			for (int i = 0; i <= 24; ++i) {
				final int finalI = i;
				select.addCase(i, b -> b.model(Conductance.id("item/program_circuit/" + finalI), b2 -> {
				}));
			}
			select.component(BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(NCDataComponents.PROGRAM_CIRCUIT.get()));
		}));
		for (int i = 0; i <= 24; ++i) {
			final int finalI = i;
			event.addItemModel(Conductance.id("program_circuit/" + i), b -> b.layer0(Conductance.id("item/program_circuit/" + finalI)));
		}
		event.addItemsModel(NCItems.EMPTY_EXTRUDER_SHAPE.value(), b -> b.simple(NCItems.EMPTY_EXTRUDER_SHAPE.value()));
		event.addItemModel(NCItems.EMPTY_EXTRUDER_SHAPE.value(), b -> b.layer0(Conductance.id("item/extruder_shape/empty")));
		NCItems.EXTRUDER_SHAPES.forEach((shape, item) -> {
			event.addItemsModel(item.value(), b -> b.simple(item.value()));
			event.addItemModel(item.value(), b -> b.layer0(Conductance.id("item/extruder_shape/" + shape)));
		});
		ConductanceItems.SIMPLE_ITEMS.forEach(itemHolder -> event.addSimpleItem(itemHolder.value()));
	}

	@EventListener(priority = -100)
	private static void addItemTags(final RegisterTagEvent event) {
		event.tag(Tags.Items.TOOLS, CAPI.TAG_HAMMERS.location(), CAPI.TAG_WIRE_CUTTERS.location());
		event.item(CAPI.TAG_WRENCHES, NCItems.WRENCH.value());
		event.item(CAPI.TAG_HAMMERS, NCItems.HAMMER.value());
		event.item(CAPI.TAG_WIRE_CUTTERS, NCItems.WIRE_CUTTERS.value());
	}

	@EventListener
	private static void registerPackets(final RegisterPacketEvent event) {
		ProgramCircuitSetItemPacketC2S.register(event.getRegistrar());
	}

	private static void handleMaterialItemColors(final RegisterColorHandlersEvent.ItemTintSources event) {
		event.register(MaterialColorTintSource.ID, MaterialColorTintSource.MAP_CODEC);
	}

	private ConductanceItems() {
	}
}
