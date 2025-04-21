package conductance.core.pipenet;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public class NetworkPath<DATA> {

	@Getter
	private final BlockPos dest;
	@Getter
	private final Direction side;
	@Getter
	private final List<INetworkNode<DATA>> path;
	@Getter
	@Nullable
	private final Object data;

	public NetworkPath(final BlockPos dest, final Direction side, final List<INetworkNode<DATA>> path, @Nullable final Object data) {
		this.dest = dest;
		this.side = side;
		this.path = path;
		this.data = data;
	}
}
