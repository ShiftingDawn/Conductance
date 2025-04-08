package conductance.core.xdata;

import net.minecraft.server.level.ServerLevel;

public interface SyncTask {

	void tick(long tick, ServerLevel serverLevel);
}
