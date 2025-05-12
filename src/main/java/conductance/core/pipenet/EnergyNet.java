package conductance.core.pipenet;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultEdge;
import conductance.api.capability.CapabilityHelper;
import conductance.Conductance;

public class EnergyNet extends PipeNetwork<IWireNode, WireData> {

	public static final ResourceLocation TYPE = Conductance.id("energynet");

	public EnergyNet(final LevelPipeNetwork<IWireNode, WireData> levelNet) {
		super(levelNet, EnergyNet.TYPE);
	}

	@Override
	protected NetworkPath<IWireNode, WireData> createNetworkPath(final BlockPos startPos, final BlockPos endPos, final Direction endSide, final GraphPath<BlockPos, DefaultEdge> path) {
		return new NetworkPath<>(endPos, endSide, path.getVertexList().stream().map(this::getActualNode).toList(), new EnergyPathData());
	}

	@Override
	protected boolean isEndpointStillValid(final Level level, final BlockPos pos, final Direction side) {
		return CapabilityHelper.getEnergyHandler(level, pos.relative(side), side.getOpposite()) != null;
	}
}
