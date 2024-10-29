package conductance.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.CreativeModeTabModifier;
import com.tterrag.registrate.util.entry.RegistryEntry;
import conductance.api.registry.NCRegistrate;
import conductance.Conductance;

public final class ConductanceRegistrate extends Registrate implements NCRegistrate {

	private ConductanceRegistrate() {
		super(Conductance.MODID);
	}

	public static ConductanceRegistrate create(final IEventBus modEventBus) {
		return Util.make(new ConductanceRegistrate(), registrate -> registrate.registerEventListeners(modEventBus));
	}

	// region Creative Tabs
	private final Map<RegistryEntry<CreativeModeTab, CreativeModeTab>, List<RegistryEntry<?, ?>>> creativeTabLookup = Collections.synchronizedMap(new IdentityHashMap<>());

	public void setCreativeTab(RegistryEntry<?, ?> entry, RegistryEntry<CreativeModeTab, CreativeModeTab> tab) {
		this.creativeTabLookup.computeIfAbsent(tab, k -> Collections.synchronizedList(new ArrayList<>())).add(entry);
	}

	public List<RegistryEntry<?, ?>> getContentsForTab(RegistryEntry<CreativeModeTab, CreativeModeTab> tab) {
		return this.creativeTabLookup.getOrDefault(tab, List.of());
	}
	// endregion

	@Override
	public Registrate modifyCreativeModeTab(ResourceKey<CreativeModeTab> creativeModeTab, Consumer<CreativeModeTabModifier> modifier) {
		return this;
	}
}
