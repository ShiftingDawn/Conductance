package conductance.init;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import conductance.api.CAPI;
import conductance.api.NCMaterialTraits;
import conductance.block.CableBlock;
import conductance.block.CableBlockItem;
import conductance.block.MaterialBlock;
import conductance.block.MaterialBlockItem;
import conductance.block.MaterialOreBlock;
import conductance.block.MaterialOreBlockItem;
import conductance.block.MaterialOreRotatedPillarBlock;
import conductance.core.apiimpl.ApiBridge;
import conductance.core.apiimpl.MaterialOreTypeImpl;
import conductance.core.apiimpl.MaterialTaggedSet;
import conductance.core.pipenet.CableRegistry;
import conductance.core.pipenet.CableType;

public final class ConductanceBlocks {

	public static void init() {
		CAPI.regs().materials().forEach(material -> CAPI.regs().materialTaggedSets().values().stream().filter(set -> set.canGenerateBlock(material)).forEach(set -> {
			final String name = set.getUnlocalizedName(material);
			final BlockBuilder<MaterialBlock, Registrate> blockBuilder = ApiBridge.getRegistrate().block(name, props -> new MaterialBlock(props, material, set))
					.initialProperties(() -> Blocks.IRON_BLOCK)
					.setData(ProviderType.BLOCKSTATE, NonNullBiConsumer.noop())
					.color(() -> MaterialBlock::handleColorTint)
					.item(MaterialBlockItem::new)
					.model(NonNullBiConsumer.noop())
					.color(() -> MaterialBlockItem::handleColorTint)
					.build();
			if (((MaterialTaggedSet) set).getBlockGeneratorCallback() != null) {
				((MaterialTaggedSet) set).getBlockGeneratorCallback().accept(material, blockBuilder);
			}
			CAPI.materials().register(set, material, blockBuilder.register());
		}));
		CAPI.regs().materials().values().stream().filter(material -> material.hasTrait(NCMaterialTraits.ORE)).forEach(material -> CAPI.regs().materialOreTypes().forEach(oreType -> {
			final String name = "%s_%s_ore".formatted(oreType.getRegistryKey().getPath(), material.getRegistryKey().getPath());
			final BlockBuilder<? extends Block, Registrate> blockBuilder = ApiBridge.getRegistrate().block(name, props -> switch (((MaterialOreTypeImpl) oreType).getOreBlockType()) {
						case DEFAULT -> new MaterialOreBlock(props, material, oreType);
						case PILLAR -> new MaterialOreRotatedPillarBlock(props, material, oreType);
					})
					.initialProperties(() -> Blocks.STONE)
					.setData(ProviderType.BLOCKSTATE, NonNullBiConsumer.noop())
					.color(() -> MaterialOreBlock::handleColorTint)
					.item(MaterialOreBlockItem::new)
					.model(NonNullBiConsumer.noop())
					.color(() -> MaterialOreBlockItem::handleColorTint)
					.build();
			blockBuilder.register();
		}));
		ConductanceBlocks.generateCables();
	}

	@SuppressWarnings("removal")
	private static void generateCables() {
		CAPI.regs().materials().values().stream().filter(mat -> mat.hasTrait(NCMaterialTraits.CABLE)).forEach(material -> {
			for (final CableType cableType : CableType.values()) {
				if (!cableType.isCable() || !material.getTrait(NCMaterialTraits.CABLE).isSuperconductor()) {
					final String name = cableType.getMaterialTaggedSet().getUnlocalizedName(material);
					final BlockEntry<CableBlock> block = ApiBridge.getRegistrate().block(name, props -> new CableBlock(props, cableType, material))
							.initialProperties(() -> Blocks.IRON_BLOCK)
							.properties(props -> props.dynamicShape().noOcclusion())
							.blockstate(NonNullBiConsumer.noop())
							.addLayer(() -> RenderType::cutoutMipped)
							.color(() -> CableBlock::handleColorTint)
							.item(CableBlockItem::new)
							.model(NonNullBiConsumer.noop())
							.color(() -> CableBlockItem::handleColorTint)
							.build()
							.register();
					CAPI.materials().register(cableType.getMaterialTaggedSet(), material, block);
					CableRegistry.register(cableType, material, block);
				}
			}
		});
	}

	private ConductanceBlocks() {
	}
}
