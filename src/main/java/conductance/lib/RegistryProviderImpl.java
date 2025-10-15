package conductance.lib;

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
import lombok.Getter;
import lombok.experimental.Accessors;
import conductance.api.coil.CoilBlockType;
import conductance.api.machine.MachineType;
import conductance.api.machine.multi.MultiBlockPartCapability;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.material.MaterialOreBearer;
import conductance.api.material.MaterialTraitKey;
import conductance.api.periodicelement.PeriodicElement;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.recipe.RecipeElementType;
import conductance.api.tier.Tier;
import conductance.api.util.RegistryProvider;
import conductance.Conductance;

@Accessors(fluent = true)
public final class RegistryProviderImpl implements RegistryProvider {

	private final Map<ResourceLocation, Registry<?>> loadOrder = new LinkedHashMap<>();
	private final AtomicBoolean frozen = new AtomicBoolean(true);
	private final Table<Registry<?>, ResourceLocation, Object> registerCache = HashBasedTable.create();

	private final @Getter ResourceKey<Registry<PeriodicElement>> periodicElementRegistry = this.makeKey("periodic_element");
	private final @Getter ResourceKey<Registry<MaterialFlag>> materialFlagRegistry = this.makeKey("material_flag");
	private final @Getter ResourceKey<Registry<MaterialTraitKey<?>>> materialTraitRegistry = this.makeKey("material_trait");
	private final @Getter ResourceKey<Registry<MaterialOreBearer>> materialOreBearerRegistry = this.makeKey("material_ore_bearer");
	private final @Getter ResourceKey<Registry<Material>> materialRegistry = this.makeKey("material");
	private final @Getter ResourceKey<Registry<MaterialGenerationHandler>> materialGenerationHandlerRegistry = this.makeKey("material_generation_handler");
	private final @Getter ResourceKey<Registry<Tier>> tierRegistry = this.makeKey("tier");
	private final @Getter ResourceKey<Registry<RecipeElementType<?>>> recipeElementTypeRegistry = this.makeKey("recipe_element_type");
	private final @Getter ResourceKey<Registry<MachineRecipeType>> recipeTypeRegistry = this.makeKey("recipe_type");
	private final @Getter ResourceKey<Registry<MultiBlockPartCapability>> multiBlockPartCapabilityRegistry = this.makeKey("multiblock_part_capability");
	private final @Getter ResourceKey<Registry<CoilBlockType>> coilBlockTypeRegistry = this.makeKey("coil_block_type");
	private final @Getter ResourceKey<Registry<MachineType<?>>> machineRegistry = this.makeKey("machine");

	private final @Getter Registry<PeriodicElement> periodicElements = this.makeRegistry(this.periodicElementRegistry);
	private final @Getter Registry<MaterialFlag> materialFlags = this.makeRegistry(this.materialFlagRegistry);
	private final @Getter Registry<MaterialTraitKey<?>> materialTraits = this.makeRegistry(this.materialTraitRegistry);
	private final @Getter Registry<MaterialOreBearer> materialOreBearers = this.makeRegistry(this.materialOreBearerRegistry);
	private final @Getter Registry<Material> materials = this.makeRegistry(this.materialRegistry);
	private final @Getter Registry<MaterialGenerationHandler> materialGenerationHandlers = this.makeRegistry(this.materialGenerationHandlerRegistry);
	private final @Getter Registry<Tier> tiers = this.makeRegistry(this.tierRegistry);
	private final @Getter Registry<RecipeElementType<?>> recipeElementTypes = this.makeRegistry(this.recipeElementTypeRegistry);
	private final @Getter Registry<MachineRecipeType> recipeTypes = this.makeRegistry(this.recipeTypeRegistry);
	private final @Getter Registry<MultiBlockPartCapability> multiBlockPartCapabilities = this.makeRegistry(this.multiBlockPartCapabilityRegistry);
	private final @Getter Registry<CoilBlockType> coilBlockTypes = this.makeRegistry(this.coilBlockTypeRegistry);
	private final @Getter Registry<MachineType<?>> machines = this.makeRegistry(this.machineRegistry);

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
}
