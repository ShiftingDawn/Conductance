package conductance.core.xdata.datasync;

import net.minecraft.server.level.ServerLevel;

public interface SyncTask {

	void tick(long tick, ServerLevel serverLevel);
}
