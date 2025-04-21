package conductance.core.pipenet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import conductance.Conductance;

@RequiredArgsConstructor
public abstract class LevelPipeNetwork<DATA> extends SavedData {

	private final Map<BlockPos, PipeNetwork<DATA>> networks = new HashMap<>();
	@Getter
	private final ServerLevel level;

	protected abstract PipeNetwork<DATA> createNetwork();

	public void add(final BlockPos pos, final int connections) {
		if (this.networks.containsKey(pos)) {
			Conductance.LOGGER.warn("Trying to add PipeNetworkNode at {} twice!", pos);
			return;
		}
		if (connections == 0) {
			this.networks.put(pos, Util.make(this.createNetwork(), net -> net.addNode(pos)));
			return;
		}
		final List<Direction> connectedSides = PipeNetHelper.getConnections(connections);
		final List<PipeNetwork<DATA>> connectedNetworks = connectedSides.stream().map(pos::relative).map(this.networks::get).filter(Objects::nonNull).toList();
		if (connectedNetworks.isEmpty()) {
			this.networks.put(pos, Util.make(this.createNetwork(), net -> net.addNode(pos)));
			return;
		}
		final PipeNetwork<DATA> network;
		if (connectedNetworks.size() == 1) {
			network = connectedNetworks.getFirst();
			network.addNode(pos);
		} else {
			network = this.createNetwork();
			connectedNetworks.forEach(connectedNetwork ->
					network.consume(connectedNetwork).forEach(consumedPos ->
							this.networks.put(consumedPos, network)
					)
			);
		}
		this.networks.put(pos, network);
		network.applyConnections(pos, connections);
		network.recalculate();
	}

	public void remove(final BlockPos pos) {
		if (!this.networks.containsKey(pos)) {
			return;
		}
		final PipeNetwork<DATA> network = this.networks.get(pos);
		network.removeNode(pos);
		this.networks.remove(pos);
		final Set<PipeNetwork<DATA>> islands = network.findIslands();
		islands.forEach(island -> {
			island.getNodes().forEach(node -> {
				this.networks.put(node, island);
			});
			island.recalculate();
		});
	}

	public void setConnected(final INetworkNode<DATA> node, final INetworkNode<DATA> otherNode, final Direction side, final boolean connect) {
		if (PipeNetHelper.isConnected(node.getConnections(), side) == connect) {
			return;
		}
		node.setConnections(PipeNetHelper.setConnection(node.getConnections(), side, connect));
		otherNode.setConnections(PipeNetHelper.setConnection(otherNode.getConnections(), side.getOpposite(), connect));
		final PipeNetwork<DATA> network = this.networks.get(node.getBlockPos());
		if (connect) {
			final List<Direction> connectedSides = PipeNetHelper.getConnections(node.getConnections());
			final List<PipeNetwork<DATA>> connectedNetworks = connectedSides.stream().map(node.getBlockPos()::relative).map(this.networks::get).filter(Objects::nonNull).toList();
			connectedNetworks.forEach(connectedNetwork -> {
				if (connectedNetwork != network) {
					network.consume(connectedNetwork).forEach(connectedPos ->
							this.networks.put(connectedPos, network)
					);
				}
			});
			network.applyConnections(node.getBlockPos(), node.getConnections());
			network.recalculate();
		} else {
			network.applyConnections(node.getBlockPos(), node.getConnections());
			network.findIslands().forEach(island -> {
				island.getNodes().forEach(islandNode -> this.networks.put(islandNode, island));
				island.recalculate();
			});
		}
	}

	public void addEndpoint(final BlockPos pos, final Direction side, final boolean connect) {
		final PipeNetwork<DATA> network = this.networks.get(pos);
		network.addEndpoint(pos, side, connect);
		network.recalculate();
	}

	@Nullable
	public Set<NetworkPath<DATA>> getPaths(final INetworkNode<DATA> node) {
		return this.networks.get(node.getBlockPos()).getPaths(node.getBlockPos());
	}

	@Override
	public CompoundTag save(final CompoundTag compoundTag, final HolderLookup.Provider provider) {
		return compoundTag;
	}
}
