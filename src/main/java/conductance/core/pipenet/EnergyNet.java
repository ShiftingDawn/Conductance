package conductance.core.pipenet;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultEdge;
import conductance.api.capability.CapabilityHelper;
import conductance.Conductance;

public class EnergyNet extends PipeNetwork<CableData> {

	public static final ResourceLocation TYPE = Conductance.id("energynet");

	public EnergyNet(final LevelPipeNetwork<CableData> levelNet) {
		super(levelNet, EnergyNet.TYPE);
	}

	@Override
	protected NetworkPath<CableData> createNetworkPath(final BlockPos startPos, final BlockPos endPos, final Direction endSide, final GraphPath<BlockPos, DefaultEdge> path) {
		final int totalLoss = path.getVertexList().stream().map(this::getActualNode)
				.map(Objects::requireNonNull)
				.mapToInt(node -> node.getData().properties().getCableLoss()).sum();
		return new NetworkPath<>(endPos, endSide, path.getVertexList().stream().map(this::getActualNode).toList(), new EnergyPathData(totalLoss));
	}

	@Override
	protected boolean isEndpointStillValid(final Level level, final BlockPos pos, final Direction side) {
		return CapabilityHelper.getEnergyHandler(level, pos.relative(side), side.getOpposite()) != null;
	}
}
