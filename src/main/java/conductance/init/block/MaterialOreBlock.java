package conductance.init.block;

import java.util.List;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import lombok.Getter;
import conductance.api.NCMaterialProps;
import conductance.api.block.IGeneratedMiningTags;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.material.MaterialOreBearer;
import conductance.core.block.IMaterialBlock;

public final class MaterialOreBlock extends Block implements IMaterialBlock, IGeneratedMiningTags {

	private final @Getter Material material;
	private final @Getter MaterialGenerationHandler handler;
	private final @Getter MaterialOreBearer oreBearer;
	private final MutableComponent name;

	public MaterialOreBlock(final Properties properties, final Material material, final MaterialGenerationHandler handler) {
		super(properties);
		this.material = material;
		this.handler = handler;
		this.oreBearer = Objects.requireNonNull(handler.getOreBearer());
		this.name = Component.translatable(handler.makeDescriptionId(material), Component.translatable(material.getDescriptionId()));
	}

	@Override
	public List<TagKey<Block>> getRequiredToolTypeTag() {
		final List<TagKey<Block>> tags = this.handler.getMiningToolTypeTags();
		return !tags.isEmpty() ? tags : List.of(BlockTags.MINEABLE_WITH_PICKAXE);
	}

	@Override
	public List<TagKey<Block>> getRequiredToolLevelTag() {
		return List.of(this.material.getProp(NCMaterialProps.REQUIRED_TOOL_LEVEL, BlockTags.NEEDS_STONE_TOOL));
	}

	@Override
	public MutableComponent getName() {
		return this.name;
	}

	@Override
	public void onPlace(final BlockState state, final Level level, final BlockPos pos, final BlockState oldState, final boolean isMoving) {
		if (this.getOreBearer().hasGravity()) {
			level.scheduleTick(pos, this, 2);
		}
	}

	@Override
	protected BlockState updateShape(
		final BlockState state, final LevelReader level, final ScheduledTickAccess scheduledTickAccess, final BlockPos pos, final Direction direction, final BlockPos neighborPos, final BlockState neighborState,
		final RandomSource random
	) {
		if (this.getOreBearer().hasGravity()) {
			scheduledTickAccess.scheduleTick(pos, this, 2);
		}
		return super.updateShape(state, level, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
	}

	@Override
	public void tick(final BlockState state, final ServerLevel level, final BlockPos pos, final RandomSource random) {
		if (FallingBlock.isFree(level.getBlockState(pos.below())) && pos.getY() >= level.getMinY()) {
			FallingBlockEntity.fall(level, pos, state);
		}
	}

	@Override
	public void animateTick(final BlockState state, final Level level, final BlockPos pos, final RandomSource random) {
		if (this.getOreBearer().hasGravity() && random.nextInt(16) == 0 && FallingBlock.isFree(level.getBlockState(pos.below()))) {
			final double d = (double) pos.getX() + random.nextDouble();
			final double e = (double) pos.getY() - 0.05;
			final double f = (double) pos.getZ() + random.nextDouble();
			level.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, state), d, e, f, 0.0, 0.0, 0.0);
		}
	}
}
