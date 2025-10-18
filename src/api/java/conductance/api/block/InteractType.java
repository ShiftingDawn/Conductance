package conductance.api.block;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.NCSoundEvents;

@Getter
public enum InteractType {

	WRENCH(CAPI.TAG_WRENCHES, () -> NCSoundEvents.TOOL_WRENCH.get()),
	WIRE_CUTTERS(CAPI.TAG_WIRE_CUTTERS, () -> NCSoundEvents.TOOL_WIRE_CUTTERS.get()),
	HAMMER(CAPI.TAG_HAMMERS, () -> NCSoundEvents.TOOL_HAMMER.get()),
	CROWBAR(CAPI.TAG_CROWBARS, () -> NCSoundEvents.TOOL_CROWBAR.get());

	private final TagKey<Item> toolTag;
	private final @Nullable Supplier<SoundEvent> sound;

	InteractType(final TagKey<Item> toolTag, @Nullable final Supplier<SoundEvent> sound) {
		this.toolTag = toolTag;
		this.sound = sound;
	}

	public boolean is(final ItemStack stack) {
		return stack.is(this.toolTag);
	}

	public void playSound(final UseOnContext ctx) {
		if (this.sound != null) {
			BlockHelper.playSound(ctx, this.sound.get());
		}
	}

	public void playSound(final Level level, final Entity entity, final BlockPos pos) {
		if (this.sound != null) {
			BlockHelper.playSound(level, entity, this.sound.get(), pos);
		}
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
