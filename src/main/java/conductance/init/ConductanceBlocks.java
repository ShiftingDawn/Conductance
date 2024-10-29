package conductance.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import conductance.api.CAPI;
import conductance.api.NCMaterialTraits;
import conductance.content.block.MaterialBlock;
import conductance.content.block.MaterialBlockItem;
import conductance.content.block.MaterialOreBlock;
import conductance.content.block.MaterialOreBlockItem;
import conductance.content.block.MaterialOreRotatedPillarBlock;
import conductance.core.apiimpl.MaterialOreTypeImpl;
import conductance.core.apiimpl.MaterialTaggedSet;
import static conductance.core.apiimpl.ApiBridge.REGISTRATE;

public final class ConductanceBlocks {

	public static void init() {
		CAPI.REGS.materials().forEach(material -> CAPI.REGS.materialTaggedSets().values().stream().filter(set -> set.canGenerateBlock(material)).forEach(set -> {
			final String name = set.getUnlocalizedName(material);
			final BlockBuilder<MaterialBlock, Registrate> blockBuilder = REGISTRATE.block(name, props -> new MaterialBlock(props, material, set)).initialProperties(() -> Blocks.IRON_BLOCK)
					.setData(ProviderType.BLOCKSTATE, NonNullBiConsumer.noop()).color(() -> MaterialBlock::handleColorTint).item(MaterialBlockItem::new).model(NonNullBiConsumer.noop())
					.color(() -> MaterialBlockItem::handleColorTint).build();
			if (((MaterialTaggedSet) set).getBlockGeneratorCallback() != null) {
				((MaterialTaggedSet) set).getBlockGeneratorCallback().accept(material, blockBuilder);
			}
			CAPI.MATERIALS.register(set, material, blockBuilder.register());
		}));
		CAPI.REGS.materials().values().stream().filter(material -> material.hasTrait(NCMaterialTraits.ORE)).forEach(material -> CAPI.REGS.materialOreTypes().forEach(oreType -> {
			final String name = "%s_%s_ore".formatted(oreType.getRegistryKey().getPath(), material.getRegistryKey().getPath());
			final BlockBuilder<? extends Block, Registrate> blockBuilder = REGISTRATE.block(name, props -> switch (((MaterialOreTypeImpl) oreType).getOreBlockType()) {
						case DEFAULT -> new MaterialOreBlock(props, material, oreType);
						case PILLAR -> new MaterialOreRotatedPillarBlock(props, material, oreType);
					}).initialProperties(() -> Blocks.STONE).setData(ProviderType.BLOCKSTATE, NonNullBiConsumer.noop()).color(() -> MaterialOreBlock::handleColorTint).item(MaterialOreBlockItem::new)
					.model(NonNullBiConsumer.noop()).color(() -> MaterialOreBlockItem::handleColorTint).build();
			blockBuilder.register();
		}));
	}
}
