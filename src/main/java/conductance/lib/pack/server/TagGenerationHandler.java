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
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagLoader;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import conductance.api.NCMaterialProps;
import conductance.Conductance;

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
		Conductance.MATERIALS.getItemTable().rowMap().forEach((material, map) -> map.forEach((handler, item) -> {
			if (handler == null || item == null || material == null) {
				return;
			}
			for (final TagKey<Item> entryTag : handler.getEntryTags(BuiltInRegistries.ITEM, material)) {
				tagMap.computeIfAbsent(entryTag.location(), k -> new ArrayList<>())
					.add(new TagLoader.EntryWithSource(TagEntry.element(BuiltInRegistries.ITEM.getKey(item)), TagGenerationHandler.TAG_SOURCE));
			}
			for (final TagKey<Item> entryTag : handler.getGroupTags(BuiltInRegistries.ITEM, material)) {
				tagMap.computeIfAbsent(entryTag.location(), k -> new ArrayList<>())
					.add(new TagLoader.EntryWithSource(TagEntry.element(BuiltInRegistries.ITEM.getKey(item)), TagGenerationHandler.TAG_SOURCE));
			}
		}));
		Conductance.MATERIALS.getBlockTable().rowMap().forEach((material, map) -> map.forEach((handler, block) -> {
			if (handler == null || block == null || material == null) {
				return;
			}
			for (final TagKey<Item> entryTag : handler.getEntryTags(BuiltInRegistries.ITEM, material)) {
				tagMap.computeIfAbsent(entryTag.location(), k -> new ArrayList<>())
					.add(new TagLoader.EntryWithSource(TagEntry.element(BuiltInRegistries.BLOCK.getKey(block)), TagGenerationHandler.TAG_SOURCE));
			}
			for (final TagKey<Item> groupTag : handler.getGroupTags(BuiltInRegistries.ITEM, material)) {
				tagMap.computeIfAbsent(groupTag.location(), k -> new ArrayList<>())
					.add(new TagLoader.EntryWithSource(TagEntry.element(BuiltInRegistries.BLOCK.getKey(block)), TagGenerationHandler.TAG_SOURCE));
			}
		}));
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
		//TODO mining tool tags

		//		RegisterCore.REGISTRATE.getAll(Registries.BLOCK).forEach(blockEntry -> {
		//			if (blockEntry.get() instanceof final IConductanceBlock conductanceBlock) {
		//				tagMap.computeIfAbsent(conductanceBlock.getMiningToolTag().location(), k -> new ArrayList<>())
		//						.add(new TagLoader.EntryWithSource(TagEntry.element(blockEntry.getId()), TagGenerationHandler.TAG_SOURCE));
		//			}
		//		});
		Conductance.MATERIALS.getBlockTable().rowMap().forEach((material, map) -> map.forEach((handler, block) -> {
			if (handler == null || block == null || material == null) {
				return;
			}
			final ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
			for (final TagKey<Block> entryTag : handler.getEntryTags(BuiltInRegistries.BLOCK, material)) {
				tagMap.computeIfAbsent(entryTag.location(), k -> new ArrayList<>())
					.add(new TagLoader.EntryWithSource(TagEntry.element(blockId), TagGenerationHandler.TAG_SOURCE));
			}
			for (final TagKey<Block> entryTag : handler.getGroupTags(BuiltInRegistries.BLOCK, material)) {
				tagMap.computeIfAbsent(entryTag.location(), k -> new ArrayList<>())
					.add(new TagLoader.EntryWithSource(TagEntry.element(blockId), TagGenerationHandler.TAG_SOURCE));
			}
			tagMap.computeIfAbsent(material.getProp(NCMaterialProps.REQUIRED_TOOL_LEVEL, BlockTags.NEEDS_STONE_TOOL).location(), k -> new ArrayList<>())
				.add(new TagLoader.EntryWithSource(TagEntry.element(blockId), TagGenerationHandler.TAG_SOURCE));
			handler.getMiningToolTypeTags().forEach(tagKey -> {
				tagMap.computeIfAbsent(tagKey.location(), k -> new ArrayList<>())
					.add(new TagLoader.EntryWithSource(TagEntry.element(blockId), TagGenerationHandler.TAG_SOURCE));
			});
		}));
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
