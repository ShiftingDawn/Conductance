package conductance.core.tier;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import conductance.api.CAPI;
import conductance.api.NCBlocks;
import conductance.api.NCItems;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.Conductance;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

@ConductancePluginListener(modid = Conductance.MODID)
final class TierModelHandlers {

	@EventListener(priority = -100)
	private static void onAddRuntimeModels(final AddRuntimeModelEvent event) {
		NCItems.TIERED.rowMap().forEach((itemType, column) -> column.forEach((tier, itemEntry) -> {
			event.addItemModel(itemEntry.getId(), builder -> {
				if (CAPI.resourceFinder().isItemTextureValid(itemEntry.getId())) {
					builder.layer0(CAPI.resourceFinder().getItemTexture(itemEntry.getId()));
				} else {
					builder.layer0(Conductance.id("item/tier/%s/base".formatted(itemType)));
					builder.layer1(Conductance.id("item/tier/%s/overlay".formatted(itemType)));
				}
			});
		}));
		NCBlocks.MACHINE_CASING.forEach((tier, blockEntry) -> {
			final ResourceLocation model = blockEntry.getId().withPath("block/machine_casing_tiered_%s".formatted(tier.getRegistryKey()));
			event.addBlockState(blockEntry.getId(), builder -> builder.variants(variants -> {
				variants.variant(HORIZONTAL_FACING, Direction.NORTH).model(model);
				variants.variant(HORIZONTAL_FACING, Direction.EAST).model(model).y(90);
				variants.variant(HORIZONTAL_FACING, Direction.SOUTH).model(model).y(180);
				variants.variant(HORIZONTAL_FACING, Direction.WEST).model(model).y(270);
			}));
			event.addItemModel(BuiltInRegistries.ITEM.getKey(blockEntry.asItem()), builder -> builder.parent(model));
		});
	}

	private TierModelHandlers() {
	}
}
