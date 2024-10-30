package conductance.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import conductance.api.machine.MetaBlockEntity;
import conductance.api.machine.MetaBlockEntityType;
import conductance.api.machine.data.Managed;
import conductance.api.machine.data.Persisted;
import conductance.Conductance;

@Managed
public class RecipeMachine extends MetaBlockEntity<RecipeMachine> {

	@Persisted
	private int testInteger = 10;
	@Persisted("test")
	private ResourceLocation testRL = Conductance.id("test2");

	public RecipeMachine(final MetaBlockEntityType<RecipeMachine> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
	}
}
