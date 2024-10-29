package conductance.content.item;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import com.tterrag.registrate.util.entry.RegistryEntry;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;
import conductance.client.resourcepack.MaterialItemModelHandler;
import conductance.init.ConductanceCreativeTabs;

@Getter
@Accessors(fluent = true)
public class MaterialItem extends ConductanceItem {

	private final Material material;
	private final TaggedMaterialSet set;
	private final String unlocalizedName;

	public MaterialItem(final Properties properties, final Material material, final TaggedMaterialSet set) {
		super(properties);
		this.material = material;
		this.set = set;
		this.unlocalizedName = "item.%s.%s".formatted(material.getRegistryKey().getNamespace(), set.getUnlocalizedName(material));
		if (CAPI.isClient()) {
			MaterialItemModelHandler.add(this, material, material.getTextureSet(), set.getTextureType());
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
	public RegistryEntry<CreativeModeTab, CreativeModeTab> getCreativeTab() {
		return ConductanceCreativeTabs.MATERIAL_ITEMS;
	}

	@Override
	public int getBurnTime(final ItemStack itemStack, @Nullable final RecipeType<?> recipeType) {
		final int time = this.material.getData().getBurnTime();
		return time <= 0 ? -1 : (int) (time * this.set.getUnitValue(this.material) / CAPI.UNIT);
	}

	@OnlyIn(Dist.CLIENT)
	public static ItemColor handleColorTint() {
		return (stack, tintIndex) -> {
			if (tintIndex == 0 && stack.getItem() instanceof final MaterialItem materialItem) {
				return materialItem.material.getTintColor(tintIndex);
			}
			return -1;
		};
	}
}
