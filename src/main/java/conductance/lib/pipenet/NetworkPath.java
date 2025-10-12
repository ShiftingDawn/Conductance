package conductance.lib.pipenet;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public class NetworkPath<NODE extends INetworkNode<NODE, DATA>, DATA> {

	@Getter
	private final BlockPos dest;
	@Getter
	private final Direction side;
	@Getter
	private final List<NODE> path;
	@Getter
	@Nullable
	private final Object data;

	public NetworkPath(final BlockPos dest, final Direction side, final List<NODE> path, @Nullable final Object data) {
		this.dest = dest;
		this.side = side;
		this.path = path;
		this.data = data;
	}
}
