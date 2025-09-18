package conductance.init.fluid;

import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;

public final class MaterialBucketItem extends BucketItem {

	private final Material material;

	public MaterialBucketItem(final Fluid content, final Properties properties, final Material material, final MaterialGenerationHandler handler) {
		super(content, properties.component(DataComponents.ITEM_NAME, MaterialBucketItem.makeItemName(material, handler)));
		this.material = material;
	}

	private static Component makeItemName(final Material material, final MaterialGenerationHandler handler) {
		final Component fluidName = Component.translatable(handler.makeDescriptionId(material), Component.translatable(material.getDescriptionId()));
		return Component.translatable(handler.getDescriptionId() + ".bucket", fluidName);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void appendHoverText(final ItemStack stack, final TooltipContext context, final TooltipDisplay tooltipDisplay, final Consumer<Component> tooltipAdder, final TooltipFlag flag) {
		tooltipAdder.accept(Component.literal(this.material.getChemicalFormula()).withStyle(ChatFormatting.AQUA));
		final FluidType type = this.content.getFluidType();
		tooltipAdder.accept(Component.translatable("tooltip.material_bucket.temperature", type.getTemperature()));
		tooltipAdder.accept(Component.translatable("tooltip.material_bucket.density", type.getDensity()));
		tooltipAdder.accept(Component.translatable("tooltip.material_bucket.viscosity", type.getViscosity()));
	}
}
