package conductance.api.machine;

import net.minecraft.core.Direction;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelProperty;

public final class MachineModelProperties {

	public static final ModelProperty<BlockState> APPEARANCE = new ModelProperty<>();
	public static final ModelProperty<Tuple<Direction, Boolean>> ITEM_AUTO_OUTPUT = new ModelProperty<>();
	public static final ModelProperty<Tuple<Direction, Boolean>> FLUID_AUTO_OUTPUT = new ModelProperty<>();

	private MachineModelProperties() {
	}
}
