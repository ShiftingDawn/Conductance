package conductance.core;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import conductance.api.CAPI;
import conductance.api.NCMaterialGenerationHandlers;
import conductance.api.NCMaterials;

public final class CreativeTabHelper {

	@RequiredArgsConstructor
	public enum Tabs {
		GENERAL(Items.IRON_INGOT::getDefaultInstance),
		MATERIAL(() -> CAPI.materials().getItem(NCMaterials.ALUMINIUM, NCMaterialGenerationHandlers.INGOT, 1));

		private final @Getter String name = super.toString().toLowerCase(Locale.ROOT);
		private final @Getter Supplier<ItemStack> icon;
	}

	private static final EnumMap<Tabs, List<ItemLike>> CONTENT_MAPPING = new EnumMap<>(Tabs.class);

	public static void addToTab(final ItemLike item, final Tabs tab) {
		CreativeTabHelper.CONTENT_MAPPING.computeIfAbsent(tab, k -> new ArrayList<>()).add(item);
	}

	public static List<ItemLike> getTabContent(final Tabs tab) {
		return CreativeTabHelper.CONTENT_MAPPING.getOrDefault(tab, List.of());
	}

	private CreativeTabHelper() {
	}
}
