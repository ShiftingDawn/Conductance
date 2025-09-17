package conductance.lib.pack.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagLoader;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

final class TagGenerationHandler {

	private static final String TAG_SOURCE = "Conductance Runtime Tags";
	static final Map<TagKey<Item>, List<ItemLike>> CUSTOM_ITEM_TAGS = new ConcurrentHashMap<>();
	static final Map<TagKey<Item>, List<ResourceLocation>> CUSTOM_REQUIRED_TAGS = new ConcurrentHashMap<>();
	static final Map<TagKey<Item>, List<ResourceLocation>> CUSTOM_OPTIONAL_TAGS = new ConcurrentHashMap<>();

	static void addEntriesToTagMap(final ResourceKey<? extends Registry<?>> registry, final Map<ResourceLocation, List<TagLoader.EntryWithSource>> tagMap) {
		if (registry == Registries.ITEM) {
			TagGenerationHandler.addItemEntriesToTagMap(tagMap);
		} else if (registry == Registries.BLOCK) {
			TagGenerationHandler.addBlockEntriesToTagMap(tagMap);
		} else if (registry == Registries.FLUID) {
			TagGenerationHandler.addFluidEntriesToTagMap(tagMap);
		}
	}

	private static void addItemEntriesToTagMap(final Map<ResourceLocation, List<TagLoader.EntryWithSource>> tagMap) {
//		MaterialRegistryImpl.INSTANCE.getItemTable().rowMap().forEach((taggedSet, map) -> map.forEach((material, items) -> items.forEach(item -> {
//			taggedSet.streamAllItemTags(material).forEach(tagKey -> {
//				tagMap.computeIfAbsent(tagKey.location(), k -> new ArrayList<>())
//						.add(new TagLoader.EntryWithSource(TagEntry.element(BuiltInRegistries.ITEM.getKey(item)), TagGenerationHandler.TAG_SOURCE));
//			});
//		})));
//		MaterialRegistryImpl.INSTANCE.getBlockTable().rowMap().forEach((taggedSet, map) -> map.forEach((material, blocks) -> blocks.forEach(block -> {
//			taggedSet.streamAllItemTags(material).forEach(tagKey -> {
//				tagMap.computeIfAbsent(tagKey.location(), k -> new ArrayList<>())
//						.add(new TagLoader.EntryWithSource(TagEntry.element(BuiltInRegistries.BLOCK.getKey(block)), TagGenerationHandler.TAG_SOURCE));
//			});
//		})));
		TagGenerationHandler.CUSTOM_ITEM_TAGS.forEach((tagKey, items) -> {
			final List<TagLoader.EntryWithSource> tags = tagMap.computeIfAbsent(tagKey.location(), k -> new ArrayList<>());
			items.forEach(item -> tags.add(new TagLoader.EntryWithSource(TagEntry.element(BuiltInRegistries.ITEM.getKey(item.asItem())), TagGenerationHandler.TAG_SOURCE)));
		});
		TagGenerationHandler.CUSTOM_REQUIRED_TAGS.forEach((tagKey, locations) -> {
			final List<TagLoader.EntryWithSource> tags = tagMap.computeIfAbsent(tagKey.location(), k -> new ArrayList<>());
			locations.forEach(location -> tags.add(new TagLoader.EntryWithSource(TagEntry.tag(location), TagGenerationHandler.TAG_SOURCE)));
		});
		TagGenerationHandler.CUSTOM_OPTIONAL_TAGS.forEach((tagKey, locations) -> {
			final List<TagLoader.EntryWithSource> tags = tagMap.computeIfAbsent(tagKey.location(), k -> new ArrayList<>());
			locations.forEach(location -> tags.add(new TagLoader.EntryWithSource(TagEntry.optionalTag(location), TagGenerationHandler.TAG_SOURCE)));
		});
	}

	private static void addBlockEntriesToTagMap(final Map<ResourceLocation, List<TagLoader.EntryWithSource>> tagMap) {
//		RegisterCore.REGISTRATE.getAll(Registries.BLOCK).forEach(blockEntry -> {
//			if (blockEntry.get() instanceof final IConductanceBlock conductanceBlock) {
//				tagMap.computeIfAbsent(conductanceBlock.getMiningToolTag().location(), k -> new ArrayList<>())
//						.add(new TagLoader.EntryWithSource(TagEntry.element(blockEntry.getId()), TagGenerationHandler.TAG_SOURCE));
//			}
//		});
//		MaterialRegistryImpl.INSTANCE.getBlockTable().rowMap().forEach((taggedSet, map) -> map.forEach((material, blocks) -> blocks.forEach(block -> {
//			final ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
//			taggedSet.streamAllBlockTags(material).forEach(tagKey -> {
//				tagMap.computeIfAbsent(tagKey.location(), k -> new ArrayList<>())
//						.add(new TagLoader.EntryWithSource(TagEntry.element(blockId), TagGenerationHandler.TAG_SOURCE));
//			});
//			// Mining tool tags
//			tagMap.computeIfAbsent(material.getBlockRequiredToolTag().location(), k -> new ArrayList<>())
//					.add(new TagLoader.EntryWithSource(TagEntry.element(blockId), TagGenerationHandler.TAG_SOURCE));
//			if (!((TaggedSetImpl<?>) taggedSet).getMiningTags().isEmpty()) {
//				((TaggedSetImpl<?>) taggedSet).getMiningTags().forEach(tagKey -> {
//					tagMap.computeIfAbsent(tagKey.location(), k -> new ArrayList<>())
//							.add(new TagLoader.EntryWithSource(TagEntry.element(blockId), TagGenerationHandler.TAG_SOURCE));
//				});
//			}
//		})));
	}

	private static void addFluidEntriesToTagMap(final Map<ResourceLocation, List<TagLoader.EntryWithSource>> tagMap) {
//		MaterialRegistryImpl.INSTANCE.getFluidTable().rowMap().forEach((taggedSet, map) -> map.forEach((material, fluids) -> fluids.forEach(fluid -> {
//			final ResourceLocation fluidId = BuiltInRegistries.FLUID.getKey(fluid);
//			taggedSet.streamAllFluidTags(material).forEach(tagKey -> {
//				tagMap.computeIfAbsent(tagKey.location(), k -> new ArrayList<>())
//						.add(new TagLoader.EntryWithSource(TagEntry.element(fluidId), TagGenerationHandler.TAG_SOURCE));
//			});
//		})));
	}

	private TagGenerationHandler() {
	}
}
