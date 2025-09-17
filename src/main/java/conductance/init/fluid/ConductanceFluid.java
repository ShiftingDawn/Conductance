package conductance.init.fluid;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidType;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public final class ConductanceFluid extends Fluid {

	private final Supplier<FluidType> fluidType;
	private final Supplier<Item> bucket;
	private final @Nullable Supplier<Block> block;

	@Override
	public Item getBucket() {
		return this.bucket.get();
	}

	@Override
	protected boolean canBeReplacedWith(final FluidState fluidState, final BlockGetter blockGetter, final BlockPos blockPos, final Fluid fluid, final Direction direction) {
		return true;
	}

	@Override
	protected Vec3 getFlow(final BlockGetter blockGetter, final BlockPos blockPos, final FluidState fluidState) {
		return Vec3.ZERO;
	}

	@Override
	public int getTickDelay(final LevelReader levelReader) {
		return 0;
	}

	@Override
	protected float getExplosionResistance() {
		return 0;
	}

	@Override
	public float getHeight(final FluidState fluidState, final BlockGetter blockGetter, final BlockPos blockPos) {
		return 0;
	}

	@Override
	public float getOwnHeight(final FluidState fluidState) {
		return 0;
	}

	@Override
	protected BlockState createLegacyBlock(final FluidState fluidState) {
		return this.block != null ? this.block.get().defaultBlockState() : Blocks.AIR.defaultBlockState();
	}

	@Override
	public boolean isSource(final FluidState fluidState) {
		return true;
	}

	@Override
	public int getAmount(final FluidState fluidState) {
		return 0;
	}

	@Override
	public VoxelShape getShape(final FluidState fluidState, final BlockGetter blockGetter, final BlockPos blockPos) {
		return Shapes.block();
	}

	@Override
	public FluidType getFluidType() {
		return this.fluidType.get();
	}
}
