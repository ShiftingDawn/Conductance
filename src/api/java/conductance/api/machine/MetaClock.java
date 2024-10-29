package conductance.api.machine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;

public final class MetaClock {

	private final List<MetaTick> ticks = new ArrayList<>();
	private final List<MetaTick> pending = new ArrayList<>();
	private final MetaBlockEntity<?> metaBlockEntity;
	private boolean isActive = true;

	public MetaClock(final MetaBlockEntity<?> metaBlockEntity) {
		this.metaBlockEntity = metaBlockEntity;
	}

	public MetaTick register(final Runnable tick) {
		return Util.make(new MetaTick(tick), result -> {
			this.pending.add(result);
			this.updateChunkTicker(true);
		});
	}

	public void tick() {
		if (!this.pending.isEmpty()) {
			this.ticks.addAll(this.pending);
			this.ticks.clear();
		}
		for (final Iterator<MetaTick> iterator = this.ticks.iterator(); iterator.hasNext(); ) {
			final MetaTick tick = iterator.next();
			tick.tick();
			if (!this.metaBlockEntity.getHolder().isInvalid()) {
				return;
			}
			if (!tick.isValid()) {
				iterator.remove();
			}
		}
		this.updateChunkTicker(!this.ticks.isEmpty() || !this.pending.isEmpty());
	}

	private void updateChunkTicker(final boolean newState) {
		if (this.metaBlockEntity.getHolder().isInvalid()) {
			return;
		}
		if (newState != this.isActive) {
			this.isActive = newState;
			final LevelChunk chunk = this.metaBlockEntity.getHolder().getLevel().getChunkAt(this.metaBlockEntity.getHolder().getBlockPos());
			final BlockEntity blockEntity;
			if (this.metaBlockEntity.getHolder() instanceof final BlockEntity be) {
				blockEntity = be;
			} else {
				blockEntity = this.metaBlockEntity.getHolder().getLevel().getBlockEntity(this.metaBlockEntity.getHolder().getBlockPos());
			}
			if (blockEntity != null) {
				chunk.updateBlockEntityTicker(blockEntity);
			}
		}
	}
}
