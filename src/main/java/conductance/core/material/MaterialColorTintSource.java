package conductance.core.material;

import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;
import conductance.Conductance;
import conductance.init.block.MaterialBlockItem;
import conductance.init.item.MaterialItem;

public record MaterialColorTintSource(int defaultColor) implements ItemTintSource {

	public static final ResourceLocation ID = Conductance.id("material");
	public static final MapCodec<MaterialColorTintSource> MAP_CODEC = ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").xmap(MaterialColorTintSource::new, MaterialColorTintSource::defaultColor);

	@Override
	public int calculate(final ItemStack itemStack, @Nullable final ClientLevel clientLevel, @Nullable final LivingEntity livingEntity) {
		if (itemStack.getItem() instanceof final MaterialItem materialItem) {
			return materialItem.getMaterial().getColor().getCurrentColor();
		} else if (itemStack.getItem() instanceof final MaterialBlockItem materialBlockItem) {
			return materialBlockItem.getBlock().getMaterial().getColor().getCurrentColor();
		}
		return -1;
	}

	@Override
	public MapCodec<? extends ItemTintSource> type() {
		return MaterialColorTintSource.MAP_CODEC;
	}
}
