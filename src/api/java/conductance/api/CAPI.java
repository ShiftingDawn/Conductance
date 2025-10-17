package conductance.api;

import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import conductance.api.coil.CoilBlockRegistry;
import conductance.api.material.MaterialRegistry;
import conductance.api.recipe.RecipeHelper;
import conductance.api.resource.ResourceFinder;
import conductance.api.tier.TierRegistry;
import conductance.api.util.RegistryProvider;

@SuppressWarnings({"unused", "NotNullFieldNotInitialized"})
public final class CAPI {

	public static final String MOD_ID = "conductance";
	public static final long UNIT = 3628800;
	public static final int BUCKET = FluidType.BUCKET_VOLUME;
	public static final int INGOT = 144;
	public static final int STEAM_TO_POWER_RATIO = 2;
	public static final RandomSource RANDOM = RandomSource.createNewThreadLocalInstance();
	public static final Gson GSON;
	public static final TagKey<Item> TAG_WRENCHES = Tags.Items.TOOLS_WRENCH;
	public static final TagKey<Item> TAG_WIRE_CUTTERS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(CAPI.MOD_ID, "tools/wire_cutter"));
	public static final TagKey<Item> TAG_HAMMERS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(CAPI.MOD_ID, "tools/hammer"));
	public static final TagKey<Item> TAG_CROWBARS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(CAPI.MOD_ID, "tools/crowbar"));

	private static RegistryProvider registryProvider;
	private static MaterialRegistry materialRegistry;
	private static TierRegistry tierRegistry;
	private static CoilBlockRegistry coilBlockRegistry;
	private static ResourceFinder resourceFinder;
	private static RecipeHelper recipeHelper;

	public static RegistryProvider regs() {
		return Objects.requireNonNull(CAPI.registryProvider, "CAPI::regs called too early!");
	}

	public static MaterialRegistry materials() {
		return Objects.requireNonNull(CAPI.materialRegistry, "CAPI::materials called too early!");
	}

	public static TierRegistry tiers() {
		return Objects.requireNonNull(CAPI.tierRegistry, "CAPI::tiers called too early!");
	}

	public static CoilBlockRegistry coils() {
		return Objects.requireNonNull(CAPI.coilBlockRegistry, "CAPI::coils called too early!");
	}

	public static ResourceFinder resourceFinder() {
		return Objects.requireNonNull(CAPI.resourceFinder, "CAPI::resourceFinder called too early!");
	}

	public static RecipeHelper recipeHelper() {
		return Objects.requireNonNull(CAPI.recipeHelper, "CAPI::recipeHelper called too early!");
	}

	public static boolean isClient() {
		return FMLEnvironment.dist.isClient();
	}

	@Contract("!null, _ -> !null; null, _ -> null")
	public static @Nullable <T> T make(@Nullable final T object, final Consumer<? super @NotNull T> consumer) {
		if (object != null) {
			consumer.accept(object);
		}
		return object;
	}

	static {
		GSON = new GsonBuilder().create();
	}

	private CAPI() {
	}
}
