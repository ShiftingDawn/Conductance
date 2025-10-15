package conductance.init.fluid;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;

public final class MaterialBucketItem extends BucketItem {

	public MaterialBucketItem(final Fluid content, final Properties properties, final Material material, final MaterialGenerationHandler handler) {
		super(content, properties.component(DataComponents.ITEM_NAME, MaterialBucketItem.makeItemName(material, handler)));
	}

	private static Component makeItemName(final Material material, final MaterialGenerationHandler handler) {
		final Component fluidName = Component.translatable(handler.makeDescriptionId(material), Component.translatable(material.getDescriptionId()));
		return Component.translatable(handler.getDescriptionId() + ".bucket", fluidName);
	}
}
