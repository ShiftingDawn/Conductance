package conductance.init;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.NCBlockStateProperties;
import conductance.api.NCBlocks;
import conductance.api.NCCapabilities;
import conductance.api.NCMaterialTraits;
import conductance.api.coil.CoilBlockType;
import conductance.api.material.Material;
import conductance.api.material.MaterialOreBearer;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.api.resource.event.AddTranslationEvent;
import conductance.api.tier.TieredItemType;
import conductance.Conductance;
import conductance.core.CreativeTabHelper;
import conductance.core.material.MaterialColorTintSource;
import conductance.init.block.ActiveBlock;
import conductance.init.block.CoilBlock;
import conductance.init.block.CoilBlockItem;
import conductance.init.block.CreativeTankBlock;
import conductance.init.block.CreativeTankBlockEntity;
import conductance.init.block.CreativeTankBlockItem;
import conductance.init.block.MaterialBlock;
import conductance.init.block.MaterialBlockItem;
import conductance.init.block.MaterialOreBlock;
import conductance.init.block.MaterialOreBlockItem;
import conductance.init.block.MaterialOreRotatedPillarBlock;
import conductance.init.block.SimpleBlock;
import conductance.init.block.TieredBlock;
import conductance.init.block.WireBlock;
import conductance.init.block.WireBlockEntity;
import conductance.init.block.WireBlockItem;
import conductance.lib.pipenet.WireRegistry;
import conductance.lib.pipenet.WireType;

@ConductancePluginListener(modid = Conductance.MODID)
public final class ConductanceBlocks {

	private static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(Conductance.MODID);
	private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Conductance.MODID);
	private static final Map<Holder<Block>, ResourceLocation> SIMPLE_BLOCKS = new IdentityHashMap<>();
	private static final Map<Holder<Block>, ResourceLocation> ACTIVE_BLOCKS = new IdentityHashMap<>();

	public static void initialize(final IEventBus modEventBus) {
		ConductanceBlocks.REGISTRY.register(modEventBus);
		ConductanceBlocks.ITEMS.register(modEventBus);
		modEventBus.addListener(RegisterCapabilitiesEvent.class, ConductanceBlocks::attachCapabilities);
		CAPI.regs().materials().forEach(ConductanceBlocks::generateMaterial);
		ConductanceBlocks.generateTiered();
		modEventBus.addListener(RegisterColorHandlersEvent.Block.class, ConductanceBlocks::handleMaterialBlockColors);
		modEventBus.addListener(RegisterColorHandlersEvent.Block.class, ConductanceBlocks::handleCoilColors);
		NCBlocks.CASING_BRONZE = ConductanceBlocks.makeSimpleBlock("bronze_casing", "casing/bronze");
		NCBlocks.CASING_STEEL = ConductanceBlocks.makeSimpleBlock("steel_casing", "casing/steel");
		NCBlocks.CASING_INVAR = ConductanceBlocks.makeSimpleBlock("invar_casing", "casing/invar");
		NCBlocks.CASING_ALUMINIUM = ConductanceBlocks.makeSimpleBlock("aluminium_casing", "casing/aluminium");
		NCBlocks.CASING_BRONZE_FIREBOX = ConductanceBlocks.makeActiveBlock("bronze_firebox_casing", "casing/bronze_firebox");
		ConductanceBlocks.generateCoils();
		NCBlocks.CREATIVE_TANK = ConductanceBlocks.REGISTRY.registerBlock("creative_tank", props -> CAPI.make(new CreativeTankBlock(props), block -> {
			ConductanceBlocks.ITEMS.registerItem("creative_tank", itemProps -> new CreativeTankBlockItem(block, itemProps));
			CreativeTabHelper.addToTab(block, CreativeTabHelper.Tabs.GENERAL);
		}));
	}

	private static Holder<Block> makeBlock(final String blockName, final Function<BlockBehaviour.Properties, Block> factory) {
		final Holder<Block> result = ConductanceBlocks.REGISTRY.registerBlock(blockName, props -> CAPI.make(factory.apply(props), block -> {
			CreativeTabHelper.addToTab(block, CreativeTabHelper.Tabs.GENERAL);
		}));
		ConductanceBlocks.ITEMS.registerSimpleBlockItem(result);
		return result;
	}

	private static Holder<Block> makeSimpleBlock(final String blockName, @Nullable final String texture) {
		return CAPI.make(ConductanceBlocks.makeBlock(blockName, SimpleBlock::new), result -> {
			ConductanceBlocks.SIMPLE_BLOCKS.put(result, Conductance.id("block/" + Objects.requireNonNullElseGet(texture, () -> result.getKey().location().getPath())));
		});
	}

	private static Holder<Block> makeActiveBlock(final String blockName, @Nullable final String texture) {
		return CAPI.make(ConductanceBlocks.makeBlock(blockName, ActiveBlock::new), result -> {
			ConductanceBlocks.ACTIVE_BLOCKS.put(result, Conductance.id("block/" + Objects.requireNonNullElseGet(texture, () -> result.getKey().location().getPath())));
		});
	}

	private static void generateMaterial(final Material material) {
		CAPI.regs().materialGenerationHandlers().stream()
			.filter(handler -> handler.hasBlock() && handler.autoGenerateBlock() && handler.test(material) && !Conductance.MATERIALS.hasBlockOverride(material, handler))
			.forEach(handler -> {
				final String name = handler.getUnlocalizedName(material);
				final DeferredBlock<MaterialBlock> holder = ConductanceBlocks.REGISTRY.registerBlock(name, props -> {
					if (!handler.shouldOccludeBlocks()) {
						props = props.noOcclusion();
					}
					if (handler.getBlockBuilderCallback() != null) {
						props = handler.getBlockBuilderCallback().apply(material, props);
					}
					return CAPI.make(new MaterialBlock(props, material, handler), block -> {
						Conductance.MATERIALS.register(material, handler, block);
					});
				}, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE));
				ConductanceBlocks.ITEMS.registerItem(name, props -> {
					if (handler.getBlockItemBuilderCallback() != null) {
						props = handler.getBlockItemBuilderCallback().apply(material, props);
					}
					return CAPI.make(new MaterialBlockItem(holder.value(), props.useBlockDescriptionPrefix()), item -> {
						Conductance.MATERIALS.register(material, handler, item);
					});
				});
			});
		CAPI.regs().materialGenerationHandlers().stream()
			.filter(handler -> handler.hasBlock() && handler.getOreBearer() != null && handler.test(material) && !Conductance.MATERIALS.hasBlockOverride(material, handler))
			.forEach(handler -> {
				final String name = handler.getUnlocalizedName(material);
				final MaterialOreBearer bearer = handler.getOreBearer();
				final DeferredBlock<Block> holder = ConductanceBlocks.REGISTRY.registerBlock(name, props -> {
					if (!handler.shouldOccludeBlocks()) {
						props = props.noOcclusion();
					}
					if (handler.getBlockBuilderCallback() != null) {
						props = handler.getBlockBuilderCallback().apply(material, props);
					}
					return CAPI.make(switch (bearer.getBlockType()) {
						case DEFAULT -> new MaterialOreBlock(props, material, handler);
						case PILLAR -> new MaterialOreRotatedPillarBlock(props, material, handler);
					}, block -> {
						Conductance.MATERIALS.register(material, handler, block);
					});
				}, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).mapColor(bearer.getMapColor()).sound(bearer.getSoundType()));
				ConductanceBlocks.ITEMS.registerItem(name, props -> {
					if (handler.getBlockItemBuilderCallback() != null) {
						props = handler.getBlockItemBuilderCallback().apply(material, props);
					}
					return CAPI.make(new MaterialOreBlockItem(holder.value(), props.useBlockDescriptionPrefix()), item -> {
						Conductance.MATERIALS.register(material, handler, item);
					});
				});
			});
		if (material.hasTrait(NCMaterialTraits.WIRE)) {
			for (final WireType wireType : WireType.values()) {
				final String name = wireType.getHandler().getUnlocalizedName(material);
				final DeferredBlock<WireBlock> holder = ConductanceBlocks.REGISTRY.registerBlock(name, props -> {
					if (wireType.getHandler().getBlockBuilderCallback() != null) {
						props = wireType.getHandler().getBlockBuilderCallback().apply(material, props);
					}
					return CAPI.make(new WireBlock(props, material, wireType), block -> {
						Conductance.MATERIALS.register(material, wireType.getHandler(), block);
						WireRegistry.register(wireType, material, block);
					});
				}, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE));
				ConductanceBlocks.ITEMS.registerItem(name, props -> {
					if (wireType.getHandler().getBlockItemBuilderCallback() != null) {
						props = wireType.getHandler().getBlockItemBuilderCallback().apply(material, props);
					}
					return CAPI.make(new WireBlockItem(holder.value(), props.useBlockDescriptionPrefix()), item -> {
						Conductance.MATERIALS.register(material, wireType.getHandler(), item);
					});
				});
			}
		}
	}

	private static void generateTiered() {
		NCBlocks.MACHINE_CASING = CAPI.tiers().newMap(tier ->
			ConductanceBlocks.REGISTRY.registerBlock(TieredItemType.MACHINE_CASING.getUnlocalizedName(tier), props -> CAPI.make(new TieredBlock(props, TieredItemType.MACHINE_CASING, tier), block -> {
				CreativeTabHelper.addToTab(block, CreativeTabHelper.Tabs.GENERAL);
			}))
		);
	}

	private static void generateCoils() {
		NCBlocks.COILS = Collections.unmodifiableMap(CAPI.make(new IdentityHashMap<>(), map -> {
			for (final CoilBlockType coilBlockType : CAPI.regs().coilBlockTypes()) {
				final String name = coilBlockType.getId().getPath() + "_coil_block";
				map.put(coilBlockType, CAPI.make(ConductanceBlocks.REGISTRY.registerBlock(name, props -> new CoilBlock(props, coilBlockType)), block -> {
					ConductanceBlocks.ITEMS.registerItem(name, props -> new CoilBlockItem(block.value(), props));
					CreativeTabHelper.addToTab(block, CreativeTabHelper.Tabs.GENERAL);
				}));
			}
		}));
	}

	@EventListener(priority = -100)
	private static void addBlockTranslations(final AddTranslationEvent event) {
		Conductance.MATERIALS.getBlockTable().rowMap().forEach((material, map) -> map.forEach((handler, block) -> {
			if (material == null || handler == null) {
				return;
			}
			handler.getGroupTagsAndTranslators(BuiltInRegistries.BLOCK, material).forEach((tagKey, translator) -> {
				if (translator != null) {
					final String translation = translator.translate(material);
					if (translation != null) {
						event.add(tagKey, translation.formatted(material.getName()));
					}
				}
			});
			handler.getEntryTagsAndTranslators(BuiltInRegistries.BLOCK, material).forEach((tagKey, translator) -> {
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
	private static void addBlockModels(final AddRuntimeModelEvent event) {
		Conductance.MATERIALS.getBlockTable().rowMap().forEach((material, map) -> map.forEach((handler, block) -> {
			if (material == null || handler == null || block == null) {
				return;
			}
			final ResourceLocation customTexture = CAPI.resourceFinder().getCustomMaterialTexture(material, handler.getTextureType());
			if (customTexture != null) {
				event.addBlockState(block, b -> b.simple(b2 -> b2.model(block)));
				event.addBlockModel(block, b -> b.parent(Conductance.id("block/cube_all")).particle(customTexture).renderType("cutout_mipped"));
				event.addItemModelDelegate(block);
			} else {
				final ResourceLocation model = CAPI.resourceFinder().getMaterialBlockModel(material.getTextureSet(), handler.getTextureType(), null, null).value();
				event.addBlockState(block, b -> b.simple(b2 -> {
					b2.model(model);
				}));
				event.addItemsModel(block.asItem(), b -> b.model(model, b2 -> b2.tints(tints -> {
					if (!material.getColor().hasMultipleColors()) {
						tints.constant(material.getColor().getCurrentColor());
					} else {
						tints.custom(MaterialColorTintSource.ID, json -> json.addProperty("default", -1));
					}
				})));
			}
		}));
		Conductance.MATERIALS.getBlockTable().rowMap().forEach((material, map) -> map.forEach((handler, block) -> {
			if (material == null || handler == null || block == null || handler.getOreBearer() == null) {
				return;
			}
			switch (handler.getOreBearer().getBlockType()) {
				case DEFAULT -> event.addBlockState(block, b -> b.simple(b2 -> b2.model(block)));
				case PILLAR -> event.addBlockState(block, b -> b.variants(variants -> {
					variants.variant(RotatedPillarBlock.AXIS, Direction.Axis.X).model(block).x(90).y(90);
					variants.variant(RotatedPillarBlock.AXIS, Direction.Axis.Y).model(block);
					variants.variant(RotatedPillarBlock.AXIS, Direction.Axis.Z).model(block).x(90);
				}));
			}
			final ResourceLocation oreTexture = CAPI.resourceFinder().getMaterialTexture(material.getTextureSet(), Conductance.id("ore"), null, null).value();
			final boolean emissive = Objects.requireNonNull(material.getTrait(NCMaterialTraits.ORE), "Should never happen").isEmissive();
			event.addBlockModel(block, builder -> builder.parent("block/cube")
				.renderType("cutout")
				.particle(oreTexture)
				.composite(composite -> composite
					.child("bearer", child -> child.parent(handler.getOreBearer().getBearingBlockModel()))
					.child("ore_overlay", child -> child.parent("block/block")
						.particle(oreTexture)
						.element(element -> {
							element.from(0, 0, 0).to(16, 16, 16);
							if (emissive) {
								element.shade(false).lightEmission(15);
							}
							element.faces((f, b) -> b.particle().neoforgeData(b2 -> b2.color(material.getColor().getCurrentColor())), true);
						})
					)
				)
			);
			event.addItemModelDelegate(block);
		}));
		ConductanceBlocks.SIMPLE_BLOCKS.forEach((blockHolder, texture) -> {
			event.addBlockState(blockHolder.value(), b -> b.simple(b2 -> b2.model(blockHolder.value())));
			event.addBlockModel(blockHolder.value(), b -> b.parent(Conductance.id("block/cube_all")).particle(texture));
			event.addItemModelDelegate(blockHolder.value());
		});
		ConductanceBlocks.ACTIVE_BLOCKS.forEach((blockHolder, texture) -> {
			final ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(blockHolder.value());
			event.addBlockState(blockHolder.value(), b -> b.variants(b2 -> {
				b2.variant(NCBlockStateProperties.ACTIVE, false).model(blockId.withPrefix("block/"));
				b2.variant(NCBlockStateProperties.ACTIVE, true).model(blockId.withPath(current -> "block/" + current + "_active"));
			}));
			event.addBlockModel(blockId, b -> b.parent(Conductance.id("block/cube_all")).particle(texture));
			event.addBlockModel(blockId.withSuffix("_active"), b -> b.parent(Conductance.id("block/cube_all")).particle(texture.withSuffix("_active")));
			event.addItemModelDelegate(blockHolder.value());
		});
		NCBlocks.MACHINE_CASING.forEach((tier, block) -> {
			event.addBlockState(block.value(), b -> b.simple(b2 -> b2.model(block.value())));
			event.addBlockModel(block.value(), b -> b.parent(Conductance.id("block/cube_all")).particle(Conductance.id("block/casing/machine_%s".formatted(tier.getId().getPath()))));
			event.addItemModelDelegate(block.value());
		});
		NCBlocks.COILS.forEach((coil, block) -> {
			event.addBlockState(block.value(), b -> b.simple(b2 -> b2.model(block.value())));
			event.addBlockModel(block.value(), b -> b.renderType("cutout_mipped").particle(Conductance.id("block/coil_block")).texture("overlay", Conductance.id("block/coil_block_overlay"))
				.element(b2 -> b2.faces((side, face) -> face.particle().neoforgeData(neo -> neo.color(coil.getColor())), true))
				.element(b2 -> b2.faces((side, face) -> face.texture("overlay"), true))
			);
			event.addItemModelDelegate(block.value());
		});
	}

	private static void handleMaterialBlockColors(final RegisterColorHandlersEvent.Block event) {
		CAPI.make(ConductanceBlocks.REGISTRY.getEntries().stream().map(DeferredHolder::get).filter(block -> block instanceof MaterialBlock).toArray(Block[]::new), blocks -> {
			event.register((blockState, blockAndTintGetter, blockPos, i) -> {
				final MaterialBlock block = (MaterialBlock) blockState.getBlock();
				return i == 0 ? block.getMaterial().getColor().getCurrentColor() : -1;
			}, blocks);
		});
		event.register((blockState, blockAndTintGetter, blockPos, i) -> {
			final WireBlock block = (WireBlock) blockState.getBlock();
			return i == 0 ? block.getMaterial().getColor().getCurrentColor() : -1;
		}, WireRegistry.getAllBlocks());
	}

	private static void handleCoilColors(final RegisterColorHandlersEvent.Block event) {
		event.register((blockState, blockAndTintGetter, blockPos, i) -> {
			final CoilBlock block = (CoilBlock) blockState.getBlock();
			return i == 0 ? block.getType().getColor() : -1;
		}, NCBlocks.COILS.values().stream().map(Holder::value).toArray(Block[]::new));
	}

	private static void attachCapabilities(final RegisterCapabilitiesEvent event) {
		for (final WireBlock wireBlock : WireRegistry.getAllBlocks()) {
			event.registerBlock(NCCapabilities.ENERGY_HANDLER_BLOCK, (level, pos, state, blockEntity, side) -> {
				if (blockEntity instanceof final WireBlockEntity wireBlockEntity) {
					return wireBlockEntity.getEnergyHandler(side);
				}
				return null;
			}, wireBlock);
		}
		event.registerBlock(Capabilities.FluidHandler.BLOCK, (level, pos, state, blockEntity, context) -> {
			if (blockEntity instanceof final CreativeTankBlockEntity creativeTankBlockEntity) {
				return creativeTankBlockEntity.getFluidHandler();
			}
			return null;
		}, NCBlocks.CREATIVE_TANK.value());
	}

	private ConductanceBlocks() {
	}
}
