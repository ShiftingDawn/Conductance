package conductance.core.data.datasync;

import net.minecraft.server.level.ServerLevel;

public interface SyncTask {

	void tick(long tick, ServerLevel serverLevel);
}
