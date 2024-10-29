package conductance.api.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import lombok.Getter;

public class MetaBlockEntity<T extends MetaBlockEntity<T>> implements LevelEventListener {

	@Getter
	private final IMbeHolder<T> holder;

	public MetaBlockEntity(final IMbeHolder<T> holder) {
		this.holder = holder;
	}

	@SuppressWarnings("unchecked")
	public final T self() {
		return (T) this;
	}

	@Override
	public void onNeighborChanged(final BlockPos neighborPos, final BlockState neighborState, final Direction neighborSide) {
	}
}
