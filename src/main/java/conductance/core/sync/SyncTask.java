package conductance.core.sync;

import net.minecraft.server.level.ServerLevel;

interface SyncTask {

	void tick(long tick, ServerLevel serverLevel);
}
