package conductance.init.block;

import net.minecraft.world.item.CreativeModeTab;
import com.tterrag.registrate.util.entry.RegistryEntry;
import conductance.init.ConductanceCreativeTabs;

public class DecoSimpleBlock extends SimpleDynamicBlock {

	public DecoSimpleBlock(final Properties properties, final String textureName) {
		super(properties, textureName);
	}

	@Override
	public RegistryEntry<CreativeModeTab, CreativeModeTab> getCreativeTab() {
		return ConductanceCreativeTabs.DECORATION;
	}
}
