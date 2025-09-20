package conductance.init;

import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import com.google.gson.JsonObject;
import conductance.api.CAPI;
import conductance.api.material.Material;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.api.resource.event.AddTranslationEvent;
import conductance.Conductance;
import conductance.init.fluid.ConductanceFluid;
import conductance.init.fluid.MaterialBucketItem;
import conductance.init.fluid.MaterialClientFluidTypeExtensions;
import conductance.init.fluid.MaterialFluidType;

@ConductancePluginListener(modid = Conductance.MODID)
public final class ConductanceFluids {

	private static final DeferredRegister<FluidType> REGISTRY = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, Conductance.MODID);
	private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, Conductance.MODID);
	private static final DeferredRegister.Items BUCKETS = DeferredRegister.createItems(Conductance.MODID);

	public static void initialize(final IEventBus modEventBus) {
		ConductanceFluids.REGISTRY.register(modEventBus);
		ConductanceFluids.FLUIDS.register(modEventBus);
		ConductanceFluids.BUCKETS.register(modEventBus);
		CAPI.regs().materials().forEach(ConductanceFluids::generateMaterial);
		modEventBus.addListener(RegisterClientExtensionsEvent.class, ConductanceFluids::onRegisterClientExtensions);
		modEventBus.addListener(RegisterCapabilitiesEvent.class, ConductanceFluids::onRegisterCapabilities);
	}

	private static void generateMaterial(final Material material) {
		CAPI.regs().materialGenerationHandlers().stream()
			.filter(handler -> handler.hasFluid() && handler.autoGenerateFluid() && handler.test(material) && !Conductance.MATERIALS.hasFluidOverride(material, handler))
			.forEach(handler -> {
				final String name = handler.getUnlocalizedName(material);
				final Supplier<FluidType> fluidType = ConductanceFluids.REGISTRY.register(name, () -> {
					FluidType.Properties props = FluidType.Properties.create();
					if (handler.getFluidBuilderCallback() != null) {
						props = handler.getFluidBuilderCallback().apply(material, props);
					}
					return new MaterialFluidType(props, material, handler);
				});
				final Supplier<Fluid> fluid = ConductanceFluids.FLUIDS.register(name, () -> {
					final ConductanceFluid result = new ConductanceFluid(fluidType, () -> CAPI.materials().getItem(material, handler), null);
					Conductance.MATERIALS.register(material, handler, result);
					return result;
				});
				ConductanceFluids.BUCKETS.registerItem(name + "_bucket", props -> Util.make(new MaterialBucketItem(fluid.get(), props, material, handler), bucketItem -> {
					Conductance.MATERIALS.register(material, handler, bucketItem);
				}));
			});
	}

	private static void onRegisterClientExtensions(final RegisterClientExtensionsEvent event) {
		for (final DeferredHolder<FluidType, ? extends FluidType> fluidType : ConductanceFluids.REGISTRY.getEntries()) {
			if (fluidType.get() instanceof final MaterialFluidType type) {
				event.registerFluidType(new MaterialClientFluidTypeExtensions(type), type);
			}
		}
	}

	@EventListener(priority = -100)
	private static void addFluidTranslations(final AddTranslationEvent event) {
		Conductance.MATERIALS.getFluidTable().rowMap().forEach((material, map) -> map.forEach((handler, fluid) -> {
			if (material == null || handler == null || fluid == null) {
				return;
			}
			handler.getGroupTagsAndTranslators(BuiltInRegistries.FLUID, material).forEach((tagKey, translator) -> {
				if (translator != null) {
					final String translation = translator.translate(material);
					if (translation != null) {
						event.add(tagKey, translation.formatted(material.getName()));
					}
				}
			});
		}));
	}

	@EventListener(priority = -100)
	private static void addFluidModels(final AddRuntimeModelEvent event) {
		ConductanceFluids.BUCKETS.getEntries().stream().map(DeferredHolder::get).filter(item -> item instanceof MaterialBucketItem).forEach(item -> {
			final MaterialBucketItem bucket = (MaterialBucketItem) item;
			event.addItemsModel(bucket, b -> b.custom(ResourceLocation.fromNamespaceAndPath(NeoForgeVersion.MOD_ID, "fluid_container"), b2 -> b2
				.addProperty("fluid", BuiltInRegistries.FLUID.getKey(bucket.content))
				.addProperty("flip_gas", true)
				.addProperty("cover_is_mask", true)
				.addProperty("textures", Util.make(new JsonObject(), json -> {
					json.addProperty("particle", ResourceLocation.withDefaultNamespace("item/bucket").toString());
					json.addProperty("base", ResourceLocation.withDefaultNamespace("item/bucket").toString());
					json.addProperty("fluid", ResourceLocation.fromNamespaceAndPath(NeoForgeVersion.MOD_ID, "item/mask/bucket_fluid").toString());
					json.addProperty("cover", ResourceLocation.fromNamespaceAndPath(NeoForgeVersion.MOD_ID, "item/mask/bucket_fluid_cover").toString());
				}))
			));
		});
	}

	private static void onRegisterCapabilities(final RegisterCapabilitiesEvent event) {
		ConductanceFluids.BUCKETS.getEntries().stream().map(DeferredHolder::get)
			.filter(item -> item instanceof MaterialBucketItem)
			.forEach(item -> {
				event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new FluidBucketWrapper(stack), item);
			});
	}

	private ConductanceFluids() {
	}
}
