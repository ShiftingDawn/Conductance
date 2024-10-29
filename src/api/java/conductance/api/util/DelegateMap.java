package conductance.api.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DelegateMap<K, V> implements Map<K, V> {

	private final Map<K, V> delegate;

	public DelegateMap(final Map<K, V> delegate) {
		this.delegate = delegate;
	}

	@Override
	public int size() {
		return this.delegate.size();
	}

	@Override
	public boolean isEmpty() {
		return this.delegate.isEmpty();
	}

	@Override
	public boolean containsKey(final Object key) {
		return this.delegate.containsKey(key);
	}

	@Override
	public boolean containsValue(final Object value) {
		return this.delegate.containsValue(value);
	}

	@Override
	public V get(final Object key) {
		return this.delegate.get(key);
	}

	@Nullable
	@Override
	public V put(final K key, final V value) {
		return this.delegate.put(key, value);
	}

	@Override
	public V remove(final Object key) {
		return this.delegate.remove(key);
	}

	@Override
	public void putAll(@NotNull final Map<? extends K, ? extends V> m) {
		this.delegate.putAll(m);
	}

	@Override
	public void clear() {
		this.delegate.clear();
	}

	@NotNull
	@Override
	public Set<K> keySet() {
		return this.delegate.keySet();
	}

	@NotNull
	@Override
	public Collection<V> values() {
		return this.delegate.values();
	}

	@NotNull
	@Override
	public Set<Entry<K, V>> entrySet() {
		return this.delegate.entrySet();
	}

	@Override
	public boolean equals(final Object o) {
		return this.delegate.equals(o);
	}

	@Override
	public int hashCode() {
		return this.delegate.hashCode();
	}

	@Override
	public V getOrDefault(final Object key, final V defaultValue) {
		return this.delegate.getOrDefault(key, defaultValue);
	}

	@Override
	public void forEach(final BiConsumer<? super K, ? super V> action) {
		this.delegate.forEach(action);
	}

	@Override
	public void replaceAll(final BiFunction<? super K, ? super V, ? extends V> function) {
		this.delegate.replaceAll(function);
	}

	@Nullable
	@Override
	public V putIfAbsent(final K key, final V value) {
		return this.delegate.putIfAbsent(key, value);
	}

	@Override
	public boolean remove(final Object key, final Object value) {
		return this.delegate.remove(key, value);
	}

	@Override
	public boolean replace(final K key, final V oldValue, final V newValue) {
		return this.delegate.replace(key, oldValue, newValue);
	}

	@Nullable
	@Override
	public V replace(final K key, final V value) {
		return this.delegate.replace(key, value);
	}

	@Override
	public V computeIfAbsent(final K key, @NotNull final Function<? super K, ? extends V> mappingFunction) {
		return this.delegate.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Nullable
	public V computeIfPresent(final K key, @NotNull final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		return this.delegate.computeIfPresent(key, remappingFunction);
	}

	@Override
	public V compute(final K key, @NotNull final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		return this.delegate.compute(key, remappingFunction);
	}

	@Override
	public V merge(final K key, @NotNull final V value, @NotNull final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
		return this.delegate.merge(key, value, remappingFunction);
	}
}
