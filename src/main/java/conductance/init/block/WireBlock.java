package conductance.init.block;

import java.util.List;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import lombok.Getter;
import conductance.api.NCMaterialProps;
import conductance.api.NCMaterialTraits;
import conductance.api.block.IGeneratedMiningTags;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.material.MaterialTraitWire;
import conductance.core.block.IMaterialBlock;
import conductance.init.ConductanceBlockEntities;
import conductance.lib.pipenet.EnergyNet;
import conductance.lib.pipenet.IWireNode;
import conductance.lib.pipenet.LevelEnergyNet;
import conductance.lib.pipenet.WireData;
import conductance.lib.pipenet.WireType;

public final class WireBlock extends PipeBlock<IWireNode, WireData, LevelEnergyNet> implements IMaterialBlock, IGeneratedMiningTags {

	private final @Getter Material material;
	private final @Getter WireType wireType;
	private final MutableComponent name;

	public WireBlock(final Properties props, final Material material, final WireType wireType) {
		super(props.noOcclusion().dynamicShape(), EnergyNet.TYPE);
		this.material = material;
		this.wireType = wireType;
		this.name = Component.translatable(this.getHandler().makeDescriptionId(material), Component.translatable(material.getDescriptionId()));
	}

	@Override
	protected VoxelShape makeBaseShape() {
		final int start = (16 - this.wireType.getVoxels()) / 2;
		final int end = start + this.wireType.getVoxels();
		return Block.box(start, start, start, end, end, end);
	}

	@Override
	protected void makeExtensionShapes(final Map<Direction, VoxelShape> map) {
		final double start = ((double) (16 - this.wireType.getVoxels()) / 2) / 16.0;
		final double end = start + (this.wireType.getVoxels() / 16.0);
		for (final Direction direction : Direction.values()) {
			map.put(direction, Shapes.create(new AABB(
				direction.getStepX() == 0 ? start : direction.getStepX() > 0 ? end : 0,
				direction.getStepY() == 0 ? start : direction.getStepY() > 0 ? end : 0,
				direction.getStepZ() == 0 ? start : direction.getStepZ() > 0 ? end : 0,
				direction.getStepX() == 0 ? end : direction.getStepX() > 0 ? 1 : start,
				direction.getStepY() == 0 ? end : direction.getStepY() > 0 ? 1 : start,
				direction.getStepZ() == 0 ? end : direction.getStepZ() > 0 ? 1 : start
			)));
		}
	}

	@Override
	public BlockEntityType<? extends PipeBlockEntity<IWireNode, WireData, LevelEnergyNet>> getBlockEntityType() {
		return ConductanceBlockEntities.WIRE.get();
	}

	public WireData getBaseProps() {
		final MaterialTraitWire trait = this.material.getTrait(NCMaterialTraits.WIRE);
		assert trait != null;
		return new WireData(trait.getTier().getVoltage(), trait.getAmperage());
	}

	public WireData getRealProps() {
		return this.wireType.getPhysicalProperties(this.getBaseProps());
	}

	@Override
	public MaterialGenerationHandler getHandler() {
		return this.wireType.getHandler();
	}

	@Override
	public List<TagKey<Block>> getRequiredToolTypeTag() {
		final List<TagKey<Block>> tags = this.getHandler().getMiningToolTypeTags();
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
}
