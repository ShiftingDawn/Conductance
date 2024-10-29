package conductance.content.block;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import com.tterrag.registrate.util.entry.RegistryEntry;
import lombok.Getter;
import conductance.api.CAPI;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;
import conductance.client.resourcepack.MaterialBlockModelHandler;
import conductance.init.ConductanceCreativeTabs;

public class MaterialBlock extends ConductanceBlock {

	@Getter
	private final Material material;
	@Getter
	private final TaggedMaterialSet set;
	private final String unlocalizedName;

	public MaterialBlock(final Properties properties, final Material material, final TaggedMaterialSet set) {
		super(properties);
		this.material = material;
		this.set = set;
		this.unlocalizedName = "block.%s.%s".formatted(material.getRegistryKey().getNamespace(), set.getUnlocalizedName(material));
		if (CAPI.isClient()) {
			MaterialBlockModelHandler.add(this, material, material.getTextureSet(), set.getTextureType());
		}
	}

	@Override
	public String getDescriptionId() {
		return this.unlocalizedName;
	}

	@Override
	public MutableComponent getName() {
		return CAPI.translations().makeLocalizedName(this.getDescriptionId(), this.set, this.material);
	}

	@Override
	public RegistryEntry<CreativeModeTab, CreativeModeTab> getCreativeTab() {
		return ConductanceCreativeTabs.MATERIAL_BLOCKS;
	}

	@OnlyIn(Dist.CLIENT)
	public static BlockColor handleColorTint() {
		return (state, reader, pos, tintIndex) -> {
			if (state.getBlock() instanceof final MaterialBlock block) {
				return block.material.getTintColor(tintIndex);
			}
			return -1;
		};
	}
}
