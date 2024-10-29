package conductance.api.util;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import conductance.api.CAPI;

public final class TagHelper {

	// region Item Tags
	public static TagKey<Item> itemTagForLoader(final String path) {
		return TagHelper.tagForLoader(BuiltInRegistries.ITEM, path);
	}

	public static TagKey<Item> itemTagForCommon(final String path) {
		return TagHelper.tagForCommon(BuiltInRegistries.ITEM, path);
	}

	public static TagKey<Item> itemTagForMod(final String path) {
		return TagHelper.tagForMod(BuiltInRegistries.ITEM, path);
	}

	public static TagKey<Item> itemTagForVanilla(final String path) {
		return TagHelper.tagForVanilla(BuiltInRegistries.ITEM, path);
	}
	// endregion

	// region Block Tags
	public static TagKey<Block> blockTagForLoader(final String path) {
		return TagHelper.tagForLoader(BuiltInRegistries.BLOCK, path);
	}

	public static TagKey<Block> blockTagForCommon(final String path) {
		return TagHelper.tagForCommon(BuiltInRegistries.BLOCK, path);
	}

	public static TagKey<Block> blockTagForMod(final String path) {
		return TagHelper.tagForMod(BuiltInRegistries.BLOCK, path);
	}

	public static TagKey<Block> blockTagForVanilla(final String path) {
		return TagHelper.tagForVanilla(BuiltInRegistries.BLOCK, path);
	}
	// endregion

	// region Helper
	public static <T> TagKey<T> tagForLoader(final Registry<T> registry, final String path) {
		return TagKey.create(registry.key(), ResourceLocation.fromNamespaceAndPath("neoforge", path));
	}

	public static <T> TagKey<T> tagForCommon(final Registry<T> registry, final String path) {
		return TagKey.create(registry.key(), ResourceLocation.fromNamespaceAndPath("c", path));
	}

	public static <T> TagKey<T> tagForMod(final Registry<T> registry, final String path) {
		return TagKey.create(registry.key(), ResourceLocation.fromNamespaceAndPath(CAPI.MOD_ID, path));
	}

	public static <T> TagKey<T> tagForVanilla(final Registry<T> registry, final String path) {
		return TagKey.create(registry.key(), ResourceLocation.withDefaultNamespace(path));
	}
	// endregion

	private TagHelper() {
	}
}
