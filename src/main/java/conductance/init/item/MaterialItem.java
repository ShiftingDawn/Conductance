package conductance.init.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.FuelValues;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCMaterialProps;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;

public final class MaterialItem extends Item {

	private final @Getter Material material;
	private final @Getter MaterialGenerationHandler handler;

	public MaterialItem(final Properties properties, final Material material, final MaterialGenerationHandler handler) {
		super(properties);
		this.material = material;
		this.handler = handler;
	}

	@Override
	public int getBurnTime(final ItemStack itemStack, @Nullable final RecipeType<?> recipeType, final FuelValues fuelValues) {
		return this.material.getProp(NCMaterialProps.BURN_TIME, 0);
	}
}
