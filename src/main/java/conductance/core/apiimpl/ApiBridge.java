package conductance.core.apiimpl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.recipe.RecipeHelper;
import conductance.api.material.ResourceFinder;
import conductance.api.registry.MaterialRegistry;
import conductance.api.registry.RegistryProvider;
import conductance.api.registry.TranslationRegistry;
import conductance.api.util.tier.TierRegistry;
import conductance.Conductance;
import conductance.core.pipenet.WireRegistry;
import conductance.core.recipe.RecipeHelperImpl;
import conductance.core.register.ConductanceRegistrate;
import conductance.core.register.MaterialRegistryImpl;

@SuppressWarnings("NotNullFieldNotInitialized")
@EventBusSubscriber(modid = Conductance.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class ApiBridge {

	public enum DataPackRegistryLoadStage {
		UNFREEZE, RESET, REFREEZE
	}

	public static final ConductanceRegistryImpl<ResourceLocation, ConductanceRegistryImpl<?, ?>> REGISTRIES = new ConductanceRegistryImpl.ResourceKeyed<>(Conductance.id("root"));
	private static RegistryProviderImpl regs;
	private static ConductanceRegistrate registrate;

	public static void init(final IEventBus modEventBus) {
		ApiBridge.regs = new RegistryProviderImpl(modEventBus);
		ApiBridge.registrate = ConductanceRegistrate.create(modEventBus);
		NeoForge.EVENT_BUS.addListener(ApiBridge::onServerAboutToStart);
		NeoForge.EVENT_BUS.addListener(ApiBridge::onServerStopped);
		ApiBridge.setApiValue(RegistryProvider.class, ApiBridge.regs);
		ApiBridge.setApiValue(ResourceFinder.class, new ResourceFinderImpl());
		ApiBridge.setApiValue(MaterialRegistry.class, MaterialRegistryImpl.INSTANCE);
		ApiBridge.setApiValue(TranslationRegistry.class, TranslationRegistryImpl.INSTANCE);
		ApiBridge.setApiValue(TierRegistry.class, TierRegistryImpl.INSTANCE);
		ApiBridge.setApiValue(RecipeHelper.class, RecipeHelperImpl.INSTANCE);
	}

	private static <T> void setApiValue(final Class<T> variableType, final T value) {
		final Field field = Arrays.stream(CAPI.class.getDeclaredFields()).filter(f -> variableType.isAssignableFrom(f.getType()) && !Modifier.isFinal(f.getModifiers())).findFirst().orElseThrow();
		try {
			field.setAccessible(true);
			field.set(null, value);
			field.setAccessible(false);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Cannot set API value", e);
		}
	}

	public static void resetRegistryAccess(@Nullable final RegistryAccess registryAccess) {
		ApiBridge.setApiValue(RegistryAccess.class, registryAccess);
	}

	private static void onServerAboutToStart(final ServerAboutToStartEvent event) {
		ApiBridge.resetRegistryAccess(event.getServer().registryAccess());
	}

	private static void onServerStopped(final ServerStoppedEvent event) {
		ApiBridge.resetRegistryAccess(null);
	}

	@SubscribeEvent
	private static void onLoadComplete(final FMLLoadCompleteEvent ignored) {
		ApiBridge.REGISTRIES.freeze();
		ApiBridge.REGISTRIES.values().forEach(ConductanceRegistryImpl::freeze);

		MaterialRegistryImpl.INSTANCE.freeze();
		TierRegistryImpl.freeze();
		WireRegistry.freeze();
	}

	public static void handleDataPackRegistryStage(final DataPackRegistryLoadStage stage) {
		switch (stage) {
			case UNFREEZE -> ApiBridge.REGISTRIES.values().forEach(reg -> {
				if (reg instanceof final ConductanceDataPackRegistry<?> dataPackRegistry) {
					dataPackRegistry.unfreeze();
				}
			});
			case RESET -> ApiBridge.REGISTRIES.values().forEach(reg -> {
				if (reg instanceof final ConductanceDataPackRegistry<?> dataPackRegistry) {
					dataPackRegistry.reset();
				}
			});
			case REFREEZE -> ApiBridge.REGISTRIES.values().forEach(reg -> {
				if (reg instanceof final ConductanceDataPackRegistry<?> dataPackRegistry) {
					dataPackRegistry.freeze();
				}
			});
		}
	}

	public static ConductanceRegistrate getRegistrate() {
		return ApiBridge.registrate;
	}

	public static RegistryProviderImpl getRegs() {
		return ApiBridge.regs;
	}

	private ApiBridge() {
	}
}
