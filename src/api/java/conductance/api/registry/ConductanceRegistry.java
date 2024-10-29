package conductance.api.registry;

import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

public interface ConductanceRegistry<KEY, VALUE extends IRegistryObject<KEY>> extends Iterable<VALUE> {

	boolean containsKey(KEY key);

	boolean containsValue(VALUE value);

	KEY register(KEY key, VALUE value);

	VALUE registerOrOverride(KEY key, VALUE value);

	@Nullable
	VALUE get(KEY key);

	VALUE getOrDefault(KEY key, VALUE defaultValue);

	KEY getKey(VALUE value);

	KEY getOrDefaultKey(VALUE key, KEY defaultKey);

	boolean remove(KEY key);

	Set<KEY> keys();

	Set<VALUE> values();

	Set<Map.Entry<KEY, VALUE>> entries();

	Map<KEY, VALUE> registry();

	boolean isFrozen();
}
