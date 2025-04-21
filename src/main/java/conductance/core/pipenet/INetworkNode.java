package conductance.core.pipenet;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public interface INetworkNode<DATA> {

	void setConnections(int connections);

	int getConnections();

	BlockPos getBlockPos();

	DATA getData();

	ResourceLocation getNodeType();

	LevelPipeNetwork<DATA> getNetwork(ServerLevel serverLevel);

	boolean canConnectTo(Level level, BlockPos pos, Direction side);
}
