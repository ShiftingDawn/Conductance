package conductance.lib.mixin;

import net.minecraft.world.level.storage.ValueInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import conductance.api.machine.gui.MachineMenu;

@Mixin(MachineMenu.class)
public interface MachineMenuHandleClientPacketInvoker {

	@Invoker
	void invokeHandleClientPacket(ValueInput input);
}
