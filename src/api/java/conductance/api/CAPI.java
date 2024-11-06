package conductance.api;

import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.loading.FMLEnvironment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.Material;
import conductance.api.material.ResourceFinder;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.registry.RegistryProvider;
import conductance.api.registry.TaggedSetRegistry;
import conductance.api.registry.TranslationRegistry;
import conductance.api.util.GsonItemStackAdapter;
import conductance.api.util.TagHelper;

@SuppressWarnings({"unused", "NotNullFieldNotInitialized"})
public final class CAPI {

	public static final String MOD_ID = "conductance";

	// https://github.com/GregTechCEu/GregTech-Modern/blob/8c12553866d39783e6ac3a902eba1cc30598e955/src/main/java/com/gregtechceu/gtceu/api/GTValues.java#L35C1-L35C42
	public static final long UNIT = 3628800;

	public static final Gson GSON;

	private static RegistryProvider registryProvider;
	private static ResourceFinder resourceFinder;
	private static TaggedSetRegistry<Material, TaggedMaterialSet> materialRegistry;
	private static TranslationRegistry translationRegistry;
	private static final RegistryAccess REGISTRY_FALLBACK = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
	@Nullable
	private static RegistryAccess registryAccess;

	public static RegistryProvider regs() {
		return CAPI.registryProvider;
	}

	public static ResourceFinder resourceFinder() {
		return CAPI.resourceFinder;
	}

	public static TaggedSetRegistry<Material, TaggedMaterialSet> materials() {
		return CAPI.materialRegistry;
	}

	public static TranslationRegistry translations() {
		return CAPI.translationRegistry;
	}

	public static RegistryAccess frozenRegistry() {
		if (CAPI.registryAccess != null) {
			return CAPI.registryAccess;
		} else if (CAPI.isClient()) {
			if (Minecraft.getInstance().getConnection() != null) {
				return Minecraft.getInstance().getConnection().registryAccess();
			}
		}
		return CAPI.REGISTRY_FALLBACK;
	}

	public static boolean isClient() {
		return FMLEnvironment.dist.isClient();
	}

	public static final class Tags {

		public static final TagKey<Item> TAG_WRENCH = TagHelper.itemTagForMod("wrenches");
		public static final TagKey<Item> TAG_HAMMER = TagHelper.itemTagForMod("hammers");
		public static final TagKey<Item> TAG_WIRE_CUTTERS = TagHelper.itemTagForMod("wire_cutters");
	}

	static {
		GSON = new GsonBuilder()
				.registerTypeAdapter(ItemStack.class, GsonItemStackAdapter.INSTANCE)
				.registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer()).create();
	}

	private CAPI() {
	}
}
