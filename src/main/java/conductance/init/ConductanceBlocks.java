package conductance.init;

import net.minecraft.world.level.block.Blocks;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import conductance.api.CAPI;
import conductance.content.block.MaterialBlock;
import conductance.content.block.MaterialBlockItem;
import conductance.core.apiimpl.MaterialTaggedSet;
import static conductance.core.apiimpl.ApiBridge.REGISTRATE;

public final class ConductanceBlocks {

	public static void init() {
		CAPI.REGS.materials().forEach(material -> CAPI.REGS.materialTaggedSets().values().stream().filter(set -> set.canGenerateBlock(material)).forEach(set -> {
			final String name = set.getUnlocalizedName(material);
			final BlockBuilder<MaterialBlock, Registrate> blockBuilder = REGISTRATE.block(name, props -> new MaterialBlock(props, material, set)).initialProperties(() -> Blocks.IRON_BLOCK).setData(ProviderType.BLOCKSTATE, NonNullBiConsumer.noop()).color(() -> MaterialBlock::handleColorTint).item(MaterialBlockItem::new).model(NonNullBiConsumer.noop()).color(() -> MaterialBlockItem::handleColorTint).build();
			if (((MaterialTaggedSet) set).getBlockGeneratorCallback() != null) {
				((MaterialTaggedSet) set).getBlockGeneratorCallback().accept(material, blockBuilder);
			}
			CAPI.MATERIALS.register(set, material, blockBuilder.register());
		}));
	}
}
