package conductance.lib.registry;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import conductance.api.periodicelement.PeriodicElement;
import conductance.api.registry.RegistryProvider;
import conductance.Conductance;

public final class RegistryProviderImpl implements RegistryProvider {

	private final Map<ResourceLocation, Registry<?>> loadOrder = new LinkedHashMap<>();
	private final AtomicBoolean frozen = new AtomicBoolean(true);
	private final Table<Registry<?>, ResourceLocation, Object> registerCache = HashBasedTable.create();

	private final ResourceKey<Registry<PeriodicElement>> periodicElementRegistry = this.makeKey("periodic_element");

	private final Registry<PeriodicElement> periodicElements = this.makeRegistry(this.periodicElementRegistry);

	public RegistryProviderImpl(final IEventBus modEventBus) {
		modEventBus.addListener(NewRegistryEvent.class, this::registerRegistries);
		modEventBus.addListener(EventPriority.HIGHEST, RegisterEvent.class, ignored -> this.frozen.set(false));
		modEventBus.addListener(EventPriority.LOW, RegisterEvent.class, this::registerCachedItems);
		modEventBus.addListener(EventPriority.LOWEST, FMLLoadCompleteEvent.class, ignored -> this.frozen.set(true));
	}

	private <T> ResourceKey<Registry<T>> makeKey(final String id) {
		return ResourceKey.createRegistryKey(Conductance.id(id));
	}

	private <T> Registry<T> makeRegistry(final ResourceKey<Registry<T>> key, final boolean doSync) {
		return Util.make(new RegistryBuilder<>(key).sync(doSync).create(),
				registry -> this.loadOrder.put(key.location(), registry));
	}

	private <T> Registry<T> makeRegistry(final ResourceKey<Registry<T>> key) {
		return this.makeRegistry(key, true);
	}

	public <TYPE, VALUE extends TYPE> void register(final Registry<TYPE> registry, final ResourceLocation id, final VALUE value) {
		if (!this.frozen.get()) {
			Conductance.LOGGER.info("[{}]Registered {}", registry.key().location(), id);
			Registry.register(registry, id, value);
		} else {
			this.registerCache.put(registry, id, value);
		}
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private void registerCachedItems(final RegisterEvent event) {
		this.registerCache.rowKeySet().forEach(registry -> {
			event.register(((Registry) registry).key(), helper -> {
				this.registerCache.row(registry).forEach((key, obj) -> {
					Conductance.LOGGER.info("[{}]Registered {}", registry.key().location(), key);
					helper.register(key, obj);
				});
			});
		});
		this.registerCache.clear();
	}

	private void registerRegistries(final NewRegistryEvent event) {
		this.loadOrder.values().forEach(event::register);
	}

	@Override
	public ResourceKey<Registry<PeriodicElement>> periodicElementRegistry() {
		return this.periodicElementRegistry;
	}

	@Override
	public Registry<PeriodicElement> periodicElements() {
		return this.periodicElements;
	}
}
