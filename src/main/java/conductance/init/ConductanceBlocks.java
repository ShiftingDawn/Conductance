package conductance.init;

import java.util.Objects;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import conductance.api.CAPI;
import conductance.api.NCMaterialTraits;
import conductance.api.material.Material;
import conductance.api.material.MaterialOreBearer;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.api.resource.event.AddTranslationEvent;
import conductance.Conductance;
import conductance.init.block.MaterialBlock;
import conductance.init.block.MaterialBlockItem;
import conductance.init.block.MaterialOreBlock;
import conductance.init.block.MaterialOreBlockItem;
import conductance.init.block.MaterialOreRotatedPillarBlock;

@ConductancePluginListener(modid = Conductance.MODID)
public final class ConductanceBlocks {

	private static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(Conductance.MODID);
	private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Conductance.MODID);

	public static void initialize(final IEventBus modEventBus) {
		ConductanceBlocks.REGISTRY.register(modEventBus);
		ConductanceBlocks.ITEMS.register(modEventBus);
		CAPI.regs().materials().forEach(ConductanceBlocks::generateMaterial);
		modEventBus.addListener(RegisterColorHandlersEvent.Block.class, ConductanceBlocks::handleMaterialBlockColors);
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
					return Util.make(new MaterialBlock(props, material, handler), block -> {
						Conductance.MATERIALS.register(material, handler, block);
					});
				}, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE));
				ConductanceBlocks.ITEMS.registerItem(name, props -> {
					if (handler.getBlockItemBuilderCallback() != null) {
						props = handler.getBlockItemBuilderCallback().apply(material, props);
					}
					return Util.make(new MaterialBlockItem(holder.value(), props.useBlockDescriptionPrefix()), item -> {
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
					return Util.make(switch (bearer.getBlockType()) {
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
					return Util.make(new MaterialOreBlockItem(holder.value(), props.useBlockDescriptionPrefix()), item -> {
						Conductance.MATERIALS.register(material, handler, item);
					});
				});
			});
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
			final ResourceLocation model = CAPI.resourceFinder().getMaterialBlockModel(material.getTextureSet(), handler.getTextureType(), null, null).value();
			event.addBlockState(block, b -> b.simple(b2 -> {
				b2.model(model);
			}));
			event.addItemsModel(block.asItem(), b -> b.model(model, b2 -> {
				b2.tints(tints -> tints.constant(material.getColor()));
			}));
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
						.element(element -> element
							.from(0, 0, 0)
							.to(16, 16, 16)
							.shade(!emissive)
							.faces((f, b) -> b.particle().neoforgeData(b2 -> b2.color(material.getColor())), true)
						)
					)
				)
			);
			event.addItemModelDelegate(block);
		}));
	}

	private static void handleMaterialBlockColors(final RegisterColorHandlersEvent.Block event) {
		final Block[] materialBlocks = ConductanceBlocks.REGISTRY.getEntries().stream().map(DeferredHolder::get)
			.filter(block -> block instanceof MaterialBlock)
			.toArray(Block[]::new);
		event.register((blockState, blockAndTintGetter, blockPos, i) -> {
			final MaterialBlock block = (MaterialBlock) blockState.getBlock();
			return i == 0 ? block.getMaterial().getColor() : -1;
		}, materialBlocks);
	}

	private ConductanceBlocks() {
	}
}
