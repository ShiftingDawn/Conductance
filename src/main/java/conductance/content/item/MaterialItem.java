package conductance.content.item;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.NCMaterialTraits;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.material.traits.MaterialTraitDust;
import conductance.client.resourcepack.MaterialModelHandler;

@Getter
@Accessors(fluent = true)
public class MaterialItem extends Item {

	private final Material material;
	private final TaggedMaterialSet set;
	private final String unlocalizedName;

	public MaterialItem(Properties properties, Material material, TaggedMaterialSet set) {
		super(properties);
		this.material = material;
		this.set = set;
		this.unlocalizedName = "item.%s.%s".formatted(material.getRegistryKey().getNamespace(), set.getUnlocalizedName(material));
		if (CAPI.isClient()) {
			MaterialModelHandler.add(this, material, material.getTextureSet(), set.getTextureType());
		}
	}

	@Override
	public String getDescriptionId() {
		return this.unlocalizedName;
	}

	@Override
	public String getDescriptionId(final ItemStack stack) {
		return super.getDescriptionId();
	}

	@Override
	public Component getDescription() {
		return CAPI.TRANSLATIONS.makeLocalizedName(this.getDescriptionId(), this.set, this.material);
	}

	@Override
	public Component getName(final ItemStack stack) {
		return this.getDescription();
	}

	@Override
	public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
		final MaterialTraitDust dust = this.material == null ? null : this.material.getTrait(NCMaterialTraits.DUST);
		return dust == null ? -1 : (int) (dust.getBurnTime() * this.set.getUnitValue(this.material) / CAPI.UNIT);
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
