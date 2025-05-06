package conductance.core.pipenet;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public interface INetworkNode<NODE extends INetworkNode<NODE, DATA>, DATA> {

	void setConnections(int connections);

	int getConnections();

	BlockPos getBlockPos();

	DATA getData();

	ResourceLocation getNodeType();

	LevelPipeNetwork<NODE, DATA> getNetwork(ServerLevel serverLevel);

	boolean canConnectTo(Level level, BlockPos pos, Direction side);
}
