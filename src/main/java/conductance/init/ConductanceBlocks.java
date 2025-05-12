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
import conductance.block.WireBlock;
import conductance.block.WireBlockItem;
import conductance.block.MaterialBlock;
import conductance.block.MaterialBlockItem;
import conductance.block.MaterialOreBlock;
import conductance.block.MaterialOreBlockItem;
import conductance.block.MaterialOreRotatedPillarBlock;
import conductance.block.SimpleDynamicBlock;
import conductance.core.apiimpl.ApiBridge;
import conductance.core.apiimpl.MaterialOreTypeImpl;
import conductance.core.apiimpl.TaggedMaterialSetImpl;
import conductance.core.pipenet.WireRegistry;
import conductance.core.pipenet.WireType;
import conductance.item.RenderedBlockItem;

@SuppressWarnings("removal")
public final class ConductanceBlocks {

	public static void init() {
		NCBlocks.CASING_STEEL = ConductanceBlocks.machineCasingBlock("steel");
		NCBlocks.CASING_INVAR = ConductanceBlocks.machineCasingBlock("invar");
		NCBlocks.CASING_ALUMINIUM = ConductanceBlocks.machineCasingBlock("aluminium");

		CAPI.regs().materials().forEach(material -> CAPI.regs().materialTaggedSets().values().stream().filter(set -> set.canGenerateBlock(material)).forEach(set -> {
			final String name = set.getUnlocalizedName(material);
			final BlockBuilder<MaterialBlock, Registrate> blockBuilder = ApiBridge.getRegistrate().block(name, props -> new MaterialBlock(props, material, set))
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
			CAPI.materials().register(set, material, blockBuilder.register());
		}));
		CAPI.regs().materials().values().stream().filter(material -> material.hasTrait(NCMaterialTraits.ORE)).forEach(material ->
				CAPI.regs().materialTaggedSets().values().stream().filter(set -> set.getOreType() != null).forEach(set -> {
					final MaterialOreType oreType = set.getOreType();
					final String name = set.getUnlocalizedName(material);
					final BlockBuilder<? extends Block, Registrate> blockBuilder = ApiBridge.getRegistrate().block(name, props -> switch (((MaterialOreTypeImpl) oreType).getOreBlockType()) {
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
					CAPI.materials().register(set, material, blockBuilder.register());
				})
		);
		ConductanceBlocks.generateWires();
	}

	private static BlockEntry<SimpleDynamicBlock> machineCasingBlock(final String name) {
		return ApiBridge.getRegistrate().block("%s_machine_casing".formatted(name), props -> new SimpleDynamicBlock(props, "casing/%s".formatted(name)))
				.initialProperties(() -> Blocks.IRON_BLOCK)
				.item(RenderedBlockItem::new)
				.model(NonNullBiConsumer.noop())
				.build()
				.register();
	}

	private static void generateWires() {
		CAPI.regs().materials().values().stream().filter(mat -> mat.hasTrait(NCMaterialTraits.WIRE)).forEach(material -> {
			for (final WireType wireType : WireType.values()) {
				final String name = wireType.getMaterialTaggedSet().getUnlocalizedName(material);
				final BlockEntry<WireBlock> block = ApiBridge.getRegistrate().block(name, props -> new WireBlock(props, wireType, material))
						.initialProperties(() -> Blocks.IRON_BLOCK)
						.properties(props -> props.dynamicShape().noOcclusion())
						.addLayer(() -> RenderType::cutoutMipped)
						.color(() -> WireBlock::handleColorTint)
						.item(WireBlockItem::new)
						.model(NonNullBiConsumer.noop())
						.color(() -> WireBlockItem::handleColorTint)
						.build()
						.register();
				CAPI.materials().register(wireType.getMaterialTaggedSet(), material, block);
				WireRegistry.register(wireType, material, block);
			}
		});
	}

	private ConductanceBlocks() {
	}
}
