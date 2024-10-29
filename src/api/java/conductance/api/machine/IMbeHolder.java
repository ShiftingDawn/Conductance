package conductance.api.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;

public interface IMbeHolder<T extends MetaBlockEntity<T>> {

	T getMetaBlockEntity();

	default boolean isRemote() {
		return this.getLevel() == null ? CAPI.isClient() : this.getLevel().isClientSide();
	}

	default boolean isValid() {
		return !this.isInvalid();
	}

	default boolean isInvalid() {
		return this.isRemoved();
	}

	/* Copied from BlockEntity */
	@Nullable
	Level getLevel();

	boolean isRemoved();

	BlockPos getBlockPos();

	BlockState getBlockState();
}
