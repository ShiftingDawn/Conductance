package conductance.content.item;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import lombok.Getter;
import lombok.experimental.Accessors;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;

@Getter
@Accessors(fluent = true)
public class MaterialItem extends Item {

	private final Material material;
	private final TaggedMaterialSet set;

	public MaterialItem(Properties properties, Material material, TaggedMaterialSet set) {
		super(properties);
		this.material = material;
		this.set = set;
	}

	@OnlyIn(Dist.CLIENT)
	public static ItemColor handleColorTint() {
		return (stack, tintIndex) -> {
			if ((tintIndex == 0 || tintIndex == 1) && stack.getItem() instanceof final MaterialItem materialItem) {
				return materialItem.material.getTintColor(tintIndex);
			}
			return -1;
		};
	}
}
