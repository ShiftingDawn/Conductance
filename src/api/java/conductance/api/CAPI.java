package conductance.api;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.loading.FMLEnvironment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import conductance.api.machine.recipe.RecipeHelper;
import conductance.api.machine.sync.SyncHelper;
import conductance.api.material.MaterialRegistry;
import conductance.api.material.ResourceFinder;
import conductance.api.registry.RegistryProvider;
import conductance.api.registry.TranslationRegistry;
import conductance.api.util.GsonItemStackAdapter;
import conductance.api.util.TagHelper;
import conductance.api.util.tier.TierRegistry;

@SuppressWarnings({ "unused", "NotNullFieldNotInitialized" })
public final class CAPI {

	public static final String MOD_ID = "conductance";

	// https://github.com/GregTechCEu/GregTech-Modern/blob/8c12553866d39783e6ac3a902eba1cc30598e955/src/main/java/com/gregtechceu/gtceu/api/GTValues.java#L35C1-L35C42
	public static final long UNIT = 3628800;

	public static final Gson GSON;

	public static final RandomSource RANDOM = RandomSource.createNewThreadLocalInstance();

	private static RegistryProvider registryProvider;
	private static ResourceFinder resourceFinder;
	private static MaterialRegistry materialRegistry;
	private static TranslationRegistry translationRegistry;
	private static TierRegistry tierRegistry;
	private static RecipeHelper recipeHelper;
	private static SyncHelper syncHelper;

	public static RegistryProvider regs() {
		return CAPI.registryProvider;
	}

	public static ResourceFinder resourceFinder() {
		return CAPI.resourceFinder;
	}

	public static MaterialRegistry materials() {
		return CAPI.materialRegistry;
	}

	public static TranslationRegistry translations() {
		return CAPI.translationRegistry;
	}

	public static TierRegistry tiers() {
		return CAPI.tierRegistry;
	}

	public static RecipeHelper recipeHelper() {
		return CAPI.recipeHelper;
	}

	public static SyncHelper syncHelper() {
		return CAPI.syncHelper;
	}

	public static boolean isClient() {
		return FMLEnvironment.dist.isClient();
	}

	public static final class Tags {

		public static final TagKey<Item> TAG_WRENCH = TagHelper.itemTagForCommon("wrenches");
		public static final TagKey<Item> TAG_HAMMER = TagHelper.itemTagForCommon("hammers");
		public static final TagKey<Item> TAG_WIRE_CUTTERS = TagHelper.itemTagForCommon("wire_cutters");
	}

	public static final Map<DyeColor, Integer> COLORS = Collections.unmodifiableMap(Util.make(new EnumMap<>(DyeColor.class), map -> {
		map.put(DyeColor.WHITE, 0xFFF9FFFE);
		map.put(DyeColor.ORANGE, 0xFFF9801D);
		map.put(DyeColor.MAGENTA, 0xFFC354CD);
		map.put(DyeColor.LIGHT_BLUE, 0xFF3AB3DA);
		map.put(DyeColor.YELLOW, 0xFFFED83D);
		map.put(DyeColor.LIME, 0xFF80C71F);
		map.put(DyeColor.PINK, 0xFFF38BAA);
		map.put(DyeColor.GRAY, 0xFF474F52);
		map.put(DyeColor.LIGHT_GRAY, 0xFF9D9D97);
		map.put(DyeColor.CYAN, 0xFF169C9C);
		map.put(DyeColor.PURPLE, 0xFF8932B8);
		map.put(DyeColor.BLUE, 0xFF3C44AA);
		map.put(DyeColor.BROWN, 0xFF835432);
		map.put(DyeColor.GREEN, 0xFF5E7C16);
		map.put(DyeColor.RED, 0xFFB02E26);
		map.put(DyeColor.BLACK, 0xFF1D1D21);
	}));

	static {
		GSON = new GsonBuilder()
				.registerTypeAdapter(ItemStack.class, GsonItemStackAdapter.INSTANCE)
				.registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer()).create();
	}

	private CAPI() {
	}
}
