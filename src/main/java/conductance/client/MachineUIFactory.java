package conductance.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import com.lowdragmc.lowdraglib.gui.factory.UIFactory;
import com.lowdragmc.lowdraglib.gui.modular.IUIHolder;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.gui.MachineGuiHolder;
import conductance.Conductance;

public class MachineUIFactory extends UIFactory<MachineBlockEntity<?>> {

	public static final MachineUIFactory INSTANCE = new MachineUIFactory();

	public MachineUIFactory() {
		super(Conductance.id("machine"));
	}

	public static <T extends MachineBlockEntity<?> & MachineGuiHolder> ModularUI createGui(final T machine, final Player player) {
		return new ModularUI(GuiHelper.GUI_WIDTH, GuiHelper.GUI_HEIGHT, machine, player).widget(new RootWidget(machine));
	}

	@Override
	@Nullable
	protected ModularUI createUITemplate(final MachineBlockEntity<?> machine, final Player player) {
		if (machine instanceof final IUIHolder holder) {
			return holder.createUI(player);
		}
		return null;
	}

	@Override
	@Nullable
	protected MachineBlockEntity<?> readHolderFromSyncData(final RegistryFriendlyByteBuf buf) {
		final Level level = Minecraft.getInstance().level;
		if (level != null && level.getBlockEntity(buf.readBlockPos()) instanceof final MachineBlockEntity<?> machine) {
			return machine;
		}
		return null;
	}

	@Override
	protected void writeHolderToSyncData(final RegistryFriendlyByteBuf buf, final MachineBlockEntity<?> machine) {
		buf.writeBlockPos(machine.getBlockPos());
	}
}
