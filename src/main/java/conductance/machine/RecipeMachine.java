package conductance.machine;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import com.lowdragmc.lowdraglib.gui.modular.IUIHolder;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import conductance.api.machine.MetaBlockEntity;
import conductance.api.machine.MetaBlockEntityType;
import conductance.api.machine.trait.MetaCapability;

public class RecipeMachine extends MetaBlockEntity<RecipeMachine> implements IUIHolder.Block {

	public RecipeMachine(final MetaBlockEntityType<RecipeMachine> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
	}

	@Override
	protected void registerCapabilities(final Consumer<MetaCapability> register) {
	}

	@Override
	public ModularUI createUI(final Player entityPlayer) {
		return new ModularUI(new WidgetGroup(100, 100, 200, 200), this, entityPlayer);
	}
}
