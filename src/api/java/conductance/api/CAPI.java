package conductance.api;

import java.util.Objects;
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
import conductance.api.material.MaterialRegistry;
import conductance.api.recipe.RecipeHelper;
import conductance.api.registry.RegistryProvider;
import conductance.api.resource.ResourceFinder;
import conductance.api.tier.TierRegistry;

@SuppressWarnings({"unused", "NotNullFieldNotInitialized"})
public final class CAPI {

	public static final String MOD_ID = "conductance";
	public static final long UNIT = 3628800;
	public static final int BUCKET = FluidType.BUCKET_VOLUME;
	public static final RandomSource RANDOM = RandomSource.createNewThreadLocalInstance();
	public static final Gson GSON;
	public static final TagKey<Item> TAG_WRENCHES = Tags.Items.TOOLS_WRENCH;
	public static final TagKey<Item> TAG_HAMMERS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(CAPI.MOD_ID, "tools/hammer"));
	public static final TagKey<Item> TAG_WIRE_CUTTERS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(CAPI.MOD_ID, "tools/wire_cutter"));

	private static RegistryProvider registryProvider;
	private static MaterialRegistry materialRegistry;
	private static TierRegistry tierRegistry;
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

	public static ResourceFinder resourceFinder() {
		return Objects.requireNonNull(CAPI.resourceFinder, "CAPI::resourceFinder called too early!");
	}

	public static RecipeHelper recipeHelper() {
		return Objects.requireNonNull(CAPI.recipeHelper, "CAPI::recipeHelper called too early!");
	}

	public static boolean isClient() {
		return FMLEnvironment.dist.isClient();
	}

	static {
		GSON = new GsonBuilder().create();
	}

	private CAPI() {
	}
}
