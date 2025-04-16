package conductance.runtimepack.client;

import java.util.HashMap;
import net.minecraft.Util;
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
import conductance.core.machine.MachineTypeImpl;

final class MachineModelHandler {

	public static void reload() {
		CAPI.regs().machines().forEach(machineType -> {
			final Block block = machineType.getBlock().get();
			final ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
			final RuntimeModelProvider modelProvider = ((MachineTypeImpl<?>) machineType).getModelProvider().apply(machineType);

			final HashMap<ResourceLocation, BlockModelBuilderImpl> blockModelBuilders = new HashMap<>();
			modelProvider.createBlockModel(
					blockId,
					loc -> Util.make(new BlockModelBuilderImpl(), builder -> blockModelBuilders.put(loc, builder)),
					RuntimeResourcePack::addBlockModel
			);
			blockModelBuilders.forEach((loc, builder) -> RuntimeResourcePack.addBlockModel(loc, builder.build()));

			final ItemModelBuilderImpl itemModelBuilder = new ItemModelBuilderImpl();
			if (!modelProvider.createItemModel(blockId, itemModelBuilder)) {
				RuntimeResourcePack.addItemModel(BuiltInRegistries.ITEM.getKey(block.asItem()), new DelegatedModel(ModelLocationUtils.getModelLocation(block)));
			} else {
				RuntimeResourcePack.addItemModel(blockId, itemModelBuilder.build());
			}

			final HashMap<ResourceLocation, BlockStateBuilderImpl> blockStateBuilders = new HashMap<>();
			modelProvider.createBlockState(
					blockId,
					loc -> Util.make(new BlockStateBuilderImpl(), builder -> blockStateBuilders.put(loc, builder)),
					blockId.withPrefix("block/"),
					RuntimeResourcePack::addBlockState
			);
			blockStateBuilders.forEach((loc, builder) -> RuntimeResourcePack.addBlockState(loc, builder.build()));
		});
	}

	private MachineModelHandler() {
	}
}
