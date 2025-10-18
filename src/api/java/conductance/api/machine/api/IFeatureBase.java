package conductance.api.machine.api;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import conductance.api.block.BlockRotationHelper;

public interface IFeatureBase {

	default boolean isValid() {
		return this instanceof final BlockEntity blockEntity ? !blockEntity.isRemoved() : !this.isInvalid();
	}

	default boolean isInvalid() {
		return this instanceof final BlockEntity blockEntity && !blockEntity.isRemoved();
	}

	void setChanged();

	@Nullable Level getLevel();

	BlockPos getBlockPos();

	BlockState getBlockState();

	default Direction getFacing() {
		return BlockRotationHelper.getFacing(this.getBlockState());
	}

	default boolean isClientSide() {
		final Level level = this.getLevel();
		return level != null && level.isClientSide();
	}

	default boolean isServerSide() {
		final Level level = this.getLevel();
		return level != null && !level.isClientSide();
	}

	default void onClient(final Consumer<Level> callback) {
		if (this.isClientSide()) {
			callback.accept(this.getLevel());
		}
	}

	default void onServer(final Consumer<ServerLevel> callback) {
		if (this.getLevel() instanceof final ServerLevel serverLevel) {
			callback.accept(serverLevel);
		}
	}
}
