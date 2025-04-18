package conductance.api.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;

@Getter
public enum InteractType {

	WRENCH(CAPI.Tags.TAG_WRENCH),
	WIRE_CUTTERS(CAPI.Tags.TAG_WIRE_CUTTERS),
	HAMMER(CAPI.Tags.TAG_HAMMER);

	private final TagKey<Item> toolTag;

	InteractType(final TagKey<Item> toolTag) {
		this.toolTag = toolTag;
	}

	public boolean is(final ItemStack stack) {
		return stack.is(this.toolTag);
	}

	@Nullable
	public static InteractType findTypeForStack(final ItemStack stack) {
		for (final InteractType interactType : InteractType.values()) {
			if (interactType.is(stack)) {
				return interactType;
			}
		}
		return null;
	}
}
