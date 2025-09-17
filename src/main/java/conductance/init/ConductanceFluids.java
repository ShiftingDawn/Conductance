package conductance.init;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import conductance.api.CAPI;
import conductance.api.material.Material;
import conductance.Conductance;
import conductance.init.fluid.ConductanceBucketItem;
import conductance.init.fluid.ConductanceFluid;
import conductance.init.fluid.MaterialClientFluidTypeExtensions;
import conductance.init.fluid.MaterialFluidType;

public final class ConductanceFluids {

	private static final DeferredRegister<FluidType> REGISTRY = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, Conductance.MODID);
	private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, Conductance.MODID);
	private static final DeferredRegister.Items BUCKETS = DeferredRegister.createItems(Conductance.MODID);
	private static final Map<Supplier<FluidType>, Supplier<Item>> BUCKET_ITEMS = new ConcurrentHashMap<>();

	public static void initialize(final IEventBus modEventBus) {
		ConductanceFluids.REGISTRY.register(modEventBus);
		ConductanceFluids.FLUIDS.register(modEventBus);
		ConductanceFluids.BUCKETS.register(modEventBus);
		CAPI.regs().materials().forEach(ConductanceFluids::generateMaterial);
		modEventBus.addListener(RegisterClientExtensionsEvent.class, ConductanceFluids::onRegisterClientExtensions);
	}

	private static void generateMaterial(final Material material) {
		CAPI.regs().materialGenerationHandlers().stream()
				.filter(handler -> handler.hasFluid() && handler.autoGenerateFluid() && handler.test(material) && !Conductance.MATERIALS.hasFluidOverride(material, handler))
				.forEach(handler -> {
					final String name = handler.getUnlocalizedName(material);
					final Supplier<FluidType> fluidType = ConductanceFluids.REGISTRY.register(name, () -> new MaterialFluidType(
							FluidType.Properties.create(), material, handler
					));
					final Supplier<Fluid> fluid = ConductanceFluids.FLUIDS.register(name, () -> new ConductanceFluid(fluidType, () -> ConductanceFluids.BUCKET_ITEMS.get(fluidType).get(), null));
					final Supplier<Item> bucket = ConductanceFluids.BUCKETS.registerItem(name + "_bucket", props -> new ConductanceBucketItem(fluid.get(), props));
					ConductanceFluids.BUCKET_ITEMS.put(fluidType, bucket);
				});
	}

	private static void onRegisterClientExtensions(final RegisterClientExtensionsEvent event) {
		for (final DeferredHolder<FluidType, ? extends FluidType> fluidType : ConductanceFluids.REGISTRY.getEntries()) {
			if (fluidType.get() instanceof final MaterialFluidType type) {
				event.registerFluidType(new MaterialClientFluidTypeExtensions(type), type);
			}
		}
	}

	private ConductanceFluids() {
	}
}
