package conductance.api.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

public interface IBlockEntityBlock extends EntityBlock {

	BlockEntityType<?> getBlockEntityType();

	@Override
	default BlockEntity newBlockEntity(final BlockPos pos, final BlockState state) {
		return this.getBlockEntityType().create(pos, state);
	}

	@Nullable
	@Override
	default <T extends BlockEntity> BlockEntityTicker<T> getTicker(final Level level, final BlockState state, final BlockEntityType<T> blockEntityType) {
		if (blockEntityType == this.getBlockEntityType()) {
			if (level.isClientSide) {
				return (pLevel, pPos, pState, pTile) -> {
					if (pTile instanceof final IBlockEntity blockEntity) {
						blockEntity.onClientTick();
					}
				};
			} else if (state.hasProperty(BlockStateProperties.LIT) && state.getValue(BlockStateProperties.LIT)) {
				return (pLevel, pPos, pState, pTile) -> {
					if (pTile instanceof final IBlockEntity blockEntity) {
						blockEntity.handleServerTick();
					}
				};
			}
		}
		return null;
	}
}
