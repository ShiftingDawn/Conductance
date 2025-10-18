package conductance.init.block;

import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import lombok.Getter;
import conductance.api.NCMaterialProps;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.core.block.IMaterialBlock;

public final class MaterialBlock extends SimpleBlock implements IMaterialBlock {

	private final @Getter Material material;
	private final @Getter MaterialGenerationHandler handler;
	private final MutableComponent name;

	public MaterialBlock(final Properties properties, final Material material, final MaterialGenerationHandler handler) {
		super(properties);
		this.material = material;
		this.handler = handler;
		this.name = Component.translatable(handler.makeDescriptionId(material), Component.translatable(material.getDescriptionId()));
	}

	@Override
	protected boolean skipRendering(final BlockState state, final BlockState adjacentState, final Direction direction) {
		if (!this.handler.shouldOccludeBlocks()) {
			if (adjacentState.getBlock() instanceof final IMaterialBlock materialBlock && materialBlock.getHandler() == this.handler) {
				return true;
			}
		}
		return super.skipRendering(state, adjacentState, direction);
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
}
