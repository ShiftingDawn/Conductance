package conductance.core.datapack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagLoader;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import conductance.content.block.IConductanceBlock;
import conductance.core.apiimpl.ApiBridge;
import conductance.core.apiimpl.PluginManager;
import conductance.core.apiimpl.TaggedSetImpl;
import conductance.core.register.MaterialRegistry;

public class TagGenerationHandler {

	private static final String TAG_SOURCE = "Conductance Runtime Tags";

	private static final HashMap<TagKey<Item>, List<ItemLike>> CUSTOM_ITEM_TAGS = new HashMap<>();

	public static void register(final TagKey<Item> tagKey, final ItemLike item) {
		TagGenerationHandler.CUSTOM_ITEM_TAGS.computeIfAbsent(tagKey, k -> new ArrayList<>()).add(item);
	}

	public static void addEntriesToTagMap(final Registry<?> registry, final Map<ResourceLocation, List<TagLoader.EntryWithSource>> tagMap) {
		if (registry == BuiltInRegistries.ITEM) {
			TagGenerationHandler.CUSTOM_ITEM_TAGS.clear();
			PluginManager.dispatchTagRegister();
			TagGenerationHandler.addItemEntriesToTagMap(tagMap);
		} else if (registry == BuiltInRegistries.BLOCK) {
			TagGenerationHandler.addBlockEntriesToTagMap(tagMap);
		} else if (registry == BuiltInRegistries.FLUID) {
			TagGenerationHandler.addFluidEntriesToTagMap(tagMap);
		}
	}

	private static void addItemEntriesToTagMap(final Map<ResourceLocation, List<TagLoader.EntryWithSource>> tagMap) {
		MaterialRegistry.INSTANCE.getItemTable().rowMap().forEach((taggedSet, map) -> map.forEach((material, items) -> items.forEach(item -> {
			taggedSet.streamAllItemTags(material).forEach(tagKey -> {
				tagMap.computeIfAbsent(tagKey.location(), k -> new ArrayList<>()).add(new TagLoader.EntryWithSource(TagEntry.element(BuiltInRegistries.ITEM.getKey(item)), TagGenerationHandler.TAG_SOURCE));
			});
		})));
		MaterialRegistry.INSTANCE.getBlockTable().rowMap().forEach((taggedSet, map) -> map.forEach((material, blocks) -> blocks.forEach(block -> {
			taggedSet.streamAllItemTags(material).forEach(tagKey -> {
				tagMap.computeIfAbsent(tagKey.location(), k -> new ArrayList<>()).add(new TagLoader.EntryWithSource(TagEntry.element(BuiltInRegistries.BLOCK.getKey(block)), TagGenerationHandler.TAG_SOURCE));
			});
		})));
		TagGenerationHandler.CUSTOM_ITEM_TAGS.forEach((taggedSet, items) -> {
			final List<TagLoader.EntryWithSource> tags = new ArrayList<>();
			items.forEach(item -> tags.add(new TagLoader.EntryWithSource(TagEntry.element(BuiltInRegistries.ITEM.getKey(item.asItem())), TagGenerationHandler.TAG_SOURCE)));
			tagMap.computeIfAbsent(taggedSet.location(), k -> new ArrayList<>()).addAll(tags);
		});
	}

	private static void addBlockEntriesToTagMap(final Map<ResourceLocation, List<TagLoader.EntryWithSource>> tagMap) {
		ApiBridge.REGISTRATE.getAll(Registries.BLOCK).forEach(blockEntry -> {
			if (blockEntry.get() instanceof final IConductanceBlock conductanceBlock) {
				tagMap.computeIfAbsent(conductanceBlock.getMiningToolTag().location(), k -> new ArrayList<>()).add(new TagLoader.EntryWithSource(TagEntry.element(blockEntry.getId()), TagGenerationHandler.TAG_SOURCE));
			}
		});
		MaterialRegistry.INSTANCE.getBlockTable().rowMap().forEach((taggedSet, map) -> map.forEach((material, blocks) -> blocks.forEach(block -> {
			final ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
			taggedSet.streamAllBlockTags(material).forEach(tagKey -> {
				tagMap.computeIfAbsent(tagKey.location(), k -> new ArrayList<>()).add(new TagLoader.EntryWithSource(TagEntry.element(blockId), TagGenerationHandler.TAG_SOURCE));
			});
			// Mining tool tags
			tagMap.computeIfAbsent(material.getRequiredToolTag().location(), k -> new ArrayList<>()).add(new TagLoader.EntryWithSource(TagEntry.element(blockId), TagGenerationHandler.TAG_SOURCE));
			if (!((TaggedSetImpl<?>) taggedSet).getMiningTags().isEmpty()) {
				((TaggedSetImpl<?>) taggedSet).getMiningTags().forEach(tagKey -> {
					tagMap.computeIfAbsent(tagKey.location(), k -> new ArrayList<>()).add(new TagLoader.EntryWithSource(TagEntry.element(blockId), TagGenerationHandler.TAG_SOURCE));
				});
			}
		})));
	}

	private static void addFluidEntriesToTagMap(final Map<ResourceLocation, List<TagLoader.EntryWithSource>> tagMap) {
		// TODO Fluids
//		MaterialRegistryImpl.INSTANCE.getFluidTable().values().forEach(fluids -> fluids.forEach(fluid -> {
//			final ResourceLocation fluidName = BuiltInRegistries.FLUID.getKey(fluid);
//			final ResourceLocation tagName = TagHelper.tagForPlatform(BuiltInRegistries.FLUID, fluidName.getPath(), fluidName.getPath()).location();
//			tagMap.computeIfAbsent(tagName, k -> new ArrayList<>()).add(new TagLoader.EntryWithSource(TagEntry.element(fluidName), TagGenerationHandler.TAG_SOURCE));
//		}));
	}
}
