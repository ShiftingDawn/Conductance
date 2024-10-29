package conductance.api.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MetaBlockEntityBlockEntity<T extends MetaBlockEntity<T>> extends BlockEntity implements IMbeHolder<T>, LevelEventListener {

	private final MetaBlockEntityType<T> metaBlockEntityType;
	private final T metaBlockEntity;

	public MetaBlockEntityBlockEntity(final BlockPos pos, final BlockState blockState, final MetaBlockEntityType<T> metaBlockEntityType) {
		super(metaBlockEntityType.getBlockEntityType().get(), pos, blockState);
		this.metaBlockEntityType = metaBlockEntityType;
		this.metaBlockEntity = this.metaBlockEntityType.newInstance(this);
	}

	@Override
	public T getMetaBlockEntity() {
		return this.metaBlockEntity;
	}

	@Override
	public void onNeighborChanged(final BlockPos neighborPos, final BlockState neighborState, final Direction neighborSide) {
		this.getMetaBlockEntity().onNeighborChanged(neighborPos, neighborState, neighborSide);
	}
}
