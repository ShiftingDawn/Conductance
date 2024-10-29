package conductance.client.resourcepack;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import conductance.api.CAPI;
import conductance.api.resource.RuntimeModelProvider;
import conductance.core.machine.BlockModelBuilderImpl;
import conductance.core.machine.BlockStateBuilderImpl;
import conductance.core.machine.ItemModelBuilderImpl;
import conductance.core.machine.MetaBlockEntityTypeImpl;

final class MachineModelHandler {

	public static void reload() {
		CAPI.regs().metaBlockEntities().forEach(metaBlockEntityType -> {
			final Block block = metaBlockEntityType.getBlock().get();
			final ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
			final RuntimeModelProvider modelProvider = ((MetaBlockEntityTypeImpl<?>) metaBlockEntityType).getModelProvider();

			final BlockModelBuilderImpl blockModelBuilder = new BlockModelBuilderImpl();
			modelProvider.createBlockModel(blockId, blockModelBuilder);
			RuntimeResourcePack.addBlockModel(blockId, blockModelBuilder.build());

			final ItemModelBuilderImpl itemModelBuilder = new ItemModelBuilderImpl();
			if (!modelProvider.createItemModel(blockId, itemModelBuilder)) {
				RuntimeResourcePack.addItemModel(BuiltInRegistries.ITEM.getKey(block.asItem()), new DelegatedModel(ModelLocationUtils.getModelLocation(block)));
			} else {
				RuntimeResourcePack.addItemModel(blockId, itemModelBuilder.build());
			}

			final BlockStateBuilderImpl blockStateBuilder = new BlockStateBuilderImpl();
			modelProvider.createBlockState(blockId, blockStateBuilder, blockId.withPrefix("block/"));
			RuntimeResourcePack.addBlockState(blockId, blockStateBuilder.build());
		});
	}

	private MachineModelHandler() {
	}
}
