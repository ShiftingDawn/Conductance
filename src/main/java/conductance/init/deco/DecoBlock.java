package conductance.init.deco;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.CreativeModeTab;
import com.tterrag.registrate.util.entry.RegistryEntry;
import conductance.api.CAPI;
import conductance.init.ConductanceCreativeTabs;
import conductance.init.block.ConductanceBlock;

public class DecoBlock extends ConductanceBlock {

	public DecoBlock(final Properties properties) {
		super(properties);
	}

	@Override
	public MutableComponent getName() {
		return CAPI.translations().makeLocalizedName(this);
	}

	@Override
	public RegistryEntry<CreativeModeTab, CreativeModeTab> getCreativeTab() {
		return ConductanceCreativeTabs.DECORATION;
	}
}
