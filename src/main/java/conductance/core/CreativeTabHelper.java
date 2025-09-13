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

public final class CreativeTabHelper {

	@RequiredArgsConstructor
	public enum Tabs {
		GENERAL(Items.IRON_INGOT::getDefaultInstance);

		private final @Getter String name = super.toString().toLowerCase(Locale.ROOT);
		private final @Getter Supplier<ItemStack> icon;
	}

	private static final EnumMap<Tabs, List<ItemLike>> CONTENT_MAPPING = new EnumMap<>(Tabs.class);

	public static void addToTab(final ItemLike item, final Tabs tab) {
		CreativeTabHelper.CONTENT_MAPPING.computeIfAbsent(tab, k -> new ArrayList<>()).add(item);
	}

	public static List<ItemLike> getTabContent(final Tabs tab) {
		return CreativeTabHelper.CONTENT_MAPPING.get(tab);
	}

	private CreativeTabHelper() {
	}
}
