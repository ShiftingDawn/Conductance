package conductance.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import com.tterrag.registrate.util.entry.RegistryEntry;
import lombok.Getter;
import conductance.api.CAPI;
import conductance.api.material.Material;
import conductance.api.material.MaterialOreType;
import conductance.client.resourcepack.MaterialOreModelHandler;
import conductance.init.ConductanceCreativeTabs;

public class MaterialOreRotatedPillarBlock extends RotatedPillarBlock implements IConductanceBlock, IMaterialOreBlock {

	@Getter
	private final Material material;
	@Getter
	private final MaterialOreType oreType;
	private final String unlocalizedName;

	public MaterialOreRotatedPillarBlock(Properties properties, Material material, MaterialOreType oreType) {
		super(properties);
		this.material = material;
		this.oreType = oreType;
		this.unlocalizedName = "block.%s.%s".formatted(material.getRegistryKey().getNamespace(), oreType.getUnlocalizedNameFactory().formatted(material.getRegistryKey().getPath()));
		if (CAPI.isClient()) {
			MaterialOreModelHandler.add(this, material, oreType);
		}
	}

	@Override
	public RegistryEntry<CreativeModeTab, CreativeModeTab> getCreativeTab() {
		return ConductanceCreativeTabs.MATERIAL_BLOCKS;
	}

	@Override
	public String getDescriptionId() {
		return this.unlocalizedName;
	}

	@Override
	public MutableComponent getName() {
		return CAPI.TRANSLATIONS.makeLocalizedName(this.getDescriptionId(), this.oreType, this.material);
	}

	@Override
	public void onPlace(final BlockState state, final Level level, final BlockPos pos, final BlockState oldState, final boolean isMoving) {
		if (this.getOreType().hasGravity()) {
			level.scheduleTick(pos, this, 2);
		}
	}

	@Override
	public BlockState updateShape(final BlockState state, final Direction direction, final BlockState neighborState, final LevelAccessor level, final BlockPos currentPos, final BlockPos neighborPos) {
		if (this.getOreType().hasGravity()) {
			level.scheduleTick(currentPos, this, 2);
		}
		return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
	}

	@Override
	public void tick(final BlockState state, final ServerLevel level, final BlockPos pos, final RandomSource random) {
		if (FallingBlock.isFree(level.getBlockState(pos.below())) && pos.getY() >= level.getMinBuildHeight()) {
			FallingBlockEntity.fall(level, pos, state);
		}
	}

	@Override
	public void animateTick(final BlockState state, final Level level, final BlockPos pos, final RandomSource random) {
		if (this.getOreType().hasGravity() && random.nextInt(16) == 0 && FallingBlock.isFree(level.getBlockState(pos.below()))) {
			final double d = (double) pos.getX() + random.nextDouble();
			final double e = (double) pos.getY() - 0.05;
			final double f = (double) pos.getZ() + random.nextDouble();
			level.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, state), d, e, f, 0.0, 0.0, 0.0);
		}
	}
}
