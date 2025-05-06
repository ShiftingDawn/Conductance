package conductance.core.pipenet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.BFSShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

@RequiredArgsConstructor
public abstract class PipeNetwork<NODE extends INetworkNode<NODE, DATA>, DATA> {

	private final SimpleGraph<BlockPos, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
	private final Map<BlockPos, Set<Direction>> endpoints = new HashMap<>();
	private final Map<BlockPos, Set<NetworkPath<NODE, DATA>>> paths = new HashMap<>();
	private final LevelPipeNetwork<NODE, DATA> levelNet;
	private final ResourceLocation networkType;

	protected abstract NetworkPath<NODE, DATA> createNetworkPath(BlockPos startPos, BlockPos endPos, Direction endSide, GraphPath<BlockPos, DefaultEdge> path);

	protected abstract boolean isEndpointStillValid(Level level, BlockPos pos, Direction side);

	@SuppressWarnings("unchecked")
	@Nullable
	protected NODE getActualNode(final BlockPos pos) {
		if (this.levelNet.getLevel().getBlockEntity(pos) instanceof final INetworkNode<?, ?> node && node.getNodeType().equals(this.networkType)) {
			return (NODE) node;
		}
		return null;
	}

	final void addNode(final BlockPos node) {
		if (this.graph.containsVertex(node)) {
			return;
		}
		this.graph.addVertex(node);
	}

	void applyConnections(final BlockPos pos, final int connections) {
		for (final Direction direction : Direction.values()) {
			final BlockPos otherPos = pos.relative(direction);
			if (!this.graph.containsVertex(otherPos)) {
				continue;
			}
			if (PipeNetHelper.isConnected(connections, direction)) {
				this.graph.addEdge(pos, otherPos);
			} else {
				this.graph.removeEdge(pos, otherPos);
			}
		}
	}

	final void removeNode(final BlockPos pos) {
		if (!this.graph.containsVertex(pos)) {
			return;
		}
		this.graph.removeVertex(pos);
		this.endpoints.remove(pos);
	}

	public boolean addEndpoint(final BlockPos pos, final Direction side, final boolean connect) {
		if (!connect && !this.endpoints.containsKey(pos)) {
			return false;
		}
		final Set<Direction> sides = this.endpoints.computeIfAbsent(pos, k -> new HashSet<>());
		if (connect) {
			if (sides.contains(side)) {
				return false;
			}
			sides.add(side);
		} else {
			if (!sides.contains(side)) {
				return false;
			}
			sides.remove(side);
			if (sides.isEmpty()) {
				this.endpoints.remove(pos);
			}
		}
		return true;
	}

	public boolean isEndpoint(final BlockPos pos, final Direction side) {
		return this.endpoints.containsKey(pos) && this.endpoints.get(pos).contains(side);
	}

	protected final Set<BlockPos> consume(final PipeNetwork<NODE, DATA> other) {
		for (final BlockPos vertex : other.graph.vertexSet()) {
			this.graph.addVertex(vertex);
		}
		for (final DefaultEdge edge : other.graph.edgeSet()) {
			final BlockPos source = other.graph.getEdgeSource(edge);
			final BlockPos target = other.graph.getEdgeTarget(edge);
			if (!this.graph.containsEdge(source, target)) {
				this.graph.addEdge(source, target);
			}
		}
		for (final Map.Entry<BlockPos, Set<Direction>> entry : other.endpoints.entrySet()) {
			this.endpoints.computeIfAbsent(entry.getKey(), k -> new HashSet<>()).addAll(entry.getValue());
		}
		return other.graph.vertexSet();
	}

	void recalculate() {
		this.paths.clear();
		this.validateEndpoints();
		final Set<BlockPos> endpointNodes = this.endpoints.keySet();
		final BFSShortestPath<BlockPos, DefaultEdge> walker = new BFSShortestPath<>(this.graph);
		for (final BlockPos start : endpointNodes) {
			final ShortestPathAlgorithm.SingleSourcePaths<BlockPos, DefaultEdge> shortestPaths = walker.getPaths(start);
			for (final BlockPos end : endpointNodes) {
				if (start.equals(end) || !this.endpoints.containsKey(end)) {
					continue;
				}
				final GraphPath<BlockPos, DefaultEdge> shortestPath = shortestPaths.getPath(end);
				this.endpoints.get(end).forEach(connectedSide -> {
					this.paths.computeIfAbsent(start, k -> new HashSet<>()).add(this.createNetworkPath(start, end, connectedSide, shortestPath));
				});
			}
		}
	}

	private void validateEndpoints() {
		final Map<BlockPos, Set<Direction>> knownEndpoints = new HashMap<>();
		this.endpoints.forEach((knownPos, knownSides) -> {
			if (this.graph.containsVertex(knownPos)) {
				knownEndpoints.put(knownPos, knownSides);
			}
		});
		this.endpoints.clear();
		for (final BlockPos pos : knownEndpoints.keySet()) {
			final Set<Direction> sides = knownEndpoints.get(pos).stream()
					.filter(side -> this.isEndpointStillValid(this.levelNet.getLevel(), pos, side))
					.collect(Collectors.toSet());
			this.endpoints.put(pos, sides);
		}
	}

	final Set<BlockPos> getNodes() {
		return this.graph.vertexSet();
	}

	@Nullable
	public Set<NetworkPath<NODE, DATA>> getPaths(final BlockPos pos) {
		return this.paths.get(pos);
	}

	final Set<PipeNetwork<NODE, DATA>> findIslands() {
		final ConnectivityInspector<BlockPos, DefaultEdge> inspector = new ConnectivityInspector<>(this.graph);
		final List<Set<BlockPos>> components = inspector.connectedSets();
		if (components.size() <= 1) {
			return Set.of(this);
		}
		final Set<PipeNetwork<NODE, DATA>> networks = new HashSet<>();
		for (final Set<BlockPos> component : components) {
			final PipeNetwork<NODE, DATA> newNet = this.levelNet.createNetwork();
			for (final BlockPos node : component) {
				newNet.addNode(node);
			}
			for (final DefaultEdge edge : this.graph.edgeSet()) {
				final BlockPos pos1 = this.graph.getEdgeSource(edge);
				final BlockPos pos2 = this.graph.getEdgeTarget(edge);
				if (newNet.graph.containsVertex(pos1) && newNet.graph.containsVertex(pos2)) {
					newNet.graph.addEdge(pos1, pos2);
				}
			}
			for (final BlockPos node : component) {
				final Set<Direction> dirs = this.endpoints.get(node);
				if (dirs != null) {
					for (final Direction dir : dirs) {
						newNet.addEndpoint(node, dir, true);
					}
				}
			}
			networks.add(newNet);
		}
		return networks;
	}
}
