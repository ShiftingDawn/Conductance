package conductance.core.apiimpl;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import com.google.common.collect.HashBiMap;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import conductance.api.registry.ConductanceRegistry;
import conductance.api.registry.IRegistryObject;
import conductance.api.registry.RegistryObject;
import conductance.Conductance;

public abstract class ConductanceRegistryImpl<KEY, VALUE extends IRegistryObject<KEY>>
		extends RegistryObject<ResourceLocation> implements ConductanceRegistry<KEY, VALUE> {

	private final HashBiMap<KEY, VALUE> registry;
	@Getter
	protected boolean frozen = false;
	@Nullable
	@Setter
	private BiConsumer<KEY, VALUE> registerCallback, unregisterCallback;

	public ConductanceRegistryImpl(final ResourceLocation registryKey) {
		super(registryKey);
		this.registry = HashBiMap.create();

		if (!registryKey.equals(Conductance.id("root"))) {
			ApiBridge.REGISTRIES.register(registryKey, this);
		}
	}

	public void freeze() {
		Conductance.LOGGER.info("Registry {} has been frozen!", this.getRegistryKey());
		this.frozen = true;
	}

	@Override
	public boolean containsKey(final KEY key) {
		return this.registry.containsKey(key);
	}

	@Override
	public boolean containsValue(final VALUE value) {
		return this.registry.containsValue(value);
	}

	@Override
	public KEY register(final KEY key, final VALUE value) {
		if (this.frozen) {
			throw new IllegalStateException("[register]Registry %s has been frozen".formatted(this.getRegistryKey()));
		}
		if (this.containsKey(key)) {
			throw new IllegalStateException(
					"[register]Registry %s contains key %s already".formatted(this.getRegistryKey(), key));
		}
		this.registry.put(key, value);
		Conductance.LOGGER.debug("Registered {} in registry {}", key, this.getRegistryKey());
		if (this.registerCallback != null) {
			this.registerCallback.accept(key, value);
		}
		return key;
	}

	@Override
	public VALUE registerOrOverride(final KEY key, final VALUE value) {
		if (this.frozen) {
			throw new IllegalStateException(
					"[registerOrOverride]Registry %s has been frozen".formatted(this.getRegistryKey()));
		}
		final VALUE result = this.registry.put(key, value);
		if (this.unregisterCallback != null && result != null) {
			this.unregisterCallback.accept(key, result);
		}
		if (this.registerCallback != null) {
			this.registerCallback.accept(key, value);
		}
		return result;
	}

	@Override
	@Nullable
	public VALUE get(final KEY key) {
		return this.registry.get(key);
	}

	@Override
	public VALUE getOrDefault(final KEY key, final VALUE defaultValue) {
		return this.registry.getOrDefault(key, defaultValue);
	}

	@Override
	public KEY getKey(final VALUE value) {
		return this.registry.inverse().get(value);
	}

	@Override
	public KEY getOrDefaultKey(final VALUE key, final KEY defaultKey) {
		return this.registry.inverse().getOrDefault(key, defaultKey);
	}

	@Override
	public boolean remove(final KEY key) {
		if (this.frozen) {
			throw new IllegalStateException("[remove]Registry %s has been frozen".formatted(this.getRegistryKey()));
		}
		return this.registry.remove(key) != null;
	}

	@Override
	public Set<KEY> keys() {
		return this.registry.keySet();
	}

	@Override
	public Set<VALUE> values() {
		return this.registry.values();
	}

	@Override
	public Set<Map.Entry<KEY, VALUE>> entries() {
		return this.registry.entrySet();
	}

	@Override
	public Map<KEY, VALUE> registry() {
		return this.registry;
	}

	@NotNull
	@Override
	public Iterator<VALUE> iterator() {
		return this.registry.values().iterator();
	}

	public static final class ResourceKeyed<VALUE extends IRegistryObject<ResourceLocation>>
			extends ConductanceRegistryImpl<ResourceLocation, VALUE> {

		public ResourceKeyed(final ResourceLocation registryName) {
			super(registryName);
		}
	}

	public static final class StringKeyed<VALUE extends IRegistryObject<String>>
			extends ConductanceRegistryImpl<String, VALUE> {

		public StringKeyed(final ResourceLocation registryName) {
			super(registryName);
		}
	}
}
