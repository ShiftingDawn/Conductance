package conductance.init.block;

import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.FuelValues;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.NCMaterialProps;
import conductance.api.util.LazyInt;

public final class MaterialBlockItem extends BlockItem {

	private final LazyInt burnValue;

	public MaterialBlockItem(final MaterialBlock block, final Properties properties) {
		super(block, properties.component(DataComponents.ITEM_NAME, block.getName()));
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
	public MaterialBlock getBlock() {
		return (MaterialBlock) super.getBlock();
	}

	@Override
	public int getBurnTime(final ItemStack itemStack, @Nullable final RecipeType<?> recipeType, final FuelValues fuelValues) {
		return this.burnValue.getAsInt();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void appendHoverText(final ItemStack stack, final TooltipContext context, final TooltipDisplay tooltipDisplay, final Consumer<Component> tooltipAdder, final TooltipFlag flag) {
		tooltipAdder.accept(Component.literal(this.getBlock().getMaterial().getChemicalFormula()).withStyle(ChatFormatting.AQUA));
	}
}
