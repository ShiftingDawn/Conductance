package conductance.api.machine.multi;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.BlockPos;

public final class StructureCheckContext {

	public static final CheckToken<Set<IMultiBlockPart>> PARTS = new CheckToken<>(HashSet::new);
	public static final CheckToken<Set<BlockPos>> ACTIVE_BLOCKS = new CheckToken<>(HashSet::new);
	private final Map<CheckToken<?>, Object> data = new IdentityHashMap<>();

	public <T> void set(final CheckToken<T> token, final T value) {
		this.data.put(token, value);
	}

	public boolean has(final CheckToken<?> token) {
		return this.data.containsKey(token);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(final CheckToken<T> token) {
		return (T) this.data.computeIfAbsent(token, t -> t.factory().get());
	}
}
