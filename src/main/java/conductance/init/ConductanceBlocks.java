package conductance.init;

import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
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
import conductance.init.block.MaterialBlock;
import conductance.init.block.MaterialBlockItem;

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
					final DeferredBlock<MaterialBlock> holder = ConductanceBlocks.REGISTRY.registerBlock(name, props -> Util.make(new MaterialBlock(props, material, handler), block -> {
						Conductance.MATERIALS.register(material, handler, block);
					}));
					ConductanceBlocks.ITEMS.registerItem(name, props -> Util.make(new MaterialBlockItem(holder.value(), props.useBlockDescriptionPrefix()), item -> {
						Conductance.MATERIALS.register(material, handler, item);
					}));
				});
	}

	@EventListener(priority = -100)
	private static void addBlockTranslations(final AddTranslationEvent event) {
		ConductanceBlocks.REGISTRY.getEntries().stream().map(DeferredHolder::get).filter(block -> block instanceof MaterialBlock).forEach(block -> {
			final MaterialBlock materialBlock = (MaterialBlock) block;
			final String name = materialBlock.getHandler().getUnlocalizedName(materialBlock.getMaterial());
			event.add(materialBlock.getDescriptionId(), TextHelper.lowerUnderscoreToEnglish(name));
		});
		Conductance.MATERIALS.getBlockTable().rowMap().forEach((material, map) -> map.forEach((handler, block) -> {
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
		ConductanceBlocks.REGISTRY.getEntries().stream().map(DeferredHolder::get).filter(block -> block instanceof MaterialBlock).forEach(block -> {
			final MaterialBlock materialBlock = (MaterialBlock) block;
			final ResourceLocation model = CAPI.resourceFinder().getMaterialBlockModel(materialBlock.getMaterial().getTextureSet(), materialBlock.getHandler().getTextureType(), null, null).value();
			event.addBlockState(materialBlock, b -> b.simple(b2 -> {
				b2.model(model);
			}));
			event.addItemsModel(materialBlock.asItem(), b -> b.model(model, b2 -> {
				b2.tints(tints -> tints.constant(materialBlock.getMaterial().getColor()));
			}));
		});
	}

	private static void handleMaterialBlockColors(final RegisterColorHandlersEvent.Block event) {
		final Block[] materialBlocks = ConductanceBlocks.REGISTRY.getEntries().stream().map(DeferredHolder::get)
				.filter(block -> block instanceof MaterialBlock)
				.map(MaterialBlock.class::cast)
				.toArray(MaterialBlock[]::new);
		event.register((blockState, blockAndTintGetter, blockPos, i) -> {
			final MaterialBlock block = (MaterialBlock) blockState.getBlock();
			return i == 0 ? block.getMaterial().getColor() : -1;
		}, materialBlocks);
	}

	private ConductanceBlocks() {
	}
}
