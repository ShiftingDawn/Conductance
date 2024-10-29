package conductance.fluid;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import com.tterrag.registrate.util.entry.RegistryEntry;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.item.IConductanceItem;
import conductance.init.ConductanceCreativeTabs;

public class MaterialBucketItem extends BucketItem implements IConductanceItem {

	public MaterialBucketItem(final Fluid content, final Properties properties) {
		super(content, properties);
	}

	@Override
	public RegistryEntry<CreativeModeTab, CreativeModeTab> getCreativeTab() {
		return ConductanceCreativeTabs.MATERIAL_FLUIDS;
	}

	@Override
	public boolean emptyContents(@Nullable final Player player, final Level level, final BlockPos pos, @Nullable final BlockHitResult result, @Nullable final ItemStack container) {
		return false;
	}

	@Override
	public Component getDescription() {
		return CAPI.translations().makeLocalizedName(this);
	}

	@Override
	public Component getName(final ItemStack stack) {
		return this.getDescription();
	}

	@OnlyIn(Dist.CLIENT)
	public static ItemColor handleColorTint() {
		return (stack, tintIndex) -> {
			if (tintIndex == 1 && stack.getItem() instanceof final MaterialBucketItem bucket) {
				return ((MaterialFluidType) bucket.content.getFluidType()).getMaterial().getTintColor(tintIndex);
			}
			return -1;
		};
	}
}
