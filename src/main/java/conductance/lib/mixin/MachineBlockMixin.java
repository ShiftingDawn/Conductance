package conductance.lib.mixin;

import net.minecraft.world.item.CreativeModeTab;
import com.tterrag.registrate.util.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import conductance.api.machine.MachineBlock;
import conductance.init.block.IConductanceBlock;
import conductance.init.ConductanceCreativeTabs;

@Mixin(MachineBlock.class)
public abstract class MachineBlockMixin implements IConductanceBlock {

	@Override
	public RegistryEntry<CreativeModeTab, CreativeModeTab> getCreativeTab() {
		return ConductanceCreativeTabs.MACHINES;
	}
}
