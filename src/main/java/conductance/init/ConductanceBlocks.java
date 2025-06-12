package conductance.init;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import conductance.api.CAPI;
import conductance.api.NCBlocks;
import conductance.api.NCMaterialTraits;
import conductance.api.material.MaterialOreType;
import conductance.core.material.MaterialRegistryImpl;
import conductance.core.material.TaggedMaterialSetImpl;
import conductance.init.block.HorizontalBlock;
import conductance.init.block.MaterialBlock;
import conductance.init.block.MaterialBlockItem;
import conductance.init.block.MaterialOreBlock;
import conductance.init.block.MaterialOreBlockItem;
import conductance.init.block.MaterialOreRotatedPillarBlock;
import conductance.init.block.SimpleDynamicBlock;
import conductance.init.block.WireBlock;
import conductance.init.block.WireBlockItem;
import conductance.init.item.RenderedBlockItem;
import conductance.lib.pipenet.WireRegistry;
import conductance.lib.pipenet.WireType;
import static conductance.core.register.RegisterCore.REGISTRATE;

@SuppressWarnings("removal")
public final class ConductanceBlocks {

	public static void init() {
		ConductanceBlocks.generatedTiered();

		NCBlocks.CASING_STEEL = ConductanceBlocks.machineCasingBlock("steel");
		NCBlocks.CASING_INVAR = ConductanceBlocks.machineCasingBlock("invar");
		NCBlocks.CASING_ALUMINIUM = ConductanceBlocks.machineCasingBlock("aluminium");

		CAPI.regs().materials().forEach(material -> CAPI.regs().materialTaggedSets().values().stream().filter(set -> set.canGenerateBlock(material)).forEach(set -> {
			final String name = set.getUnlocalizedName(material);
			final BlockBuilder<MaterialBlock, Registrate> blockBuilder = REGISTRATE.block(name, props -> new MaterialBlock(props, material, set))
					.initialProperties(() -> Blocks.IRON_BLOCK);
			if (!set.shouldOccludeBlocks()) {
				blockBuilder.properties(BlockBehaviour.Properties::noOcclusion)
						.addLayer(() -> RenderType::cutoutMipped);
			}
			blockBuilder.color(() -> MaterialBlock::handleColorTint)
					.item(MaterialBlockItem::new)
					.model(NonNullBiConsumer.noop())
					.color(() -> MaterialBlockItem::handleColorTint)
					.build();
			if (((TaggedMaterialSetImpl) set).getBlockGeneratorCallback() != null) {
				((TaggedMaterialSetImpl) set).getBlockGeneratorCallback().accept(material, blockBuilder);
			}
			MaterialRegistryImpl.INSTANCE.register(set, material, blockBuilder.register());
		}));
		CAPI.regs().materials().values().stream().filter(material -> material.has(NCMaterialTraits.ORE)).forEach(material ->
				CAPI.regs().materialTaggedSets().values().stream().filter(set -> set.getOreType() != null).forEach(set -> {
					final MaterialOreType oreType = set.getOreType();
					final String name = set.getUnlocalizedName(material);
					final BlockBuilder<? extends Block, Registrate> blockBuilder = REGISTRATE.block(name, props -> switch (oreType.getOreBlockType()) {
								case DEFAULT -> new MaterialOreBlock(props, material, set, oreType);
								case PILLAR -> new MaterialOreRotatedPillarBlock(props, material, set, oreType);
							})
							.initialProperties(() -> Blocks.STONE)
							.properties(props -> props.mapColor(oreType.getMapColor()).sound(oreType.getSoundType()))
							.color(() -> MaterialOreBlock::handleColorTint)
							.item(MaterialOreBlockItem::new)
							.model(NonNullBiConsumer.noop())
							.color(() -> MaterialOreBlockItem::handleColorTint)
							.build();
					MaterialRegistryImpl.INSTANCE.register(set, material, blockBuilder.register());
				})
		);
		ConductanceBlocks.generateWires();
	}

	private static void generatedTiered() {
		NCBlocks.MACHINE_CASING = CAPI.tiers().newMap(tier -> REGISTRATE.block("%s_machine_casing".formatted(tier.getRegistryKey()), HorizontalBlock::new)
				.initialProperties(() -> Blocks.IRON_BLOCK)
				.item()
				.model(NonNullBiConsumer.noop())
				.build()
				.register());
	}

	private static BlockEntry<SimpleDynamicBlock> machineCasingBlock(final String name) {
		return REGISTRATE.block("%s_machine_casing".formatted(name), props -> new SimpleDynamicBlock(props, "casing/%s".formatted(name)))
				.initialProperties(() -> Blocks.IRON_BLOCK)
				.item(RenderedBlockItem::new)
				.model(NonNullBiConsumer.noop())
				.build()
				.register();
	}

	private static void generateWires() {
		CAPI.regs().materials().values().stream().filter(mat -> mat.has(NCMaterialTraits.WIRE)).forEach(material -> {
			for (final WireType wireType : WireType.values()) {
				final String name = wireType.getMaterialTaggedSet().getUnlocalizedName(material);
				final BlockEntry<WireBlock> block = REGISTRATE.block(name, props -> new WireBlock(props, wireType, material))
						.initialProperties(() -> Blocks.IRON_BLOCK)
						.properties(props -> props.dynamicShape().noOcclusion())
						.addLayer(() -> RenderType::cutoutMipped)
						.color(() -> WireBlock::handleColorTint)
						.item(WireBlockItem::new)
						.model(NonNullBiConsumer.noop())
						.color(() -> WireBlockItem::handleColorTint)
						.build()
						.register();
				MaterialRegistryImpl.INSTANCE.register(wireType.getMaterialTaggedSet(), material, block);
				WireRegistry.register(wireType, material, block);
			}
		});
	}

	private ConductanceBlocks() {
	}
}
