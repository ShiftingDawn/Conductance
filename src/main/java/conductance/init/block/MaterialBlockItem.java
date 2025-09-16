package conductance.init.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.FuelValues;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.NCMaterialProps;
import conductance.api.util.LazyInt;

public final class MaterialBlockItem extends BlockItem {

	private final LazyInt burnValue;

	public MaterialBlockItem(final MaterialBlock block, final Properties properties) {
		super(block, properties);
		this.burnValue = LazyInt.of(() -> {
			final int value = block.getMaterial().getProp(NCMaterialProps.BURN_TIME, 0);
			if (value == 0) {
				return 0;
			}
			final double factor = (double) block.getHandler().getUnitValue() / (double) CAPI.UNIT;
			return (int) (value * factor);
		});
	}

	@Override
	public int getBurnTime(final ItemStack itemStack, @Nullable final RecipeType<?> recipeType, final FuelValues fuelValues) {
		return this.burnValue.getAsInt();
	}
}
