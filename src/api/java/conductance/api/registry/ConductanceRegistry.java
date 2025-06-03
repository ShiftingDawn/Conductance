package conductance.api.registry;

import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

public interface ConductanceRegistry<KEY, VALUE extends IRegistryObject<KEY>> extends Iterable<VALUE> {

	boolean containsKey(KEY key);

	boolean containsValue(VALUE value);

	@Nullable
	VALUE get(KEY key);

	KEY getKey(VALUE value);

	Set<KEY> keys();

	Set<VALUE> values();

	Set<Map.Entry<KEY, VALUE>> entries();

	Map<KEY, VALUE> registry();

	boolean isFrozen();
}
