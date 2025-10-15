package conductance.init.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.FuelValues;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.NCMaterialProps;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.util.LazyInt;

public final class MaterialItem extends Item {

	private final @Getter Material material;
	private final @Getter MaterialGenerationHandler handler;
	private final LazyInt burnValue;

	public MaterialItem(final Properties properties, final Material material, final MaterialGenerationHandler handler) {
		super(properties.component(DataComponents.ITEM_NAME, Component.translatable(handler.makeDescriptionId(material), Component.translatable(material.getDescriptionId()))));
		this.material = material;
		this.handler = handler;
		this.burnValue = LazyInt.of(() -> {
			final int value = this.material.getProp(NCMaterialProps.BURN_TIME, 0);
			if (value == 0) {
				return 0;
			}
			final double factor = (double) handler.getUnitValue(material) / (double) CAPI.UNIT;
			return (int) (value * factor);
		});
	}

	@Override
	public int getBurnTime(final ItemStack itemStack, @Nullable final RecipeType<?> recipeType, final FuelValues fuelValues) {
		return this.burnValue.getAsInt();
	}
}
