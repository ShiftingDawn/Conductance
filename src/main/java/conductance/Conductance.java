package conductance;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.slf4j.Logger;
import conductance.api.CAPI;
import conductance.api.plugin.IConductancePluginEvent;
import conductance.api.resource.ResourceFinder;
import conductance.api.util.RegistryProvider;
import conductance.core.machine.MachineCore;
import conductance.core.material.MaterialCore;
import conductance.core.material.MaterialRegistryImpl;
import conductance.core.periodicelement.PeriodicElementCore;
import conductance.core.recipe.RecipeCore;
import conductance.core.tier.TierCore;
import conductance.core.tier.TierRegistryImpl;
import conductance.init.ConductanceBlocks;
import conductance.init.ConductanceCreativeTabs;
import conductance.init.ConductanceDataComponents;
import conductance.init.ConductanceFluids;
import conductance.init.ConductanceItems;
import conductance.init.ConductanceMenuTypes;
import conductance.lib.GridInteractionHandler;
import conductance.lib.RegistryProviderImpl;
import conductance.lib.ResourceFinderImpl;
import conductance.lib.network.RegisterPacketEvent;

@SuppressWarnings("NotNullFieldNotInitialized")
public abstract class Conductance {

	public static final String MODID = "conductance";
	public static final Logger LOGGER = LogUtils.getLogger();
	public static RegistryProviderImpl REGISTRIES;
	public static MaterialRegistryImpl MATERIALS;
	public static TierRegistryImpl TIERS;
	private static final AtomicBoolean HAS_REGISTERED = new AtomicBoolean(false);
	private static IEventBus MODBUS;

	protected Conductance(final Dist dist, final IEventBus modEventBus, final ModContainer modContainer) {
		Conductance.LOGGER.info("Conductance[{}] is initializing on platform: NeoForge", dist);
		Conductance.MODBUS = modEventBus;
		modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, ModConfig.SPEC);
		modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

		PluginEventBus.initialize();

		Conductance.REGISTRIES = Util.make(new RegistryProviderImpl(modEventBus), regs -> Conductance.setApiValue(RegistryProvider.class, regs));
		Conductance.setApiValue(ResourceFinder.class, new ResourceFinderImpl());

		modEventBus.addListener(RegisterEvent.class, this::onRegister);
		modEventBus.addListener(RegisterPayloadHandlersEvent.class, this::onRegisterPayloadHandlers);
		GridInteractionHandler.init(NeoForge.EVENT_BUS);

		ConductanceCreativeTabs.initialize(modEventBus);
	}

	private void onRegister(final RegisterEvent event) {
		if (Conductance.HAS_REGISTERED.getAndSet(true)) {
			return;
		}
		PeriodicElementCore.initialize();
		MaterialCore.initialize(Conductance.MODBUS);

		TierCore.initialize();
		RecipeCore.initialize();

		ConductanceMenuTypes.initialize(Conductance.MODBUS);
		ConductanceDataComponents.initialize(Conductance.MODBUS);
		ConductanceBlocks.initialize(Conductance.MODBUS);
		ConductanceItems.initialize(Conductance.MODBUS);
		ConductanceFluids.initialize(Conductance.MODBUS);

		MachineCore.initialize(Conductance.MODBUS);
	}

	private void onRegisterPayloadHandlers(final RegisterPayloadHandlersEvent event) {
		Conductance.dispatchAll(RegisterPacketEvent.class, new RegisterPacketEvent(event.registrar("1")));
	}

	public static ResourceLocation id(final String path) {
		if (path.contains(":")) {
			return ResourceLocation.parse(path);
		} else {
			return ResourceLocation.fromNamespaceAndPath(Conductance.MODID, path);
		}
	}

	public static <T> void setApiValue(final Class<T> variableType, final T value) {
		final Field field = Arrays.stream(CAPI.class.getDeclaredFields())
			.filter(f -> variableType.isAssignableFrom(f.getType()) && !Modifier.isFinal(f.getModifiers()))
			.findFirst().orElseThrow();
		try {
			field.setAccessible(true);
			field.set(null, value);
			field.setAccessible(false);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Cannot set API value", e);
		}
	}

	public static Set<String> getKnownPluginNamespaces() {
		return PluginEventBus.LISTENERS.values().stream().flatMap(Collection::stream).map(PluginEventBus.EventMethod::modid).collect(Collectors.toSet());
	}

	public static <T extends IConductancePluginEvent> void dispatch(final Class<T> eventClass, final Function<String, T> eventFactory) {
		PluginEventBus.LISTENERS.values().forEach(listeners -> {
			for (final PluginEventBus.EventMethod listener : listeners) {
				if (listener.eventType().isAssignableFrom(eventClass)) {
					listener.listener().accept(eventFactory.apply(listener.modid()));
				}
			}
		});
	}

	public static <T extends IConductancePluginEvent> void dispatchAll(final Class<T> eventClass, final T event) {
		Conductance.dispatch(eventClass, ignored -> event);
	}

	public static boolean isSafeToAccessLevel() {
		if (CAPI.isClient()) {
			return Minecraft.getInstance().level != null;
		}
		return Optional.ofNullable(ServerLifecycleHooks.getCurrentServer())
			.map(server -> !server.isStopped() && !server.isShutdown() && server.isRunning() && !server.isCurrentlySaving())
			.orElse(false);
	}
}
